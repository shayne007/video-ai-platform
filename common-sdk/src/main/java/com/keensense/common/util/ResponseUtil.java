package com.keensense.common.util;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ResponseUtil {
    private ResponseUtil() {
    }

    public static final long STATUS_CODE_FAILED = 7;
    public static final long STATUS_CODE_OK = 0;
    public static final String STATUS_STRING_OK = "OK";

    public static JSONObject createSuccessResponse(String id, String requestUrl) {
        JSONObject object = new JSONObject();
        object.put("Id", id);
        object.put("StatusCode", STATUS_CODE_OK);
        object.put("StatusString", STATUS_STRING_OK);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = format.format(new Date());
        object.put("LocalTime", date);
        object.put("RequestURL", requestUrl);
        return object;
    }

    public static JSONObject createFailedResponse(String id, String requestUrl, long statusCode, String statusString) {
        JSONObject object = new JSONObject();
        object.put("Id", id);
        object.put("StatusCode", statusCode);
        object.put("StatusString", statusString);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = format.format(new Date());
        object.put("LocalTime", date);
        object.put("RequestURL", requestUrl);
        return object;
    }

    public static String generatorDeleteResposnse(String errorCode, String status, String errorMessage) {
        JSONObject object = new JSONObject();
        object.put("ErrorCode", errorCode);
        object.put("Status", status);
        object.put("ErrorMessage", errorMessage);
        return object.toJSONString();
    }

    public static JSONObject genereteInsertResponse(JSONObject object) {
        JSONArray array = new JSONArray();
        array.add(object);
        return genereteInsertResponse(array);
    }

    public static JSONObject genereteInsertResponse(JSONArray array) {
        JSONObject responseObject = new JSONObject();
        responseObject.put("ResponseStatusObject", array);
        JSONObject response = new JSONObject();
        response.put("ResponseStatusListObject", responseObject);

        return response;
    }
}