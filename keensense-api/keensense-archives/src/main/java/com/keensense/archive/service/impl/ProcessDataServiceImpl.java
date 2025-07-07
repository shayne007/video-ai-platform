package com.keensense.archive.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.keensense.archive.config.ArchiveConfig;
import com.keensense.archive.feign.FeignImageService;
import com.keensense.archive.feign.FeignSearchService;
import com.keensense.archive.service.ProcessDataService;
import com.keensense.archive.utils.DownloadImageUtil;
import com.keensense.archive.utils.ElasticsearchUtil;
import com.keensense.archive.utils.HttpClientUtil;
import com.keensense.archive.utils.KafkaUtil;
import com.keensense.archive.utils.RedisUtil;
import com.keensense.archive.vo.ArchiveTitle;
import com.keensense.common.config.SpringContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * Created by memory_fu on 2019/12/26.
 */
@Slf4j
public class ProcessDataServiceImpl implements ProcessDataService {

    private static final String SPLIT_STR = "#@#";

    private ArchiveConfig archiveConfig = SpringContext.getBean(ArchiveConfig.class);

    private FeignImageService feignImageService = SpringContext.getBean(FeignImageService.class);

    private FeignSearchService feignSearchService = SpringContext.getBean(FeignSearchService.class);

    private ElasticsearchUtil elasticsearchUtil = SpringContext.getBean(ElasticsearchUtil.class);

    @Override
    public void processClusterData() {

        KafkaConsumer<String, String> kafkaConsumer = KafkaUtil
                .initConsumerParams(archiveConfig.getKafkaBrokerList(),
                        archiveConfig.getKafkaTopicFirmReceive(),
                        archiveConfig.getKafkaGroupIdFirmReceive(), 0);
        while (true) {
            try {

                JSONArray clusterData = receiveClusterData(kafkaConsumer, 5000);
                log.info("==== processClusterData clusterData:" + clusterData.size());

                JSONArray archiveData = analysisClusterData(clusterData);
                log.info("==== processClusterData archiveData:" + archiveData.size());

                kafkaConsumer.commitAsync();

                pushArchiveData(archiveData);
            } catch (Exception e) {
                log.error("==== processClusterData exception:", e);
            }
        }
    }

