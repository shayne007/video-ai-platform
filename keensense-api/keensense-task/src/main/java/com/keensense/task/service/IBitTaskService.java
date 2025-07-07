package com.keensense.task.service;

import com.alibaba.fastjson.JSONObject;
import com.keensense.task.entity.VsdTask;

/**
 * @Description: bitman接口类
 * @Author: wujw
 * @CreateDate: 2019/5/23 15:57
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public interface IBitTaskService {

    /***
     * 启动任务
     * @param url 访问路径
     * @param vsdTask vsdTask对象
     * @return: int
     */
    int startTask(String url,VsdTask vsdTask);

    /***
     * 启动任务
     * @param url 访问路径
     * @param serialnumber 任务ID
     * @param isStream 是否是实时流对象
     * @return: int
     */
    int queryTask(String url,String serialnumber,boolean isStream);

    /***
     * 查询任务状态
     * @param url 访问路径
     * @param serialnumber 任务ID
     * @return: int
     */
    int stopTask(String url,String serialnumber);

    /***
     * 查询盒子状态
     * @param url 访问路径
     * @return: JSONObject
     */
    JSONObject queryTaskByMachine(String url);

}
