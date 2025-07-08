package com.keensense.search.service.search.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.keensense.common.exception.VideoException;
import com.keensense.common.util.DateUtil;
import com.keensense.search.feign.FeignToArchive;
import com.keensense.search.feign.FeignToTask;
import com.keensense.search.schedule.ImageServiceClusterIpScheduled;
import com.keensense.search.service.impl.JviaFeatureSearch;
import com.keensense.search.service.search.ImageSearchService;
import com.keensense.search.utils.HttpClientUtil;
import com.keensense.search.utils.ParametercheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanx xiaohui on 2019-05-14.
 */
@Service
@Slf4j
@RefreshScope
public class ImageSearchServiceImpl implements ImageSearchService {

    @Value("${feature.search.ip}")
    private String searchImageServiceIpList;
    @Value("${feature.search.port}")
    private String searchImageServicePort;
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;
    @Value("${face.faceClassPath}")
    private String faceClassPath;
    @Autowired
    FeignToTask feignToTask;
    @Autowired
    FeignToArchive feignToArchive;
    @Autowired
    private ImageServiceClusterIpScheduled imageServiceClusterIpScheduled;

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String THRESHOLD = "Threshold";
    private static final String THRESHOLD_DOWN_CASE = "threshold";
    private static final String QUERY = "query";
    private static final String RESULTS = "results";
    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";
    private static final String LIMIT_NUM = "LimitNum";

    /**
     * 进行搜图的方法 否则发送到搜图模块去进行搜图
     *
     * @param object  外部请求的json
     * @param objType 搜图的数据类型
     * @return 结果的json
     */
    @Override
    public String search(JSONObject object, int objType) throws InterruptedException {
        JSONObject searchParam = new JSONObject();
        try {
            Map<String, String> analysisIdToSerialnumberMap = new HashedMap();
            getSearchParameters(searchParam, object, objType,
                    analysisIdToSerialnumberMap);

            return search(searchParam, analysisIdToSerialnumberMap);
        } catch (Exception e) {
            log.error("==== ImageSearchServiceImpl.search Exception:", e);
            JSONObject response = createResponse("-1", "Fail");
            response.put("message", e.getMessage());
            return response.toJSONString();
        }
    }

    @Override
    public String featureDump(JSONObject json) {
        String[] featureIps = searchImageServiceIpList.split(",");

        JSONObject response = createResponse("0", "Success");

        if (null == featureIps) {
            return createResponse("-1", "Fail").toJSONString();
        }

        log.info("==== featureDump request json:" + json.toJSONString());
        try {
            Map<String, String> headerMap = Maps.newHashMap();
            headerMap.put("Content-Type", "application/json");
            JSONArray jsonArray = new JSONArray();
            for (String ip : featureIps) {
                String featureUrl = "http://" + ip + ":" + searchImageServicePort + "/dump";
                String responseStr = HttpClientUtil.post(featureUrl, json.toJSONString(), headerMap);

                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                JSONArray features = jsonObject.getJSONArray("features");
                if (null != features) {
                    jsonArray.addAll(features);
                }
            }

            response.put("features", jsonArray);
        } catch (Exception e) {
            log.error("==== featureDump exception:", e);
            return createResponse("-1", "Fail").toJSONString();
        }

        return response.toJSONString();
    }

    /**
     * 业务层有可能不清楚serialnumber是多少，只有点位信息，需要根据点位信息去获取对应的任务号
     *
     * @param monitorId 点位信息
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 返回一个任务号的列表
     */
    private JSONArray getSerialsNumbers(String monitorId, String serialnumber, String startTime,
                                        String endTime) {
        JSONObject requestObject = new JSONObject();
        requestObject.put("cameraIds", monitorId);
        requestObject.put(START_TIME, startTime);
        requestObject.put(END_TIME, endTime);
        requestObject.put("serialnumbers", serialnumber);
        log.info("get task list request is {}", requestObject);
        String result = feignToTask.getSerialNumber(requestObject.toJSONString());
        log.info("get task list response is {}", result);
        JSONObject object = JSONObject.parseObject(result);

        return object.getJSONArray("data");
    }

