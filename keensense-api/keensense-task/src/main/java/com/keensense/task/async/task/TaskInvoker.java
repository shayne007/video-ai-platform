package com.keensense.task.async.task;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName: TaskInvoker
 * @Description: 业务线程执行器
 * @Author: cuiss
 * @CreateDate: 2019/8/11 14:58
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public final class TaskInvoker {

    private static final int MAX_POOLNUM = 1000;
    private static final int CORE_POOLNUM = 10;
    private static final long DEFAULT_TIMEOUT = 60000L;
    private static ExecutorService executor;
    private static ThreadFactory threadFactory = new TaskThreadFactory("executor");

    static {
        executor = new ThreadPoolExecutor(CORE_POOLNUM, MAX_POOLNUM,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                threadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 关闭执行器
     */
    public static final void shutdown() {
        executor.shutdown();
    }

    public static final <T> T invoke(Object object, String method, Object[] params) throws Exception {
        return invoke(object, method, params, DEFAULT_TIMEOUT);
    }

    public static final <T> T invoke(Object object, Method method, Object[] params) throws Exception {
        return invoke(object, method.getName(), params, DEFAULT_TIMEOUT);
    }

    public static final <T> T invoke(Object object, Method method, Object[] params, long timeout) throws Exception {
        return invoke(object, method.getName(), params, timeout);
    }

    public static final <T> T invoke(Object object, String method, Object[] params, long timeout) throws Exception {
        TaskCallable<T> taskCallable = new TaskCallable<>();
        taskCallable.setObject(object);
        taskCallable.setMethodName(method);
        taskCallable.setParam(params);
        return invokeService(taskCallable, object.getClass().getName(), method, params, timeout);
    }

    public static final <T> T invokeService(TaskCallable<T> taskCallable, String className, String method, Object[] params, long timeout) throws Exception {
        Future<T> future;
        T result;
        if (timeout < 0L) {
            log.error("[{}]调用超时或系统开启自动保护", className);
        } else {
            if (timeout == 0L) {
                timeout = DEFAULT_TIMEOUT;
            }
            taskCallable.setTimeout(timeout);
        }
        try {
            future = executor.submit(taskCallable);
            result = future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e1) {
            log.error("调用[{}]超时[{}]秒", className, timeout / 1000);
            throw e1;
        } catch (InterruptedException e2) {
            log.error("调用[{}]被中断,Exception:{}", className, e2.getMessage());
            throw e2;
        } catch (ExecutionException e3) {
            log.error("调用[{}]执行异常,Exception:{}", className, e3.getMessage());
            throw e3;
        } catch (RejectedExecutionException e4) {
            log.error("线程繁忙，请调整并发线程数[{}],Exception:{}", MAX_POOLNUM, e4.getMessage());
            throw e4;
        } finally {
            future = null;
            taskCallable = null;
        }
        return result;
    }

}
