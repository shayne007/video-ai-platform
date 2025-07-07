package com.keensense.task.service;

import com.keensense.task.entity.TaskCleanLog;

import java.util.List;

/**
 * @Description: TaskCleanLog Service 接口类
 * @Author: wujw
 * @CreateDate: 2019/6/25 16:32
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public interface ITaskCleanLogService {

    /***
     * 添加数据
     * @param taskCleanLog 写入对象
     * @return: int
     */
    int insert(TaskCleanLog taskCleanLog);

    /***
     * 批量添加数据
     * @param list 写入对象
     * @return: int
     */
    boolean insertBatch(List<TaskCleanLog> list);

    /***
     * 获取要删除的数据数据
     * @param status 要查询的状态
     * @return: int
     */
    List<TaskCleanLog> getListForDelete(int status);

    /***
     * 批量修改数据
     * @param list 修改对象
     */
    void updateBatch(List<TaskCleanLog> list);
}