    /**
     * 获取任务列表 如果携带了deviceID，那么就去根据deviceID去任务模块去查询这个设备相关的任务列表 否则就直接取json中的任务列表
     *
     * @param jsonObject 外部传入的json
     * @param deviceId   设备号
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return 任务列表
     */
    private JSONArray getTaskList(JSONObject jsonObject, String deviceId, String startTime,
                                  String endTime) {
        JSONArray taskList;
        taskList = jsonObject.getJSONArray("TaskList");
        StringBuilder serialNumbers = new StringBuilder();
        for (int i = 0; taskList != null && i < taskList.size(); i++) {
            String id = taskList.getJSONObject(i).getString("id");
            if (null != id) {
                serialNumbers.append(id);
            }
            if (i != taskList.size() - 1) {
                serialNumbers.append(",");
            }
        }
        taskList = getSerialsNumbers(deviceId, serialNumbers.toString(), startTime, endTime);

        return taskList;
    }

    /**
     * 将时间转换为对应格式，放入请求参数中
     *
     * @param searchParam 请求参数json
     * @param time        要转换的时间
     * @param timeName    时间放到请求json中的键值
     */
    private void getSearchTime(JSONObject searchParam, String time, String timeName) {
        if (StringUtils.isNotEmpty(time)) {
            Date searchStartTime = DateUtil.parseDate(time, DATE_FORMAT);
            searchParam.put(timeName, searchStartTime);
        }
    }

    /**
     * 生成发往搜图模块的请求参数的json
     *
     * @param object  外部进入的请求json
     * @param objType 数据类型
     * @return 发往搜图模块的请求参数json
     */
    private JSONObject getSearchParameters(JSONObject searchParam, JSONObject object, int objType,
                                           Map<String, String> analysisIdToSerialnumberMap) {
        searchParam.put("type", objType);
        searchParam.put("timeout_sec", 20); //设置超时时间(单位:秒)
        JSONObject jsonObject = object.getJSONObject("Search");
        String feature = jsonObject.getString("Feature");
        putFeatures(searchParam, feature);
        String startTime = jsonObject.getString("StartTime");
        String endTime = jsonObject.getString("EndTime");
        String deviceId = jsonObject.getString("DeviceId");
        int firm = getIntegerFromJson(jsonObject, "Firm", 0);
        //特征来源 第三⽅方廠家特徵代號：0 ＝千視通（預設）｜1 ＝ GLST
        searchParam.put("firm", firm);
        Float threshold = getFloatFromJson(jsonObject, THRESHOLD, 0f);
        if (threshold >= 0 && threshold <= 1) {
            searchParam.put(THRESHOLD_DOWN_CASE, threshold);
        }
        int tradeOff = getIntegerFromJson(jsonObject, "TradeOff", 50);
        if (0 < tradeOff && 100 >= tradeOff) {       //设置搜索范围
            searchParam.put("tradeoff", tradeOff);
        }
        int limitNum = getIntegerFromJson(jsonObject, LIMIT_NUM, 3000);
        searchParam.put("max_result", limitNum);

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(startTime) && !ParametercheckUtil.isTimeLegal(startTime)) {
            throw new VideoException("开始时间格式不正确,格式为[yyyy-MM-dd HH:mm:ss]");
        }

        if (StringUtils.isNotEmpty(endTime) && !ParametercheckUtil.isTimeLegal(endTime)) {
            throw new VideoException("结束时间格式不正确,格式为[yyyy-MM-dd HH:mm:ss]");
        }
        getSearchTime(searchParam, startTime, "from");
        getSearchTime(searchParam, endTime, "to");

