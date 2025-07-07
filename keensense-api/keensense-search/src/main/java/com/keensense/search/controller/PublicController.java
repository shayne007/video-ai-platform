package com.keensense.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.service.impl.SummaryServiceImpl;
import com.keensense.search.service.search.ImageSearchService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhanx xiaohui on 2019-03-14.
 */
@RestController
@Slf4j
public class PublicController {
    private static final String REQUEST_URL = "RequestURL";
    private static final String RERQUEST_BODY_IS_WRONG = "the request body is wrong";
    private static final String DEVICEID = "DeviceID";
    private static final String STATUS_CODE = "StatusCode";
    private static final String STATUS_STRING = "StatusString";
    private static final String LOCAL_TIME = "LocalTime";
    private static final String TIME_PATTERN = "yyyyMMddHHmmss";
    private static final String RESPONSE_STATUS_OBJECT = "ResponseStatusObject";

    @Autowired
    SummaryServiceImpl summaryService;
    
    @Autowired
    ImageSearchService imageSearchService;

    @PostMapping(value = "/serviceHeartbeat", produces = "application/json;charset=UTF-8")
    public String serviceHeartbeat() {
        return "{\"msg\":\"OK\"}";
    }


    @PostMapping(value = "/VIID/System/Register", produces = "application/json;charset=UTF-8")
    public String register(@RequestBody JSONObject json) {
        JSONObject registerObject = json.getJSONObject("RegisterObject");
        if(registerObject == null){
            return RERQUEST_BODY_IS_WRONG;
        }
        String deviceId = registerObject.getString(DEVICEID);
        JSONObject response = new JSONObject();
        JSONObject statusObject = new JSONObject();
        statusObject.put(REQUEST_URL, "http://localhost:8080/VIID/Register");
        statusObject.put(STATUS_CODE, "0");
        statusObject.put(STATUS_STRING, "正常");
        statusObject.put("Id", deviceId);
        statusObject.put(LOCAL_TIME, new SimpleDateFormat(TIME_PATTERN).format(new Date()));
        response.put(RESPONSE_STATUS_OBJECT, statusObject);
        return response.toJSONString();
    }

    @PostMapping(value = "/VIID/System/UnRegister", produces = "application/json;charset=UTF-8")
    public String unregister(@RequestBody JSONObject json) {
        JSONObject unregisterObject = json.getJSONObject("UnRegisterObject");
        if(unregisterObject == null){
            return RERQUEST_BODY_IS_WRONG;
        }
        String deviceId = unregisterObject.getString(DEVICEID);
        JSONObject response = new JSONObject();
        JSONObject statusObject = new JSONObject();
        statusObject.put(REQUEST_URL, "http://localhost:8080/VIID/UnRegister");
        statusObject.put(STATUS_CODE, "0");
        statusObject.put(STATUS_STRING, "正常");
        statusObject.put("Id", deviceId);
        statusObject.put(LOCAL_TIME, new SimpleDateFormat(TIME_PATTERN).format(new Date()));
        response.put(RESPONSE_STATUS_OBJECT, statusObject);
        return response.toJSONString();
    }

    @PostMapping(value = "/VIID/System/Keepalive", produces = "application/json;charset=UTF-8")
    public String keepalive(@RequestBody JSONObject json) {
        JSONObject keepaliveObject = json.getJSONObject("KeepaliveObject");
        if(keepaliveObject == null){
            return RERQUEST_BODY_IS_WRONG;
        }
        String deviceId = keepaliveObject.getString(DEVICEID);
        JSONObject response = new JSONObject();
        JSONObject statusObject = new JSONObject();
        statusObject.put(REQUEST_URL, "http://localhost:8080/VIID/Keepalive");
        statusObject.put(STATUS_CODE, "0");
        statusObject.put(STATUS_STRING, "正常");
        statusObject.put("Id", deviceId);
        statusObject.put(LOCAL_TIME, new SimpleDateFormat(TIME_PATTERN).format(new Date()));
        response.put(RESPONSE_STATUS_OBJECT, statusObject);
        return response.toJSONString();
    }

    @GetMapping(value = "/VIID/System/Time", produces = "application/json;charset=UTF-8")
    public String time() {
        JSONObject response = new JSONObject();
        JSONObject statusObject = new JSONObject();
        statusObject.put("TimeMode", "1");
        statusObject.put("TimeZone", TimeZone.getDefault());
        statusObject.put("VIIDServerID", "50010800055038000003");
        statusObject.put(LOCAL_TIME, new SimpleDateFormat(TIME_PATTERN).format(new Date()));
        response.put("SystemTimeObject", statusObject);
        return response.toJSONString();
    }

    @GetMapping(value = "/VIID/Result", produces = "application/json;charset=UTF-8")
    public String batchQuery(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        return summaryService.unitBatchQuery(parameterMap);
    }

    @DeleteMapping(value = "/VIID/Result/Single/Delete", produces = "application/json;charset=UTF-8")
    public String singleDelete(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        return summaryService.singleDelete(parameterMap);
    }

    @DeleteMapping(value = "/VIID/Result/Delete/Data", produces = "application/json;charset=UTF-8")
    public String batchDelete(@RequestParam("Serialnumber") String serialnumber,
        @RequestParam(value = "Time", required = false) String time){
        return summaryService.batchDeleteData(serialnumber, time);
    }

    @DeleteMapping(value = "/VIID/Result/Delete/Image", produces = "application/json;charset=UTF-8")
    public String batchDeleteAsync(@RequestParam("Serialnumber") String serialnumber,
        @RequestParam(value = "Time", required = false) String time){
        return summaryService.batchDeleteImage(serialnumber, time);
    }
    
    @PostMapping(value = "/VIID/Feature/Dump", produces = "application/json;charset=UTF-8")
    public String featureDump(@RequestBody JSONObject json) {
        return imageSearchService.featureDump(json);
    }

    @GetMapping(value = "/VIID/System/status", produces = "application/json;charset=UTF-8")
    public String status() {
        JSONObject object = new JSONObject();
        object.put("code", 0);
        object.put("msg", "OK");
        return object.toJSONString();
    }

}

/**
 * @program: data
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-03-14 17:23
 **/