package com.keensense.dataconvert.api.alg.impl;

import com.keensense.dataconvert.api.alg.IAlgorithm;
import com.keensense.dataconvert.api.request.vo.AlgKafkaVo;
import com.keensense.dataconvert.api.util.PushToKafkaUtil;
import com.keensense.dataconvert.api.util.area.AreaCompare;
import com.keensense.dataconvert.biz.common.consts.CommonConst;
import com.keensense.dataconvert.biz.entity.PictureInfo;
import com.keensense.dataconvert.biz.kafka.producer.AppProducer;
import com.keensense.dataconvert.framework.common.ext.spring.SpringContextHolder;
import com.keensense.dataconvert.framework.common.loadbalance.WeightRoundRobin;
import com.keensense.dataconvert.framework.common.utils.cache.EhCacheUtil;
import com.keensense.dataconvert.framework.config.ehcache.constants.EhcacheConstant;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.datastruct.WeekArray;
import com.loocme.sys.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.alg.impl
 * @Description： <p> AppAlgorithmImpl </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 16:54
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 */
public class AppAlgorithmImpl implements IAlgorithm , Serializable {

    private static final long serialVersionUID = -3654781618581973049L;

    private static final Logger logger = LoggerFactory.getLogger(AppAlgorithmImpl.class);

    private static AppProducer appProducer = SpringContextHolder.getBean(AppProducer.class);

    /**
     * 特征识别参数封装
     * @param config
     * @param imgArr
     * @return
     */
    @Override
    public String getRequestParam(Var config, JSONArray imgArr) {
        JSONObject requestParam = new JSONObject();
        requestParam.put("isDetectFullFrame",1);
        requestParam.put("scenes",0);
        requestParam.put("images",imgArr);
        return requestParam.toString();
    }

    @Override
    public String getRequestUrl() {
        if (CommonConst.RECOG_VIDEO_FEATURE_CLUSTER_ENABLE){
            return WeightRoundRobin.getServer();
        }
        return CommonConst.RECOG_VIDEO_FEATURE_URL;
    }


    @Override
    public String getResponseCode(Var responseVar) {
        if (null == responseVar || responseVar.isNull()) {
            return "NO_RESPONSE";
        }
        if (StringUtil.isNotNull(responseVar.getString(CommonConst.RESULT_CODE_KEY))) {
            return responseVar.getString(CommonConst.RESULT_CODE_KEY);
        }
        return null;
    }


    /**
     * 将请求数据解析出来推送到kafka
     *        fix  20190730 由于接口返回数据结构问题
     *              需要根据imageId 分组 在排序 取出分组->排序后->面积最大的那个数据
     * @param list
     * @param responseVar
     */
    @Override
    public void dealResponse(List<PictureInfo> list, Var responseVar) {
        // 将数据解析出来 推送到kafka
        WeekArray featureArray = responseVar.getArray(CommonConst.RESULT_CONTENT_KEY);
        Map<String,List<AreaCompare>> resultGroupMap;
        List<AreaCompare> compareListAll = new ArrayList<>();
        if (!featureArray.isNull()){
            logger.info("=== [处理特征值&推送到Kafka]dealResponse:featureSize:{} ===",featureArray.getSize());
            Var featureResult;
            AreaCompare areaCompare;
            // 将所有的结果数据都封装到一个 compareList - all
            for (int i = 0; i < featureArray.getSize(); i++) {
                featureResult = featureArray.get(i+"");
                logger.debug("== featureResult :\n{}\n",featureResult);
                String imageId = featureResult.getString("imageId");
                int w = featureResult.getInt("w");
                int h = featureResult.getInt("h");
                areaCompare = new AreaCompare(i, imageId, w, h);
                compareListAll.add(areaCompare);
            }
            // 接口返回的数据 1-n [如果只输入一个image 也就是group只有一组 因为只有一个imageId]
            // 如果是n-m [输入的数据是一次多个图片 那么返回的数据 就需要进行分组 by ImageId]
            if (!compareListAll.isEmpty()){
                resultGroupMap = PushToKafkaUtil.groupByImageId(compareListAll);
                for (Map.Entry<String,List<AreaCompare>> entry : resultGroupMap.entrySet()) {
                    String imageId = entry.getKey();
                    // 分组后的 list 选取imageId 对应的面积最大的一个特征数据 推送到kafka
                    List<AreaCompare> compareList = entry.getValue();
                    if (!compareList.isEmpty()){
                        //按照面积排序
                        Collections.sort(compareList, new Comparator<AreaCompare>() {
                            @Override
                            public int compare(AreaCompare ac1, AreaCompare ac2) {
                                return ac2.getCountArea() - ac1.getCountArea();
                            }
                        });
                        int resultIndex = compareList.get(0).getIndex();
                        Var var = featureArray.get(resultIndex + "");
                        String featureVector = var.getString("featureVector");
                        AlgKafkaVo algKafkaVo = (AlgKafkaVo)EhCacheUtil.get(EhcacheConstant.EHCAHCHE_PIC_PUSH_KAFKA_CACHE, imageId);
                        JSONObject kafkaJson = PushToKafkaUtil.convertPushToKafka(algKafkaVo, featureVector);
                        appProducer.sendConVertDataToKafka(kafkaJson.toString(),algKafkaVo.getCreatTime());
                    }
                }
            }
        }
    }




}
