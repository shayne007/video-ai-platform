package com.keensense.common.util;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 通用返回信息
 * @author luowei
 * @date 2019年5月6日
 * @description
 */
public class ResultUtils {
	
	   /**
     * 向页面返回一个错误JSON信息。
     * @param status  状态码
     * @param msg 消息
     * @param data 返回对象
     */
    public static String renderFailure(String status, String msg, Object data) {
        ResultBean res = new ResultBean();
        res.setRet(status);
        res.setDesc(msg);
        res.setData(data);
        return getResultJson(res);
    }

    /**
     * 向页面返回一个错误JSON信息。
     * @param status  状态码
     * @param msg   消息
     */
    public static String renderFailure(String status, String msg) {
        return renderFailure(status, msg, null);
    }

    /**
     * 向页面返回一个错误JSON信息。
     * @param data 返回对象
     */
    public static String renderFailure(Object data) {
        return renderFailure("-1","操作失败，请重试！",data);
    }

    /**
     * 返回一个成功对象。
     * @param data 返回对象
     */
    public static String renderSuccess(Object data) {
        return renderSuccess("success", data);
    }

    /**
     * 返回一个成功对象。
     * @param msg 返回消息
     * @param data 返回对象
     */
    public static String renderSuccess(String msg, Object data) {
        ResultBean res = new ResultBean();
        res.setRet("0");
        res.setDesc(msg);
        res.setData(data);
        return getResultJson(res);
    }

    /**
     * 解决Json的key第一个字符需要大写
     * 返回status list
     *
     * @param messList
     * @return
     */
    public static ResponseStatusTotal returnStatusList(List<ResponseStatus> messList) {
        ResponseStatusList statusList = new ResponseStatusList();
        statusList.setResponseStatus(messList);
        ResponseStatusTotal total = new ResponseStatusTotal();
        total.setResponseStatusList(statusList);
        return total;
    }

    /**
     * 返回status
     *
     * @param mess
     * @return
     * @throws Exception
     */
    public static ResponseStatusList returnStatus(ResponseStatus mess) {
        ResponseStatusList statusList = new ResponseStatusList();
        statusList.setResponseStatus(mess);
        return statusList;
    }
    
    /***
     * @description: 同一格式化时间格式
     * @param resultBean 返回对象
     * @return: java.lang.String
     */
    private static String getResultJson(Object resultBean) {
        return JSON.toJSONStringWithDateFormat(resultBean, "yyyy-MM-dd HH:mm:ss");
    }
	
	/**
	 * 返回一个指定的object对象或者list的json字符串
	 * @param object
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public static JSONObject returnObject(String object ,Object o) throws Exception {
		String ss = "{\""+ object + "\":" + JSON.toJSONString(o) +"}";
		return JSON.parseObject(ss);
	}
	
	/**
	 * 返回一个task指定的json
	 * @param jsonObject
	 * @return
	 */
	public static String returnSuccess(JSONObject jsonObject) {
		jsonObject.put("ret", "0");
		jsonObject.put("desc", "success");
		return getResultJson(jsonObject);
	}

	
}
