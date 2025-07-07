package com.keensense.task.async.executor;

import com.keensense.task.async.AsyncQueueConstants;
import com.keensense.task.async.Message;

/**
 * @ClassName: ReloadTaskExecutor
 * @Description: 需要重试的消息执行器
 * @Author: cuiss
 * @CreateDate: 2019/9/5 10:13
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public abstract class ReloadTaskExecutor extends AsyncTaskExecutor {

    public ReloadTaskExecutor(){

    }

    /**
     * 假如处理失败，则重试消息
     * @param message
     */
    @Override
    public void doAfter(Message message) {
        if(!AsyncQueueConstants.TASK_ERROR_MESSAGE_OK.equals(resultCode)){
            this.reloadTask(message);
        }
    }

    public abstract void  reloadTask(Message message);


}
