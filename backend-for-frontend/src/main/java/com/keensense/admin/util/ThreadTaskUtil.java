package com.keensense.admin.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author admin
 */
public class ThreadTaskUtil {

    private ThreadTaskUtil() {

    }

    public static Future submit(Runnable task) {
        return Holder.SERVICE.submit(task);
    }

    private static class Holder {
        public static final ExecutorService SERVICE = new ThreadPoolExecutor(15, 30, 20,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), new ThreadFactoryBuilder().setNameFormat("task-util-pool-%d").build(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
