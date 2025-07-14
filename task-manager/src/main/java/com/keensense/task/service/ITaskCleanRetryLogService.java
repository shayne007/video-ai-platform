package com.keensense.task.service;

import com.keensense.task.entity.TaskCleanRetryLog;

import java.util.List;

/**
 * @ClassName: ITaskCleanRetryLogService
 * @Description: TaskCLeanRetryLog的服务接口
 * @Author: cuiss
 * @CreateDate: 2020/2/21 14:04
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public interface ITaskCleanRetryLogService {
    /**
     * 多线程查询列表
     * @param threadIndex
     * @param threadCount
     * @return
     */
    public List<TaskCleanRetryLog> queryTaskCleanRetryLogList(int threadIndex, int threadCount);
}
