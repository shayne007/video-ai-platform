package com.keensense.task.async.listener.callable;


import com.keensense.task.async.AsyncQueueConstants;
import com.keensense.task.async.Message;
import com.keensense.task.async.executor.IAsyncTaskExecutor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @ClassName: TaskExecutorCallable
 * @Description:
 * @Author: cuiss
 * @CreateDate: 2019/8/12 20:31
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class TaskExecutorCallable implements Callable {

    @Getter@Setter private Message message;
    @Getter@Setter private Class executorClass;

    public TaskExecutorCallable(Message message,Class executorClass){
        this.message = message;
        this.executorClass = executorClass;
    }

    @Override
    public Object call() throws Exception {
        String taskId = this.message.getTaskId();
        Map<String,Object> params = this.message.getParam();
        IAsyncTaskExecutor asyncTaskExecutor = (IAsyncTaskExecutor) this.executorClass.newInstance();
        if(!asyncTaskExecutor.doBefore(taskId,message)){
            return AsyncQueueConstants.TASK_ERROR_MESSAGE_OVERTIME;
        }
        asyncTaskExecutor.doAsyncTask(taskId,params);
        asyncTaskExecutor.doAfter(this.message);
        return AsyncQueueConstants.TASK_ERROR_MESSAGE_OK;
    }
}
