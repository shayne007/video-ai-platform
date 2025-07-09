package com.keensense.image.repository.bigdata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.config.SpringContext;
import com.keensense.common.exception.VideoException;
import com.keensense.image.config.FastDfsConfig;
import com.keensense.image.feign.FeignToTask;
import com.keensense.image.repository.ImageRepository;
import com.keensense.image.utils.DateUtil;
import com.keensense.image.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.base.Charsets.UTF_8;

/**
 * FastDFS distributed file system
 */
@Repository("fastDfsImageRepository")

@Slf4j
public class FastDfsImageRepository implements ImageRepository {

    private FastDfsConfig config = SpringContext.getBean(FastDfsConfig.class);
    private String auth;

    Properties properties = new Properties();

    private String[] remoteUrlArray;
    private String[] groupArray;
    private Map<String, String> map = new HashMap<>();
    @Autowired
    FeignToTask feignToTask;

    private static Map<Integer, ThreadPoolExecutor> poolMap = new HashMap<Integer, ThreadPoolExecutor>();


    @PostConstruct
    private void initProperties() {
        for (int i = 0; i < 20; i++) {
            poolMap.put(i, new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>()));
        }

        properties.put("fastdfs.connect_timeout_in_seconds", config.getConnectTimeout());
        properties.put("fastdfs.network_timeout_in_seconds", config.getNetworkTimeout());
        properties.put("fastdfs.tracker_servers", config.getTrackerServer());
        properties.put("fastdfs.http.http_tracker_http_port", config.getTrackerServerPort());

        try {
            ClientGlobal.initByProperties(properties);
        } catch (Exception e) {
            log.error("", e);
        }
        remoteUrlArray = config.getRemoteUrl().split(",");
        groupArray = config.getGroup().split(",");
        for (int i = 0; i < remoteUrlArray.length; i++) {
            map.put(groupArray[i], remoteUrlArray[i]);
        }

