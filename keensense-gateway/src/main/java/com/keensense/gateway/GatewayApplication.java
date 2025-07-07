package com.keensense.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: GatewayApplication
 * @Description: 千视通网关服务
 * @Author: cuiss`	
 * @CreateDate: 2019/5/1 22:31
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@SpringCloudApplication
@Slf4j
@RestController
public class GatewayApplication {
    public static void main(String[] args){
        SpringApplication.run(GatewayApplication.class,args);
        log.info("keensense gateway is running......");
    }


    @GetMapping(value = "/fallbackcontroller")
    public Map<String, String> fallBackController() {
        Map<String, String> res = new HashMap();
        res.put("code", "-100");
        res.put("data", "service not available");
        return res;
    }

}
