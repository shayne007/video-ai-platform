package com.keensense.task.async.listener;

import com.keensense.task.async.Message;
import com.keensense.task.async.listener.callable.TaskExecutorCallable;
import com.keensense.task.async.task.TaskCollects;
import com.keensense.task.async.task.TaskThreadFactory;
import com.keensense.task.async.util.SerializationUtils;
import com.keensense.task.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: QueueListener
 * @Description: 队列监听器
 * @Author: cuiss
 * @CreateDate: 2019/8/12 20:26
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class QueueListener extends Thread {

    @Getter
    @Setter
    private String queueName;

    @Getter
    @Setter
    private Class executeClass;

    public QueueListener(String queueName, Class executeClass) {
        this.queueName = queueName;
        this.executeClass = executeClass;
    }

    private static final String SHAKEHANDS_KEY = "SHAKE-HANDS";

    private static final String HEARTBEAT_KEY = "HEART-BEAT";

    private static final String TASK_COUNT_KEY = "TASK-COUNT";

    private static ExecutorService executorService;

    private static ThreadFactory threadFactory = new TaskThreadFactory();

    private Map<String, Object> localCache = new ConcurrentHashMap<>();

    private AtomicInteger taskCount = new AtomicInteger(0);

    static {
        executorService = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(5),
                threadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    public void run() {
        shakehands();
        long loopcnt = 0L;
        while (true) {
            while (true) {
                ++loopcnt;
                // 每遍历1000次保持一下心跳
                byte[] payloads = TaskCollects.getInstance().lpop(this.queueName);
                if (payloads != null && payloads.length > 0) {
                    Message message = (Message) SerializationUtils.deserialize(payloads);
                    log.info(">>>>>>>message:=====id:" + message.getTaskId(), ",param:" + message.getParam());
                    consumeMessage(message, this.executeClass);
                    this.updateStatistics();
                }
                if (loopcnt % 1000 == 0) {
                    heartbeat();
                }
            }
        }
    }

    private void consumeMessage(Message message, Class executeClass) {
        TaskExecutorCallable taskExecutorCallable = new TaskExecutorCallable(message, executeClass);
        FutureTask<TaskExecutorCallable> futureTask = new FutureTask<>(taskExecutorCallable);
        executorService.execute(futureTask);
        try {
            futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void updateStatistics() {
        localCache.put(TASK_COUNT_KEY, taskCount.addAndGet(1));
        log.info(">>>>>>>>>>>>> task statistics:" + taskCount.intValue());
    }

    private void heartbeat() {
        String dateString = DateUtil.formatDate(System.currentTimeMillis(), DateUtil.YYYYMMDDHHMMSS);
        localCache.put(HEARTBEAT_KEY, dateString);
        // log.debug(">>>>>>>>>> heartbeat at {}",dateString);
    }

    private void shakehands() {
        String dateString = DateUtil.formatDate(System.currentTimeMillis(), DateUtil.YYYYMMDDHHMMSS);
        localCache.put(SHAKEHANDS_KEY, dateString);
        log.info(">>>>>>>>>> shakehands at {}", dateString);
    }

}
