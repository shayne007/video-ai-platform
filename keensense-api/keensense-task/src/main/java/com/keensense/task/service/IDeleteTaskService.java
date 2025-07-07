package com.keensense.task.service;


import com.keensense.task.entity.TbAnalysisDetail;
import com.keensense.task.entity.TbAnalysisTask;

import java.util.List;
import java.util.Map;

/**
 * @Description: 删除数据服务
 * @Author: wujw
 * @CreateDate: 2019/11/18 11:06
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public interface IDeleteTaskService {

    /***
     * 获取磁盘容量
     * @return: java.lang.Boolean
     */
    boolean getDataSpace();
    
    /***
     * 获取最早的es数据
     * @param paramMap 参数列表
     * @return: java.lang.String
     */
    String getResultOrderByCreateTime(Map<String, Object> paramMap);

    /***
     * 异步删除数据
     * @param serialnumber 任务号
     * @param time 时间
     * @param analyType 分析类型
     * @return: java.lang.boolean
     */
    boolean deleteEs(String serialnumber, String time, String analyType);

    /***
     * @description: 添加需要删除的图片记录
     * @param serialnumber 子任务号
     * @param userSerialnumber 主任务号
     * @param analyType 任务类型
     * @param ymd   时间
     * @param taskType 视频类型
     * @param optSource 操作类型
     * @return: boolean
     */
    boolean addDeleteImageRecord(String serialnumber, String userSerialnumber, String analyType, String ymd, int taskType, int optSource);

    /***
     * 异步删除数据
     * @param serialnumber 任务号
     * @param time 时间
     * @param analyType 分析类型
     * @return: java.lang.boolean
     */
    boolean deleteImages(String serialnumber, String time, String analyType);
}