    /**
     * 拉取kafka中聚类数据
     *
     * @return 聚类数据
     */
    private JSONArray receiveClusterData(KafkaConsumer<String, String> kafkaConsumer,
                                         long timeout) {

        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(timeout);

        JSONArray array = new JSONArray();
        for (ConsumerRecord<String, String> record : consumerRecords) {
            String value = record.value();
            JSONObject jsonObject = JSONObject.parseObject(value);
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.size(); i++) {
                JSONObject dataJSONObject = data.getJSONObject(i);
                array.add(dataJSONObject);
            }
        }
        return array;
    }


    /**
     * 下载图片并保存到fdfs
     *
     * @return 存储fdfs后返回url
     */
    private String savePicByArchives(String imgUrl) throws IOException {
        try {
            // 下载图片
            String imageBase64 = DownloadImageUtil.downloadImage(imgUrl);
            imageBase64 = imageBase64.replaceAll("\\r|\\n", "");
            // 存储图片到fdfs得到图片url
            String saveImageInfo = feignImageService
                    .imageArchivesSave(UUID.randomUUID().toString(), imageBase64);
            JSONObject jsonObject = JSONObject.parseObject(saveImageInfo);
            String imageUrl = jsonObject.getString("ImageUrl");
            return imageUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 特征保存到搜图模块
     *
     * @param feature      特征
     * @param clusterIndex 特征id
     * @param type         特征类型
     * @param firm         算法类型
     */
    private String saveFeature(String feature, String clusterIndex, int type, int firm)
            throws IOException {

        String[] featureHost = archiveConfig.getFeatureHost().split(",");
        int i = new Random().nextInt(featureHost.length);
        String host = featureHost[i];
        String url = "http://" + host + ":" + ArchiveConfig.featurePort
                + "/enrollfeature";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("repo", archiveConfig.getTaskNum());
        jsonObject.put("uuid", clusterIndex);
        jsonObject.put("type", type);
        jsonObject.put("firm", firm);
        jsonObject.put("feature", feature);

        String respone = HttpClientUtil.doPost(url, jsonObject.toJSONString(), null);

        return respone;
    }

    /**
     * 根据人脸ID更新档案ID
     */
    private String updateArchivesidByFaceId(String faceId, String archivesId) {

        JSONObject jsonObject = new JSONObject();

        JSONObject faceListObject = new JSONObject();
        JSONArray faceObjects = new JSONArray();

        JSONObject faceObject = new JSONObject();
        faceObject.put("FaceID", faceId);
        JSONObject dataObject = new JSONObject();
        dataObject.put("ArchivesId", archivesId);
        faceObject.put("Data", dataObject);
        faceObjects.add(faceObject);

        faceListObject.put("FaceObject", faceObjects);
        jsonObject.put("FaceListObject", faceListObject);

        return feignSearchService.updateFaces(jsonObject);
    }

    /**
     * 解析kafka中获取到的聚类数据
     *
     * @return 档案封面数据
     */
    private JSONArray analysisClusterData(JSONArray jsonArray) throws Exception {

        List<ArchiveTitle> archiveList = Lists.newArrayList();
        List<ArchiveTitle> archiveListCombine = Lists.newArrayList();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String clusterIndex = jsonObject.getString("clusterIndex");
            String uuid = jsonObject.getString("uuid");
            Boolean clusterFlag = jsonObject.getBoolean("clusterFlag");
            JSONArray combineCluster = jsonObject.getJSONArray("combineCluster");
            List<String> combineClusterList = JSONObject
                    .parseArray(combineCluster.toJSONString(), String.class);

            if (clusterFlag) {
                // 根据uuid从redis中获取特征数据和图片url
                String valueByKey = RedisUtil.getValueByKey(uuid);
                String[] split = null;
                try {
                    split = valueByKey.split(SPLIT_STR);
                } catch (Exception e) {
                    log.error("==== temp exception check,the uuid is {}, the valueByKey is {}.", uuid, valueByKey);
                    log.error("==== temp exception check:", e);
                }
                String feature = split[0];
                String url = split[1];
                String firm = split[2];
                long addTime = Long.valueOf(split[3]);
                long sumTime = System.currentTimeMillis() - addTime;
                log.info("==== clustering time is {}, uuid is {},", sumTime, uuid);
                if (sumTime > 5000) {
                    log.info("==== clustering over long time is {}, uuid is {},", sumTime, uuid);
                }

                // 根据url下载图片并存入fdfs中
                String archiveTitleUrl = savePicByArchives(url);
                // 特征数据存入搜图模块中
                saveFeature(feature, clusterIndex, 3, Integer.valueOf(firm));
                // 根据uuid更新结构化数据档案ID 为clusterIndex
                updateArchivesidByFaceId(uuid, clusterIndex);
                // 构建档案封面数据
                ArchiveTitle archiveTitle = new ArchiveTitle(clusterIndex, archiveTitleUrl,
                        System.currentTimeMillis(), combineClusterList, Boolean.TRUE.toString());
                // 档案封面数据存入es中
                archiveList.add(archiveTitle);
            } else {
                int size = combineCluster.size();
                if (size > 1) {

                    // 有档案合并发生需把合并信息推送业务侧
                    ArchiveTitle archiveTitle = new ArchiveTitle(clusterIndex, null,
                            System.currentTimeMillis(), combineClusterList, Boolean.FALSE.toString());
                    archiveListCombine.add(archiveTitle);

                    //被合并档案封面数据
                    List<Object> list = combineCluster.subList(1, combineCluster.size());
                    // 根据clusterIndex删除被合并聚类数据(搜图模块、图片数据、档案封面数据)

                    for (Object obj : list) {
                        String oldClusterIndex = String.valueOf(obj);
                        log.info("==== delete oldClusterIndex,the oldClusterIndex is {}", oldClusterIndex);
                        deleteFeatureById(oldClusterIndex, 3);
                    }

//                    deletePicById(clusterIndex);
                    log.info("==== analysisClusterData deleteByQuery list:{}", JSONObject.toJSONString(list));
                    int deleteEsCount = elasticsearchUtil.deleteByQuery("clusterIndex", list);
                    log.info("==== analysisClusterData deleteByQuery count:{}", deleteEsCount);

                    // 根据clusterIndex更新为合并后的clusterIndex
                    log.info("==== analysisClusterData updateByQuery clusterIndex:{}", clusterIndex);
                    int updateEsCount = 0;
                    for (int j = 0; j < 5; j++) {
                        updateEsCount = elasticsearchUtil.updateByQuery("archivesid", list, "archivesid",
                                clusterIndex, "face_result");
                        if (updateEsCount != 0) {
                            break;
                        }
                        log.info("==== analysisClusterData updateByQuery clusterIndex sleep 600ms.");
                        Thread.sleep(600);
                    }
                    log.info("==== analysisClusterData updateByQuery clusterIndex count:{}", updateEsCount);
                } else {
                    // 根据人脸uuid更新结构化数据档案ID 为clusterIndex
                    log.info("==== analysisClusterData updateByQuery uuid:{}", uuid);
                    ArrayList<Object> list = Lists.newArrayList(uuid);

                    int updateEsCount = 0;
                    for (int j = 0; j < 5; j++) {
                        updateEsCount = elasticsearchUtil
                                .updateByQuery("faceid", list, "archivesid", clusterIndex, "face_result");
                        if (updateEsCount != 0) {
                            break;
                        }
                        log.info("==== analysisClusterData updateByQuery uuid sleep 600ms.");
                        Thread.sleep(600);
                    }
                    log.info("==== analysisClusterData updateByQuery uuid count:{}", updateEsCount);
//                    updateArchivesidByFaceId(uuid, clusterIndex);
                }
            }
        }

        log.info("==== analysisClusterData batchInsert archiveList:{}", JSONObject.toJSONString(archiveList));
        int batchInsertCount = elasticsearchUtil.batchInsert(archiveList);
        log.info("==== analysisClusterData batchInsert count:{}", batchInsertCount);

        archiveList.addAll(archiveListCombine);
        JSONArray archiveDataArray = JSONArray.parseArray(JSON.toJSONString(archiveList));

        log.info("==== archiveList json:" + JSON.toJSONString(archiveList));

        return archiveDataArray;
    }

    /**
     * 删除特征
     */
    private String deleteFeatureById(String clusterIndex, int type) throws IOException {

        String[] featureHost = archiveConfig.getFeatureHost().split(",");

        String respone = org.apache.commons.lang.StringUtils.EMPTY;
        for (String host : featureHost) {
            String url = "http://" + host + ":" + ArchiveConfig.featurePort
                    + "/deletefeature";

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", type);
            jsonObject.put("uuid", clusterIndex);
            jsonObject.put("repo", archiveConfig.getTaskNum());

            respone = HttpClientUtil.doPost(url, jsonObject.toJSONString(), null);
        }

        return respone;
    }

    /**
     * 删除图片
     */
    private String deletePicById(String clusterIndex) {
        return null;
    }

    /**
     * 推送档案封面数据到kafka供业务使用
     */
    private void pushArchiveData(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            KafkaUtil
                    .sendMessage(archiveConfig.getKafkaTopicProfessionalSend(),
                            jsonObject.toJSONString(), archiveConfig.getKafkaBrokerList());
        }
    }


}