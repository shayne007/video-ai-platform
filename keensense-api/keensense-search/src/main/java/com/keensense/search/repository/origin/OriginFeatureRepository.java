package com.keensense.search.repository.origin;

import cn.jiuling.plugin.extend.featuresearch.JviaFeatureSearch;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.exception.VideoException;
import com.keensense.search.domain.Result;
import com.keensense.search.feign.FeignToArchive;
import com.keensense.search.repository.FeatureRepository;
import com.keensense.search.schedule.ImageServiceClusterIpScheduled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhanx xiaohui on 2019-02-27.
 */
@Repository("originFeatureRepository")
@Slf4j
@RefreshScope
public class OriginFeatureRepository implements FeatureRepository {

    @Value("${origin.kafka.feature.brokerList}")
    private String brokerList;
    @Value("${origin.kafka.feature.topic}")
    private String topic;
    @Value("${face.faceClassPath}")
    private String algorithmType;
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;
    private static final int EXPIRE_SECOND = 3600;

    @Autowired
    private ImageServiceClusterIpScheduled imageServiceClusterIpScheduled;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FeignToArchive feignToArchive;

    /**
     * 初始化kafka的连接
     */
    @Override
    public synchronized boolean init() {
        JviaFeatureSearch.initInstance(redisHost, redisPort, brokerList, EXPIRE_SECOND);
        return true;
    }

    /**
     * 将请求发送到搜图模块
     *
     * @param result 由json转换出的bean
     * @return 返回是否发送成功
     */
    @Override
    public int sendFeature(Result result) {
        //rest api方式录入特征到搜图模块
        String ip = imageServiceClusterIpScheduled.getFeatureIp();
        if (StringUtils.isEmpty(ip)) {
            throw new VideoException("Save feature failed : Can't find active feature search ip");
        }
        result.setIp(ip);
        JSONObject feature = generatorFeatureJsonRestApi(result);
        String url = "http://" + ip + ":39081/addobject";
        int ret = -1;
        String msg = "";
        try {
            String res = restTemplate.postForEntity(url, feature, String.class).getBody();
//            feature = null;
            JSONObject json = JSONObject.parseObject(res);
            ret = json.getInteger("error_code");
            msg = json.getString("error_msg");
        } catch (Exception e) {
            log.error(feature.toJSONString());
            log.error("Save feature failed.", e);
            throw new VideoException("Save feature failed : " + e.getMessage());
        }
        if (ret != 0) {
            log.error(feature.toJSONString());
            throw new VideoException("Save feature failed the msg is : " + msg);
        }
        return ret;
    }

    /**
     * 现有流程中需要将特征信息发送到搜图模块，以便后面进行以图搜图的时候使用，这里需要构造发送到搜图模块的请求
     * rest api模式
     *
     * @param result 由json转换出的bean
     * @return 构造好的请求json对象
     */
    JSONObject generatorFeatureJsonRestApi(Result result) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject feature = new JSONObject();

        String serialnumber = result.getSerialnumber();
        if (!StringUtils.isEmpty(result.getAnalysisId())) {
            serialnumber = result.getAnalysisId();
        }
        feature.put("task", serialnumber);
        feature.put("first_object", 0);
        feature.put("timestamp", result.getStartFramePts() == null ? result.getMarkTime().getTime() : result.getStartFramePts());
        feature.put("uuid", result.getId());
        feature.put("type", result.getObjType());
        feature.put("only_enroll_type", 1);
        feature.put("feature", result.getFeatureObject());
        if (result.getFirm() != null && result.getFirm() != 0) {
            feature.put("firm", result.getFirm());
        }
        return feature;
    }

    /**
     * 现有流程中需要将特征信息发送到搜图模块，以便后面进行以图搜图的时候使用，这里需要构造发送到搜图模块的请求
     *
     * @param result 由json转换出的bean
     * @return 构造好的请求
     */
    JSONObject generatorFeatureJson(Result result) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject feature = new JSONObject();
        feature.put("uuid", result.getId());
        feature.put("objType", result.getObjType());
        String serialnumber = result.getSerialnumber();
        if (!StringUtils.isEmpty(result.getAnalysisId())) {
            serialnumber = result.getAnalysisId();
        }
        feature.put("serialNumber", serialnumber);
        feature.put("createTime",
                format.format(result.getMarkTime() == null ? new Date() : result.getMarkTime()));
        feature.put("firstObj", 0);
        feature.put("timestamp", result.getStartFramePts() == null ? result.getMarkTime() : result.getStartFramePts());
        feature.put("startFramePts",
                result.getStartFramePts() == null ? result.getMarkTime() : result.getStartFramePts() - 1);
        feature.put("onlyEnrollTypeFeature", 1);
        if (result.getFirm() != null && result.getFirm() != 0) {
            feature.put("firm", result.getFirm());
        }
        JSONObject featuredata = new JSONObject();
        featuredata.put("featureData", result.getFeatureObject());
        feature.put("features", featuredata);
        return feature;
    }
}