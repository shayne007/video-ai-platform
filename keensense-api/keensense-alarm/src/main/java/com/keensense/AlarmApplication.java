package com.keensense;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * @author ycl
 * @date 2019/5/10
 */

@SpringCloudApplication
@EnableFeignClients
public class AlarmApplication {


    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AlarmApplication.class);
//		springApplication.addListeners(new ApplicationStartup());
        springApplication.run(args);
    }

}
