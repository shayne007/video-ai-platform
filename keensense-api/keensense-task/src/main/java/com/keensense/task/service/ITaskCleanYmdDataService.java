package com.keensense.task.service;

import com.keensense.task.entity.TaskCleanYmdData;
import com.keensense.task.entity.TbAnalysisTask;

import java.util.List;

/**
 * @Description: TaskCleanYmdData Service 接口类
 * @Author: wujw
 * @CreateDate: 2019/6/25 16:32
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public interface ITaskCleanYmdDataService {

    /***
     * 批量添加数据
     * @param list 写入对象
     * @return: int
     */
    boolean insertBatch(List<TaskCleanYmdData> list);

    /***
     * 获取要删除的数据
     * @return: int
     */
    List<TaskCleanYmdData> getListForDelete();

    /***
     * @description: 根据任务号和时间查询
     * @param list 任务列表
     * @param yyyyMMdd 时间
     * @return: java.util.List<com.keensense.task.entity.TaskCleanYmdData>
     */
    List<TaskCleanYmdData> getTaskCleanYmdDataList(List<TbAnalysisTask> list, String yyyyMMdd);

    /***
     * 批量修改数据
     * @param list 修改对象
     */
    void updateBatch(List<TaskCleanYmdData> list);
}
