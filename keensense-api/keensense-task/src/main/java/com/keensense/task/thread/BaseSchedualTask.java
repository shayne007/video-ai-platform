package com.keensense.task.thread;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName: BaseSchedualTask
 * @Description: 基础的线程类
 * @Author: cuiss
 * @CreateDate: 2020/2/13 15:45
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public abstract class BaseSchedualTask implements Runnable {

    /**
     * 线程数
     */
    @Setter
    @Getter
    private AtomicInteger threadCount;

    /**
     * 线程index
     */
    @Setter
    @Getter
    private AtomicInteger threadIndex;

    /**
     * 执行次数
     */
    @Setter
    @Getter
    private AtomicLong executeCount;

    /**
     * 是否启用日志
     */
    @Setter
    @Getter
    private AtomicBoolean enableLog;

    @Getter
    @Setter
    private String threadGroupName;

    @Override
    public void run() {
        while (true) {
            if (enableLog.get()) {
                log.info("{}:{}-{} started...", new Date(), threadGroupName, threadIndex);
            }
            consumeTask();
            try {
                Thread.sleep(30000L);
            } catch (Exception e) {
                if (enableLog.get()) {
                    e.printStackTrace();
                    log.info("{}:{}-{} exception:{}...", new Date(), threadGroupName, threadIndex, e.getMessage());
                }
            }

        }

    }

    private void consumeTask() {
        try {
            doTask();
            executeCount.addAndGet(1L);
            if (enableLog.get()) {
                log.info("{}:{}-{} running {} times", new Date(), threadGroupName, threadIndex, executeCount);
            }
        } catch (Exception e) {
            log.error(">>>>{}:{}-{} Exception:{}", new Date(), threadGroupName, threadIndex, e.getMessage());
        }


    }

    public abstract void doTask();

}
