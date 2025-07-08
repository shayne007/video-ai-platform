package com.keensense.picturestream.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.keensense.picturestream.util.VideoExceptionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: Controller父类，主要提供一些Controller的公用方法
 * @Author: wujw
 * @CreateDate: 2019/5/14 9:33
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class BaseController {

    /****
     * @description: 解析BODY参数成JSON格式
     * @param body 请求参数Body体
     * @return: com.alibaba.fastjson.JSONObject
     */
    JSONObject getJsonByBody(String body) {
        try {
            return JSON.parseObject(body);
        } catch (JSONException e) {
            log.error("parsing JSON failed",e);
            throw VideoExceptionUtil.getValidException("请求参数不是JSON格式");
        }
    }

}
