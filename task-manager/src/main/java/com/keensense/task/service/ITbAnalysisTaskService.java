package com.keensense.task.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.task.entity.SearchTaskVO;
import com.keensense.task.entity.TaskVO;
import com.keensense.task.entity.TbAnalysisTask;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jobob
 * @since 2019-05-06
 */
public interface ITbAnalysisTaskService extends IService<TbAnalysisTask> {

    /***
     * 根据ID查询任务对象
     * @param id ID
     * @return: com.keensense.task.entity.TbAnalysisTask
     */
    TbAnalysisTask getTbAnalysisTaskById(String id);

    /***
     * 添加任务
     * @param paramJson 请求参数
     * @param serialnumber 任务ID
     * @param type 任务类型
     * @param url  请求路径
     */
    void insertTask(JSONObject paramJson, String serialnumber, String type, String url);

    /**
     *
     * @param paramJson param参数
     * @param serialnumber  任务号
     * @param type 任务类型  objext/summary
     * @param url 请求路径
     * @param videoType 视频类型   1-实时流/2-离线视频/3-录像任务/4-浓缩视频
     * #@param priority   任务优先级
     * #@param analysisTypes  分析类型  1-人体/2-车辆/3-人脸/4-骑行
     */
    void addTask(JSONObject paramJson, String serialnumber, String type, String url, Integer videoType);

    /***
     * 停止任务
     * @param tbAnalysisTask 任务对象
     */
    void stopTask(TbAnalysisTask tbAnalysisTask);

    /***
     * 继续任务
     * @param tbAnalysisTask 任务对象
     */
    void continueTask(TbAnalysisTask tbAnalysisTask);

    /***
     * 删除任务
     * @param tbAnalysisTask 任务对象
     * @param optSource 操作类型 1-主动 2-自动
     */
    void deleteTask(TbAnalysisTask tbAnalysisTask, int optSource);

    /***
     * 停止任务
     * @param ids 任务对象
     */
    void stopBatchTask(String[] ids);

    /***
     * 批量删除任务,不支持录像任务批量删除
     * @param ids 任务id
     */
    void deleteBatchTask(String[] ids);

    /***
     * 批量重启任务,不支持抓拍机任务批量重启
     * @param ids 任务id
     */
    void continueBatchTask(String[] ids);

    /***
     * 获取任务列表
     * @param paramObject 请求参数
     * @param page  分页对象
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     */
    JSONObject getTaskList(JSONObject paramObject, Page<TaskVO> page);

    /***
     * 获取视频录像任务列表
     * @param paramObject 请求参数
     * @param page  分页对象
     * @return: JSONObject
     */
    JSONObject getVideoTaskList(JSONObject paramObject, Page<TbAnalysisTask> page);

    /***
     * 获取搜图参数
     * @param paramObject 请求参数
     * @return: JSONObject
     */
    Set<SearchTaskVO> getTaskForSearch(JSONObject paramObject);

    /***
     * 根据条件查询列表
     * @return: JSONObject
     */
    List<TbAnalysisTask> selectList(QueryWrapper<TbAnalysisTask> wrapper);

    /***
     * 获取最早的离线任务
     * @return: JSONObject
     */
    TbAnalysisTask selectEarliestOfficeTask();

    /***
     * 查询需要清理的非实时任务
     * @param endTime 截止时间
     * @return: JSONObject
     */
    List<TbAnalysisTask> selectOfficeForDelete(Timestamp endTime);

    /***
     * 重启任务
     * @param tbAnalysisTask 任务对象
     * @return: void
     */
    void retryTask(TbAnalysisTask tbAnalysisTask);

    /**
     * 根据分析打点数据更新录像分析进度
     */
    void updateProgressByTracks();

    /**
     * 查询录像的分析进度
     * @param paramJson
     * @return
     */
    int getVideoTaskProgress(JSONObject paramJson);

    /**
     * 根据任务插清理表
     * @param tbAnalysisTask
     * @return
     */
    int insertTaskCleanLog(TbAnalysisTask tbAnalysisTask,int optSource);
}
