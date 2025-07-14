package com.keensense.task.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.task.util.TaskParamValidUtil;
import com.keensense.task.util.VideoExceptionUtil;
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

    /**
     * 解析BODY参数成JSON格式
     * @param body 请求参数Body体
     * @param pathURL 请求的url
     * @return
     */
    JSONObject getJsonByBody(String body,String pathURL) {
        log.info(">>>>>> request URL:{},request body:{}",pathURL,body);
        try {
            return JSON.parseObject(body);
        } catch (JSONException e) {
            log.error("parsing JSON failed",e);
            throw VideoExceptionUtil.getValidException("请求参数不是JSON格式");
        }
    }

    /***
     * @description: 获取分页对象
     * @param paramObject 请求参数
     * @param defaultNum  默认页数
     * @param defaultSize 默认每页条数
     * @return: com.baomidou.mybatisplus.extension.plugins.pagination.Page
     */
    <T> Page<T> getPage(JSONObject paramObject, int defaultNum, int defaultSize) {
        int pageNo = TaskParamValidUtil.isPositiveInteger(paramObject, "pageNo", defaultNum,1, Integer.MAX_VALUE);
        int pageSize = TaskParamValidUtil.isPositiveInteger(paramObject, "pageSize", defaultSize,1, Integer.MAX_VALUE);
        return new Page<>(pageNo, pageSize);
    }

}
