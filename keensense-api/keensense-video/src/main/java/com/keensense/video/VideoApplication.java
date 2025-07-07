package com.keensense.video;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created by zhanx xiaohui on 2019-12-17.
 */
@SpringCloudApplication
@EnableFeignClients
public class VideoApplication {
    public static void main(String[] args) {
        SpringApplication.run(VideoApplication.class, args);
    }

}

/**
 * @program: platform
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-12-17 15:30
 **/