        if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
            Date startDt = DateUtil.parse(startTime);
            Date endDt = DateUtil.parse(endTime);
            long startMs = startDt.getTime();
            long endMs = endDt.getTime();
            if (endMs < startMs) {
                throw new VideoException("结束时间不能小于开始时间");
            }
        }
        //tasklist如果请求消息中携带了MonitorId，那么需要根据MonitorId去任务管理模块查询任务列表

        JSONArray taskList = getTaskList(jsonObject, deviceId, startTime, endTime);
        for (int i = 0; i < taskList.size(); i++) {
            JSONObject taskObject = taskList.getJSONObject(i);
            String id = taskObject.getString("id");
            String taskId = taskObject.getString("taskId");
            analysisIdToSerialnumberMap.put(id, taskId);
        }
        if (null == taskList || taskList.isEmpty()) {
            throw new VideoException("查询的任务列表不存在");
        }
        searchParam.put("tasks", taskList);
        return searchParam;
    }

    /**
     * 往搜图模块发送搜图请求
     *
     * @param searchParam 请求参数
     * @return 搜图结果
     */

    private String search(JSONObject searchParam, Map<String, String> analysisIdToSerialnumberMap) {
//        JviaFeatureSearch.initInstance(redisHost, redisPort, searchImageServiceIpList);
        String featureIps = imageServiceClusterIpScheduled.getFeatureIps();
        if (StringUtils.isEmpty(featureIps)) {
            throw new VideoException("没有可用的搜图模块");
        }
        JviaFeatureSearch.initInstance(redisHost, redisPort, featureIps);
        float threshold =
                searchParam.containsKey(THRESHOLD_DOWN_CASE) ? searchParam.getFloat(THRESHOLD_DOWN_CASE)
                        : 0;
        Map<String, Object> var = JviaFeatureSearch.searchFeature(searchParam.toJSONString(), threshold, 20);
        if (!"0".equals(var.get("code"))) {
            JSONObject response = createResponse("-1", "Fail");
            response.put("message", var.get("code") + ":" + var.get("desc"));
            return response.toJSONString();
        } else {
            JSONArray resultArray = transferAnalysisIdToSerialnumber(
                    JSONObject.parseArray((String) var.get(RESULTS)), analysisIdToSerialnumberMap);
            return generatorResponse(searchParam, resultArray);
        }
    }

    private JSONArray transferAnalysisIdToSerialnumber(JSONArray jsonArray,
                                                       Map<String, String> analysisIdToSerialnumberMap) {
        if (jsonArray == null) {
            return null;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String analysisId = object.getString("task");
            String serialNumber = analysisIdToSerialnumberMap.get(analysisId);
            if (serialNumber != null) {
                object.put("task", serialNumber);
            }
        }

        return jsonArray;
    }

    /**
     * 构造返回响应json
     *
     * @param searchParam 请求参数
     * @param results     返回结果
     * @return 响应json
     */
    private String generatorResponse(JSONObject searchParam, JSONArray results) {
        JSONObject response = new JSONObject();
        if (null != searchParam.get(QUERY) && searchParam.get(QUERY) instanceof String) {
            response.put("feature", searchParam.getString(QUERY));
        } else {
            response.put("feature", searchParam.getJSONArray(QUERY));
        }

        Integer max_result = searchParam.getInteger("max_result");
        JSONArray resultsSub = new JSONArray();
        if (results != null && max_result != null && results.size() >= max_result) {
            List<Object> objects = new ArrayList<>(results.subList(0, max_result));
            for (Object obj : objects) {
                resultsSub.add(obj);
            }
        } else {
            resultsSub = results;
        }

        response.put(RESULTS, null == results ? new JSONArray() : resultsSub);
        log.info("[search] result is {}", resultsSub);
        return response.toJSONString();
    }

    /**
     * 从json中获取Float值，如果为空，则设为默认值
     *
     * @param jsonObject   请求json
     * @param key          键值
     * @param defaultValue 默认值
     * @return 获取的Float
     */
    private Float getFloatFromJson(JSONObject jsonObject, String key, Float defaultValue) {
        return jsonObject.containsKey(key) ? jsonObject.getFloat(key) : defaultValue;
    }

    /**
     * 从json中获取Integer值，如果为空，则设为默认值
     *
     * @param jsonObject   请求json
     * @param key          键值
     * @param defaultValue 默认值
     * @return 获取的Integer
     */
    private Integer getIntegerFromJson(JSONObject jsonObject, String key, Integer defaultValue) {
        return jsonObject.containsKey(key) ? jsonObject.getInteger(key) : defaultValue;
    }

    private void putFeatures(JSONObject searchParam, String feature) {
        String[] featureArray = feature.split(",");
        if (featureArray.length == 1) {
            searchParam.put(QUERY, feature);
        } else {
            searchParam.put(QUERY, featureArray);
        }
    }

    private JSONObject createResponse(String ret, String desc) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ret", ret);
        jsonObject.put("desc", desc);
        return jsonObject;
    }
}