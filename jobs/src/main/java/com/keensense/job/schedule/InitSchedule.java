package com.keensense.job.schedule;

import com.keensense.job.task.ISyncVasDataTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Author cuiss
 * @Description 工程启动的时候自动运行的方法
 * @Date 2018/11/9
 */
@Component
@Slf4j
public class InitSchedule implements ApplicationRunner {

    @Autowired
    private ISyncVasDataTask syncVasDataTask;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        log.info("init.........");
        syncVasDataTask.syncVasData();
    }
}
