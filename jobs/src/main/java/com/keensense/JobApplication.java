package com.keensense;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @ClassName: JobApplication
 * @Description: 任务服务启动类
 * @Author: cuiss
 * @CreateDate: 2019/11/6 14:53
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@SpringBootApplication
@SpringCloudApplication
@Slf4j
public class JobApplication {

    public static void main(String[] args){
        SpringApplication.run(JobApplication.class, args);
        log.info("job server启动开始========");
    }
}
