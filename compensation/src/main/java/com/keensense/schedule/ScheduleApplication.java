package com.keensense.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by memory_fu on 2020/5/11.
 */
@ImportResource(locations = "classpath*:/applicationContext.xml")
@SpringCloudApplication
@Slf4j
@EnableScheduling
@EnableFeignClients
public class ScheduleApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ScheduleApplication.class);
        springApplication.run(args);
    }

}
