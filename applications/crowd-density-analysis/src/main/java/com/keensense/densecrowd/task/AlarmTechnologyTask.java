package com.keensense.densecrowd.task;

import com.keensense.common.util.HttpU2sGetUtil;
import com.keensense.densecrowd.util.DbPropUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 22:51 2019/12/28
 * @Version v0.1
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class AlarmTechnologyTask {
    /**
     * 报警
     */
    @Scheduled(cron = "0 1/1 * * * ?")
    public void alarmTechnology() {
        log.info("定时报警");
        if (DbPropUtil.getBoolean("alarm_technology_task_test", false)) {
            String username = DbPropUtil.getString("alarm_technology_username", "admin");
            String pwd = DbPropUtil.getString("alarm_technology_pwd", "888888");
            String urls = DbPropUtil.getString("alarm_technology_urls", "10.10.10.123");
            String[] url = urls.split(",");
            String content = username + ":" + pwd;
            // 加密
            BASE64Encoder encoder = new BASE64Encoder();
            String author = encoder.encode(content.getBytes());
            for (String u : url) {
                String send = "http://" + u + "/cdor.cgi?open=a";
                String result = HttpU2sGetUtil.getHttp(send, author);
                log.info("result:" + result);
            }
        }
    }
}