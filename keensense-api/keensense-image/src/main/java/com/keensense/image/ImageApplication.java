package com.keensense.image;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

/**
 * Created by zhanx xiaohui on 2019-09-03.
 */

@SpringCloudApplication
@EnableFeignClients
public class ImageApplication {
    public static void main(String[] args) {
        ApplicationContext atx = SpringApplication.run(ImageApplication.class, args);
    }
}

/**
 * @program: platform
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-09-03 10:16
 **/