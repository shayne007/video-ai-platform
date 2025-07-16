package com.keensense.search;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;

@ImportResource(locations = "classpath*:/applicationContext.xml")
@SpringCloudApplication
@EnableFeignClients
public class SearchApplication {

    public static void main(String[] args) {
//        ApplicationContext atx = SpringApplication.run(SearchApplication.class, args);


        ConfigurableApplicationContext applicationContext = SpringApplication.run(SearchApplication.class, args);
//        new Config(applicationContext).create();

        Environment env = applicationContext.getEnvironment();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        System.out.println("\n----------------------------------------------------------\n\t" +
                "Application is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/index.html\n\t" +
                "swagger-ui: \thttp://localhost:" + port + path + "/swagger-ui.html\n\t" +
                "----------------------------------------------------------");
    }

}
