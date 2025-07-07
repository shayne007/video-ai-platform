package com.keensense.task.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.task.constants.BitCommonConst;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.VsdTask;
import com.keensense.task.mapper.VsdTaskMapper;
import com.keensense.task.service.IBitTaskService;
import com.loocme.sys.exception.HttpConnectionException;
import com.loocme.sys.util.PostUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/5/23 16:11
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
@Service
public class BitTaskServiceImpl implements IBitTaskService {

    @Autowired
    private VsdTaskMapper vsdTaskMapper;

    @Override
    public int startTask(String url, VsdTask vsdTask) {
        url = url + BitCommonConst.BIT_START_TASK;
        JSONObject paramJson = JSON.parseObject(vsdTask.getParam());
        setSerialnumber(paramJson, vsdTask.getSerialnumber());
        try {
            JSONObject resultJson = JSON.parseObject(postRequest(url, paramJson.toString()));
            if (this.isSuccess(resultJson)) {
                return BitCommonConst.SUCCESS;
            } else {
                return BitCommonConst.FAIL;
            }
        } catch (HttpConnectionException e) {
            log.error("vsdTask serialNumber = " + vsdTask.getSerialnumber() + " start request url = " + url + " error!");
            log.error("http connect start task failed", e);
            return BitCommonConst.ERROR;
        }
    }

    @Override
    public int queryTask(String url, String serialnumber, boolean isStream) {
        url = url + BitCommonConst.BIT_QUERY_TASK;
        try {
            JSONObject paramJson = getParamJsonObject(serialnumber);
            JSONObject resultJson = JSONObject.parseObject(postRequest(url, paramJson.toString()));
            if (this.isSuccess(resultJson)) {
                JSONArray taskList = Optional.ofNullable(getStatus(resultJson))
                        .map(p -> p.getJSONArray("tasks")).orElse(null);
                JSONObject task = getTaskBySerialnumber(taskList, serialnumber);
                if (task != null) {
                    if (isSuccess(task)) {
                        return updateProgress(task, serialnumber, isStream);
                    } else {
                        return BitCommonConst.FAIL;
                    }
                }
            }
            return BitCommonConst.NOT_FOUND;
        } catch (HttpConnectionException e) {
            log.error("serialnumber  = " + serialnumber + " status request url = " + url + " error");
            log.error("http connect query task failed", e);
            return BitCommonConst.ERROR;
        }
    }

    @Override
    public int stopTask(String url, String serialnumber) {
        url = url + BitCommonConst.BIT_STOP_TASK;
        JSONObject paramJson = getParamJsonObject(serialnumber);
        try {
            //查询调通不报错即可，不然返回结果是什么都认为是成功
            postRequest(url, paramJson.toString());
        } catch (HttpConnectionException e) {
            log.error("serialnumber = " + serialnumber + " stop request url = " + url + " error");
            log.error("http connect stop task failed", e);
            return BitCommonConst.ERROR;
        }
        return BitCommonConst.SUCCESS;
    }

    /**
     * 查询盒子使用状态
     *
     * @param url 请求地址
     * @return JSONObject: status-通讯状态 useRoute-使用路数 allRoute-总路数 serialnumberList-运行的任务列表
     */
    @Override
    public JSONObject queryTaskByMachine(String url) {
        JSONObject result = new JSONObject();
        url = url + BitCommonConst.BIT_QUERY_TASK;
        String status = "status";
        try {
            JSONObject resultJson = JSON.parseObject(postRequest(url, "{}"));
            if (this.isSuccess(resultJson)) {
                JSONObject statusObj = getStatus(resultJson);
                if (statusObj != null) {
                    JSONArray taskList = statusObj.getJSONArray("tasks");
                    List<String> serialNumberList = new ArrayList<>();
                    int useRoute = 0;
                    if (taskList != null && !taskList.isEmpty()) {
                        useRoute = taskList.size();
                        for (int i = 0; i < taskList.size(); i++) {
                            serialNumberList.add(getSerialnumber(taskList.getJSONObject(i)));
                        }
                    }
                    result.put("useRoute", useRoute);
                    result.put("allRoute", useRoute + statusObj.getInteger("free"));
                    result.put("serialnumberList", serialNumberList);
                    result.put(status, BitCommonConst.SUCCESS);
                }
            }
        } catch (HttpConnectionException e) {
            log.error("http connect query machine failed", e);
            result.put(status, BitCommonConst.ERROR);
        }
        return result;
    }

    /****
     * @description: post请求
     * @param url 请求路径
     * @param params body参数
     * @return: java.lang.String
     */
    private static String postRequest(String url, String params) throws HttpConnectionException {
        return PostUtil.requestContent(url, "application/json", params);
    }

    /***
     * @description: 判断请求是否成功
     * @param resultJson 返回结果
     * @return: boolean
     */
    private boolean isSuccess(JSONObject resultJson) {
        return BitCommonConst.BIT_SUCCESS.equals(Optional.ofNullable(resultJson)
                .map(p -> p.getString("error_msg")).orElse(null));
    }

    /***
     * @description: 从集合中获取匹配的任务
     * @param taskList 任务集合
     * @param serialnumber 任务号
     * @return: com.alibaba.fastjson.JSONObject
     */
    private JSONObject getTaskBySerialnumber(JSONArray taskList, String serialnumber) {
        if (taskList != null && !taskList.isEmpty()) {
            JSONObject task;
            for (int i = 0; i < taskList.size(); i++) {
                task = taskList.getJSONObject(i);
                if (serialnumber.equals(getSerialnumber(task))) {
                    return task;
                }
            }
        }
        return null;
    }

    /***
     * @description: 更新进度
     * @param task   任务返回信息
     * @param serialnumber 任务号
     * @param isStream    是否是实时流
     * @return: int
     */
    private int updateProgress(JSONObject task, String serialnumber, boolean isStream) {
        Float progress = task.getFloat("progress");
        if (progress >= TaskConstants.PROGRESS_MAX_VALUE) {
            return BitCommonConst.COMPLETE;
        } else {
            if (!isStream) {
                vsdTaskMapper.updateProgress(serialnumber, progress.intValue());
            }
            return BitCommonConst.PROCESS;
        }
    }

    /***
     * @description: 获取查询参数仅有serialnumber的参数
     * @param serialnumber 任务号
     * @return: com.alibaba.fastjson.JSONObject
     */
    private JSONObject getParamJsonObject(String serialnumber) {
        JSONObject paramJson = new JSONObject(1);
        setSerialnumber(paramJson, serialnumber);
        return paramJson;
    }

    /***
     * @description: 设置serialnumber
     * @param paramJson 请求参数
     * @param serialnumber serialnumber
     * @return: com.alibaba.fastjson.JSONObject
     */
    private void setSerialnumber(JSONObject paramJson, String serialnumber) {
        paramJson.put("serialnumber", serialnumber);
    }

    /***
     * @description: 获取serialnumber
     * @param paramJson 返回结果
     * @return: String
     */
    private String getSerialnumber(JSONObject paramJson) {
        return paramJson.getString("serialnumber");
    }

    /***
     * @description: 获取返回状态
     * @param resultObj 返回结果
     * @return: com.alibaba.fastjson.JSONObject
     */
    private JSONObject getStatus(JSONObject resultObj) {
        return resultObj.getJSONObject("status");
    }
}