        String[] user = config.getUsername().split(",");
        String userName = user[0];
        String userPath = user[1];
        auth = "Basic " + new String(
                Base64.getEncoder().encode((userName + ":" + userPath).getBytes()));
    }

    @Override
    public String saveToFileServer(String imageStr, String fileExtentionName, String id) {
        String url = "";
        TrackerServer trackerServer = null;
        byte[] bytes = null;
        try {
            bytes = Base64.getDecoder().decode(imageStr.getBytes(UTF_8));
            imageStr = null;

            TrackerClient tracker = new TrackerClient();
            trackerServer = tracker.getConnection();

            StorageClient storageClient = new StorageClient(trackerServer, null);
            String[] fileIds = storageClient.upload_file(bytes, fileExtentionName, null);
            if (fileIds != null) {
                String prefix = map.get(fileIds[0]);
                url = prefix + "/" + fileIds[0] + "/" + fileIds[1];
                log.info("the image url is {}", url);
            } else {//可能有网络波动，导致丢包，从而导致返回的结果是空的，这个时候重试一次，减少出错概率
                log.error("upload file faild retry it {} ", id);
                tracker = new TrackerClient();
                trackerServer = tracker.getConnection();
                storageClient = new StorageClient(trackerServer, null);
                String[] fileIds1 = storageClient.upload_file(bytes, fileExtentionName, null);
                if (fileIds1 != null) {
                    String prefix = map.get(fileIds1[0]);
                    url = prefix + "/" + fileIds1[0] + "/" + fileIds1[1];
                    log.info("the image url is {}", url);
                } else {
                    log.error("upload file {} error return null ", id);
//                    log.error(Base64.getEncoder().encodeToString(bytes));
                }
            }
        } catch (Exception e) {
            log.error("upload file error " + id, e);
//            log.error(Base64.getEncoder().encodeToString(bytes));
        } finally {
            if (trackerServer != null) {
                try {
                    trackerServer.close();
                } catch (IOException e1) {
                    log.error("", e1);
                }
            }
        }
        return url;
    }

    @Override
    public long batchDelete(String serialNumber, String time) {
        JSONObject taskCallback = new JSONObject();
        taskCallback.put("serialnumber", serialNumber);
        taskCallback.put("time", time);
        taskCallback.put("status", true);
        try {
            Date start = DateUtil.generatorDate(time, "start");
            Date end = DateUtil.generatorDate(time, "end");
            JSONObject object = generateQueryObject(serialNumber, start, end);
//            //log.debug("{}", object);
            Map<String, String> header = new HashMap<>();
            header.put("Content-Type", "application/json");
            header.put("Authorization", auth);
            String response = HttpClientUtil
                    .post("http://" + config.getHost() + ":" + config.getPort() + "/image_result/_search?scroll=10m",
                            object.toJSONString(), header);
            AtomicLong total = new AtomicLong();
            if (StringUtils.isEmpty(response)) {
                log.error("query es response is empty, query para {}", object.toJSONString());
            }
            JSONObject responseObject = JSONObject.parseObject(response);
            String scroll = responseObject.getString("_scroll_id");
            JSONObject hits = responseObject.getJSONObject("hits");
            JSONArray resultArray = hits.getJSONArray("hits");
            deleteList(resultArray, total);
            while (resultArray.size() != 0) {
                object = new JSONObject();
                object.put("scroll_id", scroll);
                object.put("scroll", "10m");
                response = HttpClientUtil
                        .post("http://" + config.getHost() + ":" + config.getPort() + "/_search/scroll",
                                object.toJSONString(), header);
                responseObject = JSONObject.parseObject(response);
                scroll = responseObject.getString("_scroll_id");
                hits = responseObject.getJSONObject("hits");
                resultArray = hits.getJSONArray("hits");
                deleteList(resultArray, total);
            }
            log.info("delete total {} images, analysisId: {} time: {} ", total.get(), serialNumber,
                    time);
            JSONObject deleteObject = new JSONObject();
            deleteObject.put("scroll_id", scroll);
            HttpClientUtil
                    .delete("http://" + config.getHost() + ":" + config.getPort() + "/_search/scroll",
                            deleteObject.toJSONString(),
                            header);

            deleteImageResult(serialNumber, start, end, header);
        } catch (Exception e) {
            taskCallback.put("status", false);
            log.error("", e);
        }
        feignToTask.setDeleteTaskStatus(taskCallback.toJSONString());

        return 0;
    }

    private void deleteImageResult(String serialNumber, Date start, Date end, Map header) {
        JSONArray must = new JSONArray();
        JSONObject matchPhrase = new JSONObject();
        matchPhrase.put("analysisid", serialNumber);
        JSONObject matchPhraseObject = new JSONObject();
        matchPhraseObject.put("match_phrase", matchPhrase);
        must.add(matchPhraseObject);
        if (null != start && null != end) {
            JSONObject startObject = new JSONObject();
            JSONObject startRange = new JSONObject();
            JSONObject startDatetime = new JSONObject();
            startDatetime.put("gte", start.getTime());
            startRange.put("datetime", startDatetime);
            startObject.put("range", startRange);
            must.add(startObject);

            JSONObject endObject = new JSONObject();
            JSONObject endRange = new JSONObject();
            JSONObject endDatetime = new JSONObject();
            endDatetime.put("lt", end.getTime());
            endRange.put("datetime", endDatetime);
            endObject.put("range", endRange);
            must.add(endObject);
        }

        JSONObject object = new JSONObject();
        JSONObject query = new JSONObject();
        JSONObject bool = new JSONObject();
        bool.put("must", must);
        query.put("bool", bool);
        object.put("query", query);

        HttpClientUtil
                .post("http://" + config.getHost() + ":" + config.getPort() + "/image_result/_delete_by_query",
                        object.toJSONString(),
                        header);
    }


    private boolean delete(String group, String url, AtomicLong total, CountDownLatch latch,
                           AtomicBoolean isfailed) {
        TrackerServer trackerServer = null;
        int ret = 0;
        try {
            TrackerClient tracker = new TrackerClient();
            trackerServer = tracker.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            int r = storageClient.delete_file(group, url);
            if (r != 0 && r != 2) {//0为删除成功，2为找不到文件，22位找不到路径
                isfailed.set(true);
                log.error("delete fdfs file faild, group:{} url:{} code:{}", group, url, r);
            }
            total.incrementAndGet();
        } catch (Exception e) {
            isfailed.set(true);
            log.error("", e);
        } finally {
            latch.countDown();
            if (trackerServer != null) {
                try {
                    trackerServer.close();
                } catch (IOException e1) {
                    log.error("", e1);
                }
            }
        }

        return ret == 0 ? true : false;
    }

    private void deleteList(JSONArray resultArray, AtomicLong total) {
        log.info("deleteList:{}", resultArray.size());
        AtomicBoolean isFailed = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(resultArray.size());
        JSONObject result = null;
        for (int i = 0; i < resultArray.size(); i++) {
            result = resultArray.getJSONObject(i);
            JSONObject source = result.getJSONObject("_source");
            String group = source.getString("group");
            String url = source.getString("url");
            if (url.startsWith("/")) {//如果路径为/开头，则去掉
                url = url.substring(1);
            }
            String num = url.substring(1, 3);
            int index = new BigInteger(num, 16).intValue();//获取磁盘目录index（16进制），并且转为10进制
            String path = url;
            ThreadPoolExecutor pool = poolMap.get(index);//每个线程均匀负责磁盘的删除
            pool.execute(() -> delete(group, path, total, latch, isFailed));
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("", e);
        }
        if (isFailed.get()) {
            throw new VideoException("failed");
        }
    }

    private JSONObject generateQueryObject(String serialnumber, Date start, Date end) {
        JSONObject object = new JSONObject();
        object.put("size", "51200");
        JSONObject queryObject = new JSONObject();
        JSONObject boolObject = new JSONObject();
        JSONArray mustObject = new JSONArray();
        JSONObject matchphraseObject = new JSONObject();
        matchphraseObject.put("analysisid", serialnumber);
        JSONObject arrayObject1 = new JSONObject();
        arrayObject1.put("match_phrase", matchphraseObject);
        mustObject.add(arrayObject1);

        if (null != start) {
            JSONObject arrayObject2 = new JSONObject();
            JSONObject rangeObject = new JSONObject();
            JSONObject dateTimeObject = new JSONObject();
            dateTimeObject.put("gte", start);
            dateTimeObject.put("lte", end);
            rangeObject.put("datetime", dateTimeObject);
            arrayObject2.put("range", rangeObject);
            mustObject.add(arrayObject2);
        }
        boolObject.put("must", mustObject);
        queryObject.put("bool", boolObject);
        object.put("query", queryObject);

        return object;
    }
}