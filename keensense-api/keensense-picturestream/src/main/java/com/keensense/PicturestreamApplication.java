package com.keensense;

import com.keensense.picturestream.starter.ApplicationStartup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringCloudApplication
@Slf4j
@EnableScheduling
@EnableFeignClients
public class PicturestreamApplication {

    public static void main(String[] args){
        SpringApplication springApplication = new SpringApplication(PicturestreamApplication.class);
        springApplication.addListeners(new ApplicationStartup());
        springApplication.run(args);
    }

}
