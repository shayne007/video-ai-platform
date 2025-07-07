package com.keensense.task.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName: MonitorApplication
 * @Description: 基于Spring boot Admin的监控模块
 * @Author: cuiss
 * @CreateDate: 2019/5/1 23:14
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@EnableAdminServer
@SpringBootApplication
public class MonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(MonitorApplication.class, args);
    }

}
