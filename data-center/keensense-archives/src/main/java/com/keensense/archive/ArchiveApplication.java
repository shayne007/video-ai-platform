package com.keensense.archive;

import com.keensense.archive.service.impl.ProcessDataServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 一人一档
 */
@ImportResource(locations = "classpath*:/applicationContext.xml")
@SpringCloudApplication
@EnableScheduling
@EnableFeignClients
public class ArchiveApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ArchiveApplication.class);
        springApplication.run(args);

        ProcessDataServiceImpl processDataService = new ProcessDataServiceImpl();
        processDataService.processClusterData();
    }
}