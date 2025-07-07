package com.keensense.task.async.executor;


import com.keensense.task.async.Message;

import java.util.Map;

/**
 * @ClassName: IAsyncTaskExecutor
 * @Description: 执行器接口
 * @Author: cuiss
 * @CreateDate: 2019/8/11 10:24
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public interface IAsyncTaskExecutor {

    public boolean doBefore(String taskId, Message message);

    public void doAsyncTask(String taskId, Map<String, Object> param);

    public void doAfter(Message message);

    public String getResultInfo();
}
