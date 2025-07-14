package com.keensense.task.async.executor;

import com.keensense.task.async.AsyncQueueConstants;
import com.keensense.task.async.Message;
import com.keensense.task.async.task.TaskInvoker;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @ClassName: AsyncTaskExecutor
 * @Description: 基础异步执行器
 * @Author: cuiss
 * @CreateDate: 2019/8/11 18:26
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public abstract class AsyncTaskExecutor implements IAsyncTaskExecutor {

    protected String resultInfo = "Success";

    protected String resultCode = AsyncQueueConstants.TASK_ERROR_MESSAGE_OK;

    protected String threadName = Thread.currentThread().getName();


    @Setter
    @Getter
    private long timeoutSeconds;

    @Override
    public boolean doBefore(String taskId, Message message) {
        long ttl = message.getTtl();
        long sendTime = message.getSendtime();
        timeoutSeconds = message.getTimeoutSecond();
        //判断消息是否超时
        if (System.currentTimeMillis() - sendTime > ttl) {
            log.error(">>>>>current thread:{}, 异步消息已经超时...taskId:{},message:{}", threadName, taskId, message);
            resultCode = AsyncQueueConstants.TASK_ERROR_MESSAGE_OVERTIME;
            return false;
        } else {
            log.info(">>>>>current thread:{},异步消息有效...taskId:{},messsage:{}", threadName, taskId, message);
            return true;
        }
    }

    @Override
    public void doAsyncTask(String taskId, Map<String, Object> param) {
        if (timeoutSeconds <= 0) {
            timeoutSeconds = 300000L;
        }
        try {
            log.info(">>>>>>>>>>>>current thread:{},具体消费行为,taskId:{},param:{}", threadName, taskId, param);
            resultCode = TaskInvoker.invoke(this, "execute", new Object[]{param}, timeoutSeconds);
//            resultCode = execute(param);
            log.info(">>>>>>>>>current thread:{}, execute result :{}", threadName, resultCode);
        } catch (Exception e) {
            log.error(">>>>>>>>current thread:{},Exception:{}", threadName, e.getMessage());
            e.printStackTrace();
        }

    }

    public abstract String execute(Map<String, Object> param);

    @Override
    public void doAfter(Message message) {
        log.info(">>>>current thread:{},异步任务执行结果：[{}]", threadName, getResultInfo());
    }

    @Override
    public String getResultInfo() {
        return this.resultCode;
    }
}
