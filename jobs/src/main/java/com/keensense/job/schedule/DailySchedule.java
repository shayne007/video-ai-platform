package com.keensense.job.schedule;


import com.keensense.job.service.ICronService;
import com.keensense.job.task.impl.SyncVasDataTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.time.LocalDateTime;

/**
 * @Author cuiss
 * @Description // 定时执行器
 * @Date 2018/11/6
 */
@Slf4j
@Configuration
@EnableScheduling
public class DailySchedule implements SchedulingConfigurer {


    @Autowired
    private ICronService cronService;

    @Autowired
    private SyncVasDataTask syncVasDataTask;


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        log.info("------定时任务触发器......");
        taskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                () ->{
                    log.info("执行定时任务: " + LocalDateTime.now().toLocalTime());
                    syncVasDataTask.syncVasData();
                },
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    // 从数据库获取执行周期
                    String cron = cronService.getCronScript();
                    // 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                });
    }

}