package com.keensense.search.repository.bigdata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.exception.VideoException;
import com.keensense.search.domain.ImageResult;
import com.keensense.search.feign.FeignToTask;
import com.keensense.search.repository.ImageRepository;
import com.keensense.search.repository.StructuringDataRepository;
import com.keensense.search.utils.DateUtil;
import com.keensense.search.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zhanx xiaohui on 2019-08-12.
 */
@Repository("fastDfsImageRepository")
@RefreshScope
@Slf4j
public class FastDfsImageRepository implements ImageRepository {

    @Resource(name = "${structuringData.repository}")
    protected StructuringDataRepository structuringDataRepository;

    @Value("${origin.es.datasource.port}")
    String port;
    @Value("${origin.es.datasource.host}")
    String host;
    @Value("${origin.es.datasource.username}")
    String username;

    private String auth;

    @Value("${fastdfs.connecttimeout}")
    String connectTimeout;
    @Value("${fastdfs.networktimeout}")
    String networkTimeout;
    @Value("${fastdfs.trackerserver}")
    String trackerServer;
    @Value("${fastdfs.trackerserver.port}")
    String trackerServerPort;

    @Value("${image.url.remote}")
    String remoteUrl;

    @Autowired
    FeignToTask feignToTask;

    Properties properties = new Properties();

//    private ExecutorService service = Executors.newFixedThreadPool(10);

    //    private Map<Integer, ExecutorService> serviceMap = new HashMap<Integer, ExecutorService>();
    private static Map<Integer, ThreadPoolExecutor> poolMap = new HashMap<Integer, ThreadPoolExecutor>();

    private static final Random RANDOM = new SecureRandom();

    private String[] remoteUrlArray;

    @PostConstruct
    private void initProperties() {
        for (int i = 0; i < 20; i++) {
//            serviceMap.put(i, Executors.newSingleThreadExecutor());
            poolMap.put(i, new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>()));
        }
        properties.put("fastdfs.connect_timeout_in_seconds", connectTimeout);
        properties.put("fastdfs.network_timeout_in_seconds", networkTimeout);
        properties.put("fastdfs.tracker_servers", trackerServer);
        properties.put("fastdfs.http.http_tracker_http_port", trackerServerPort);
        try {
            ClientGlobal.initByProperties(properties);
        } catch (Exception e) {
            log.error("", e);
        }

        remoteUrlArray = remoteUrl.split(",");

        String[] user = username.split(",");
        String userName = user[0];
        String userPath = user[1];
        auth = "Basic " + new String(
                Base64.getEncoder().encode((userName + ":" + userPath).getBytes()));
    }

    @Override
    public String getUrl(JSONObject jsonObject, String imageStr, String type, String pricuteType,
                         String serialNumber, String root, String appearTimeName, String disappearTimeName,
                         Date markTime, String id) {
        String url = "";
        TrackerServer trackerServer = null;
        try {
            byte[] bytes = Base64.getDecoder().decode(imageStr.getBytes());

            TrackerClient tracker = new TrackerClient();
            trackerServer = tracker.getConnection();

            StorageClient storageClient = new StorageClient(trackerServer, null);
            String[] fileIds = storageClient.upload_file(bytes, "jpg", null);
            String prefix = remoteUrlArray[RANDOM.nextInt(remoteUrlArray.length)];
            url = prefix + "/" + fileIds[0] + "/" + fileIds[1];
            log.info("{}", url);

            ImageResult result = new ImageResult();
            result.setDatetime(markTime);
            result.setId(id);
            result.setGroup(fileIds[0]);
            result.setAnalysisId(serialNumber);
            result.setUrl(fileIds[1]);
            result.setType(type);
            structuringDataRepository.save(result);
        } catch (Exception e) {
            log.error("", e);
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
    public long batchDelete(String serialNumber, String time) throws ParseException {
        JSONObject taskCallback = new JSONObject();
        taskCallback.put("serialnumber", serialNumber);
        taskCallback.put("time", time);
        taskCallback.put("status", true);
        log.info("begin to delete images, analysisId: {}, time: {}", serialNumber, time);
        try {
            Date start = DateUtil.generatorDate(time, "start");
            Date end = DateUtil.generatorDate(time, "end");
            JSONObject object = generateQueryObject(serialNumber, start, end);
            //log.debug("{}", object);
            Map<String, String> header = new HashMap<>();
            header.put(HttpHeaders.CONTENT_TYPE, "application/json");
            header.put(HttpHeaders.AUTHORIZATION, auth);
            String response = HttpClientUtil
                    .post("http://" + host + ":" + port + "/image_result/_search?scroll=10m",
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
                        .post("http://" + host + ":" + port + "/_search/scroll",
                                object.toJSONString(), header);
                responseObject = JSONObject.parseObject(response);
                scroll = responseObject.getString("_scroll_id");
                hits = responseObject.getJSONObject("hits");
                resultArray = hits.getJSONArray("hits");
                deleteList(resultArray, total);
            }
            log.info("delete total {} images, analysisId: {} time: {} ", total.get(), serialNumber, time);
            JSONObject deleteObject = new JSONObject();
            deleteObject.put("scroll_id", scroll);
            HttpClientUtil
                    .delete("http://" + host + ":" + port + "/_search/scroll",
                            deleteObject.toJSONString(),
                            header);

            Map<String, String> map = new HashMap<>();
            map.put("analysisId", serialNumber);
            structuringDataRepository
                    .batchDelete(map, "datetime", start, end, ImageResult.class);
        } catch (Exception e) {
            taskCallback.put("status", false);
            log.error("", e);
        }
        feignToTask.setDeleteTaskStatus(taskCallback.toJSONString());

        return 0;
    }


    private boolean delete(String group, String url, AtomicLong total, CountDownLatch latch, AtomicBoolean isfailed) {
        TrackerServer trackerServer = null;
        int ret = 0;
        try {
            TrackerClient tracker = new TrackerClient();
            trackerServer = tracker.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            int r = storageClient.delete_file(group, url);
            if (r != 0 && r != 2 && r != 117 && r != 22) {//0为删除成功，2为找不到文件，22位找不到路径, 117 Structure needs cleaning(磁盘坏道导致，是删除不掉的，不管了)
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
//            service.submit(() -> delete(group, url, total, latch, isFailed));
            if (url.startsWith("/")) {//如果路径为/开头，则去掉
                url = url.substring(1);
            }
            String num = url.substring(1, 3);
            int index = new BigInteger(num, 16).intValue();//获取磁盘目录index（16进制），并且转为10进制
            index = index % 20;//该处理是为了兼容当图片挂载盘单机多余20块的情况
            String path = url;
//            ExecutorService service = serviceMap.get(index%10);//每个线程均匀负责磁盘的删除
//            ExecutorService service = serviceMap.get(index>>1);//每个线程负责2个磁盘的删除
//            ThreadPoolExecutor pool = poolMap.get(index%10);//每个线程均匀负责磁盘的删除
            ThreadPoolExecutor pool = poolMap.get(index);//每个线程均匀负责磁盘的删除
//            service.submit(() -> delete(group, path, total, latch, isFailed));
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