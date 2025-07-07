package com.keensense.picturestream.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Created by zhanx xiaohui on 2019-07-04.
 */
@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {

        // return Executors.newScheduledThreadPool(10);
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("thread-schedule-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;

    }
}

/**
 * @program: platform
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-07-04 19:18
 **/