package com.keensense.extension.util;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by memory_fu on 2019/8/27.
 */
public class ResponseUtil {
    
    /**
     * ret:     1,-1
     * desc:    success:fail
     */
    public static JSONObject createResponse(String ret, String desc) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ret", null == ret ? "1" : ret);
        jsonObject.put("desc", null == desc ? "success" : desc);
        return jsonObject;
    }
    
    public static JSONObject createResponse() {
        return createResponse(null, null);
    }
    
}
