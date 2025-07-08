package com.keensense.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.exception.VideoException;
import com.keensense.search.domain.FaceResult;
import com.keensense.search.domain.NonMotorVehiclesResult;
import com.keensense.search.domain.PersonResult;
import com.keensense.search.domain.Result;
import com.keensense.search.domain.SummaryResult;
import com.keensense.search.domain.VlprResult;
import com.keensense.search.repository.FeatureRepository;
import com.keensense.search.repository.ImageRepository;
import com.keensense.search.utils.DateUtil;
import com.keensense.search.utils.ResponseUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Created by zhanx xiaohui on 2019-05-08.
 */
@Service
@Slf4j
@RefreshScope
public class SummaryServiceImpl extends DataServiceImpl {

    private static final String SUMMARY_OBJECT = "SummaryObject";

    @Resource(name = "${image.repository}")
    private ImageRepository imageRepository;
    @Resource(name = "${feature.repository}")
    private FeatureRepository featureRepository;
    @Value("${send.to.feature.save}")
    private String featureSave;

    private static final String SUCCESS = "Success";

//    private static ExecutorService service = Executors.newSingleThreadExecutor();

    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    /**
     * 批量写入浓缩数据
     *
     * @param jsonObject 外部的请求json
     * @return 响应json
     */
    @Override
    public JSONObject batchInsert(JSONObject jsonObject) {
        //log.debug("begin to batch insert face");
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String url = request.getRequestURL().toString();
        JSONArray summaryListObject = getSummaryObejectList(jsonObject);
        JSONObject responseObject = new JSONObject();
        JSONArray responseStatusArray = new JSONArray();
        String id = "";
        JSONObject summaryObject = null;
        //循环处理请求中的多个人脸对象
        for (int i = 0; i < summaryListObject.size(); i++) {
            try {
                summaryObject = summaryListObject.getJSONObject(i);
                id = summaryObject.getString("ID");
                //将json转换为可存储到es的对象
                SummaryResult result = convertJsonToResult(summaryObject);
                //log.debug("convert json to result is {}", result);
                responseStatusArray.add(ResponseUtil.createSuccessResponse(id, url));
            } catch (Exception e) {
                log.error("handler request failed,the request is {}", summaryObject, e);
                responseStatusArray.add(ResponseUtil
                        .createFailedResponse(id, url, ResponseUtil.STATUS_CODE_FAILED,
                                e.getMessage()));
            }
        }
        responseObject.put("ResponseStatusObject", responseStatusArray);
        JSONObject response = new JSONObject();
        response.put("ResponseStatusListObject", responseObject);
        //log.debug("end to batch insert face, the response is {}", responseObject.toJSONString());
        return responseObject;
    }

    /**
     * 查询单个浓缩数据
     *
     * @param id 浓缩id
     * @return 单个浓缩数据
     */
    @Override
    public String query(String id) {
        //log.debug("begin to query person");
        JSONObject jsonObject = new JSONObject();
        List<SummaryResult> list = structuringDataRepository
                .query("id", id, SummaryResult.class);
        if (null != list && !list.isEmpty()) {
            jsonObject.put(SUMMARY_OBJECT, list.get(0));
            //log.debug("end to query summary, the response is {}", jsonObject);
            return jsonObject.toJSONString();
        }
        return jsonObject.toJSONString();
    }

    /**
     * 批量查询浓缩
     *
     * @param parameterMap 查询条件
     * @return 查询结果
     */
    @Override
    public String batchQuery(Map<String, String[]> parameterMap) {
        //log.debug("begin to batch query person");
        Map<String, String> map = jsonConvertUtil
                .covertInputParameterToResultParameter(parameterMap);

        JSONObject responseObject = new JSONObject();
        JSONObject summaryObject = new JSONObject();
        try {
            Map<Integer, List<SummaryResult>> resultMap = structuringDataRepository
                    .batchQuery(map, SummaryResult.class);
            Entry<Integer, List<SummaryResult>> entry = resultMap.entrySet().iterator().next();
            int count = entry.getKey();
            List<SummaryResult> summaryResults = entry.getValue();

            summaryObject.put(SUMMARY_OBJECT, summaryResults);
            summaryObject.put("Count", count);
        } catch (Exception e) {
            log.error(
                    "query failed.", e);
            responseObject.put("Count", 0);
            summaryObject.put(SUMMARY_OBJECT, new JSONArray());
        }
        //log.debug("end to batch query summary, the response is {}", responseObject.toJSONString());

        responseObject.put("SummaryListObject", summaryObject);
        return responseObject.toJSONString();
    }

