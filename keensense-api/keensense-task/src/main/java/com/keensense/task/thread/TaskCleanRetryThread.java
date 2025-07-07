package com.keensense.task.thread;

import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.TaskCleanRetryLog;
import com.keensense.task.service.IDeleteTaskService;
import com.keensense.task.service.ITaskCleanRetryLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: TaskCleanRetryThread
 * @Description: task删除任务重试线程
 * @Author: cuiss
 * @CreateDate: 2020/2/13 14:54
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Component
@Slf4j
@Qualifier("taskCleanRetryThread")
@Scope("prototype")
public class TaskCleanRetryThread extends BaseSchedualTask {

    @Autowired
    private ITaskCleanRetryLogService taskCLeanRetryLogService;

    @Autowired
    private IDeleteTaskService deleteTaskService;

    @Override
    public void doTask() {
        List<TaskCleanRetryLog> taskCleanRetryLogList = taskCLeanRetryLogService
                .queryTaskCleanRetryLogList(this.getThreadIndex().get(),this.getThreadCount().get());
        if(taskCleanRetryLogList != null && taskCleanRetryLogList.size()>0 ){
            doDeleteService(taskCleanRetryLogList);
        }
    }

    /**
     * 重试删除数据
     * @param taskCleanRetryLogList
     */
    private void doDeleteService(List<TaskCleanRetryLog> taskCleanRetryLogList) {
        for(TaskCleanRetryLog taskCleanRetryLog:taskCleanRetryLogList){
            deleteTaskService.deleteEs(taskCleanRetryLog.getSerialnumber(),null, TaskConstants.ANALY_TYPE_OBJEXT);
        }
    }
}