    /**
     * 以图搜图，浓缩设备没有这个功能，空实现
     *
     * @param jsonObject 搜图的请求json
     * @return 搜图的结果
     */
    @Override
    public String search(JSONObject jsonObject) {
        return null;
    }

    public String getAllResultList(JSONObject jsonObject) {
        String serialnumber = jsonObject.getString("serialnumber");
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        String pageNo = jsonObject.getString("pageNo");
        String pageSize = jsonObject.getString("pageSize");
        Map<String, String> paraMap = new HashMap<>();
        if (!StringUtils.isEmpty(serialnumber)) {
            paraMap.put("Summary.Serialnumber.In", serialnumber);
        }
        paraMap.put("Summary.PageRecordNum", StringUtils.isEmpty(pageSize) ? "16" : pageSize);
        paraMap.put("Summary.RecordStartNo", StringUtils.isEmpty(pageNo) ? "1" : pageNo);
        if (!StringUtils.isEmpty(startTime)) {
            paraMap.put("Summary.CreateTime.Gte", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            paraMap.put("Summary.CreateTime.Lte", endTime);
        }

        Map<Integer, List<SummaryResult>> resultMap = structuringDataRepository
                .batchQuery(paraMap, SummaryResult.class);
        Entry<Integer, List<SummaryResult>> entry = resultMap.entrySet().iterator().next();
        int count = entry.getKey();
        List<SummaryResult> summaryResults = entry.getValue();

        JSONObject responseObject = new JSONObject();
        responseObject.put("ret", 0);
        responseObject.put("desc", SUCCESS);
        responseObject.put("totalcount", count);
        responseObject.put("resultList", summaryResults);

        return responseObject.toJSONString();
    }

    /**
     * 将json对象转换为es存储对象
     */
    public SummaryResult convertJsonToResult(JSONObject summaryObject) {
        //log.debug("begin to covert person json");
        SummaryResult summaryResult = getPrivate(summaryObject);

        String key = summaryResult.getSerialnumber() + "_" + summaryResult.getId();
        String resultString = generatorQueryResponse(summaryResult)
                .toJSONString();
        alarmRepository.insert(key, resultString);
        structuringDataRepository.save(summaryResult);
        //log.debug("end to covert person json, the result is {}", summaryResult);
        return summaryResult;
    }

    public String singleDelete(Map<String, String[]> parameterMap) {
        featureRepository.init();
        Map<String, String> map = jsonConvertUtil
                .covertInputParameterToResultParameter(parameterMap);
        String id = map.get("Id");
        try {
            Result deleteResult = getImageSearchIp(id, Result.class);
            Class tclass = tranferTypeToClass(deleteResult.getObjType());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String startTime = format.format(DateUtil.addDay(deleteResult.getMarkTime(), -1));
            String endTime = format.format(DateUtil.addDay(deleteResult.getMarkTime(), 1));
            String serialnumber = deleteResult.getSerialnumber();
            if (!StringUtils.isEmpty(deleteResult.getAnalysisId())) {
                serialnumber = deleteResult.getAnalysisId();
            }
            boolean deleted = !JviaFeatureSearch.deleteFeature(deleteResult.getIp(), serialnumber,
                    deleteResult.getObjType(), deleteResult.getId(), startTime, endTime, 20);
            if (deleted) {
                throw new VideoException("batchDeleteData feature failed");
            }
            if (-1 == structuringDataRepository.delete("id", id, tclass)) {
                throw new VideoException("batchDeleteData ES failed");
            }
            //batchDeleteData
        } catch (Exception e) {
            log.error("", e);
            return ResponseUtil.generatorDeleteResposnse("-1", "Failed", e.getMessage());
        }
        return ResponseUtil.generatorDeleteResposnse("0", SUCCESS, null);
    }

    public String batchDeleteData(String serialnumber, String time) {
        log.info("begin to remove data, serialnumer is {}, time is {}", serialnumber, time);
        try {
            if (StringUtils.isEmpty(serialnumber) && StringUtils.isEmpty(time)) {
                throw new VideoException("the parameter is empty");
            }
            //入库时，特征数据是否入搜图模块，默认true，发送搜图模块；如果没有存特征，则也不删除特征
            if (Boolean.valueOf(featureSave)) {
                String featureTime = formatTimeToFeaturePattern(time);
                if (!batchDeleteFeature(serialnumber,
                        featureTime)) {
                    throw new VideoException("batchDeleteData feature failed");
                }
            }

            Map<String, String> map = new HashMap<>();
            map.put("analysisId", serialnumber);
            Date start = DateUtil.generatorDate(time, "start");
            Date end = DateUtil.generatorDate(time, "end");
            Date initDt = new SimpleDateFormat("yyyyMMddHHmmss").parse("19700101080001");
            ;
            ;//临界时间
            //为了防止时间越界导致es无法正常处理，对越界时间进行处理
            if (initDt.after(start)) {//越界了
                start = initDt;
            }
            if (initDt.after(start)) {//越界了
                end = initDt;
            }
            long esNum = structuringDataRepository
                    .batchDelete(map, "markTime",
                            start,
                            end, Result.class);
            if (esNum == -1) {
                log.info("batchDeleteData es data failed, retry it, serialnumer is {}, time is {}", serialnumber, time);
                esNum = structuringDataRepository
                        .batchDelete(map, "markTime",
                                start,
                                end, Result.class);
                if (esNum == -1) {
                    throw new VideoException("Please try again, batchDeleteData es data failed, serialnumer is " + serialnumber + ", time is " + time);
                }
            }
        } catch (Exception e) {
            log.error("", e);
            return ResponseUtil.generatorDeleteResposnse("-1", "Failed", e.getMessage());
        }
        return ResponseUtil.generatorDeleteResposnse("0", SUCCESS, null);
    }

    public String batchDeleteImage(String serialnumber, String time) {
        try {
            if (StringUtils.isEmpty(serialnumber) && StringUtils.isEmpty(time)) {
                throw new VideoException("the parameter is empty");
            }

//            service.submit(()->imageRepository.batchDelete(serialnumber, time));
            pool.execute(() -> {
                try {
                    imageRepository.batchDelete(serialnumber, time);
                } catch (ParseException e) {
                    log.error("", e);
                }
            });
        } catch (Exception e) {
            log.error("", e);
            return ResponseUtil.generatorDeleteResposnse("-1", "Failed", e.getMessage());
        }
        return ResponseUtil.generatorDeleteResposnse("0", SUCCESS, null);
    }

    @Scheduled(cron = "0 30 6 * * ?")////每天凌晨6点半执行清理队列任务,后续的任务不在执行了
    private void clearPoolQueue() {
        log.error("clear pool queue at {} ", System.currentTimeMillis());
        if (pool.getQueue().size() > 0) {
            log.error("pool queue size {}", pool.getQueue().size());
            pool.getQueue().clear();
        }

    }

    private SummaryResult getPrivate(JSONObject object) {
        return JSONObject.toJavaObject(object, SummaryResult.class);
    }

    private JSONArray getSummaryObejectList(JSONObject inputObject) {
        JSONObject summaryObject = inputObject.getJSONObject("SummaryListObject");
        return summaryObject.getJSONArray(SUMMARY_OBJECT);
    }

    private JSONObject generatorQueryResponse(SummaryResult result) {
        JSONObject responseObject = new JSONObject();
        JSONObject object = JSONObject.parseObject(JSON.toJSONString(result));
        responseObject.put(SUMMARY_OBJECT, object);

        return responseObject;
    }

    public String unitBatchQuery(Map<String, String[]> parameterMap) {
        return batchQuery(parameterMap, "UnitListObject", "UnitObject",
                Result.class);
    }

    private Class tranferTypeToClass(Integer type) {
        switch (type) {
            case 1:
                return PersonResult.class;
            case 2:
                return VlprResult.class;
            case 3:
                return FaceResult.class;
            case 4:
                return NonMotorVehiclesResult.class;
            default:
                throw new VideoException("type is wrong");
        }
    }

    private Result getImageSearchIp(String id, Class tclass) {
        List<Result> objectList = structuringDataRepository.query("id", id, tclass);
        if (null != objectList && !objectList.isEmpty()) {
            return objectList.get(0);
        }
        throw new VideoException();
    }


    private boolean batchDeleteFeature(String serialnumber, String time) {
        featureRepository.init();
        if (StringUtils.isEmpty(time)) {
            return JviaFeatureSearch.deleteOfflineFeatures(serialnumber, 20);
        } else {
            return JviaFeatureSearch.deleteOnlineFeatures(serialnumber, time, 20);
        }
    }

    private String formatTimeToFeaturePattern(String time) {
        if (StringUtils.isEmpty(time)) {
            return time;
        }
        if (time.length() < 8) {
            throw new VideoException("the time pattern is wrong");
        }
        String year = time.substring(0, 4);
        String month = time.substring(4, 6);
        String day = time.substring(6, 8);

        return year + "-" + month + "-" + day;
    }


}

/**
 * @program: data
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-05-08 09:23
 **/