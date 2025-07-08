package com.keensense.job.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author cuiss
 * @Description vas url地址路径配置
 * @Date 2018/11/7
 */
@Component
@ConfigurationProperties(prefix = "vas-url")
public class VasUrlConfig {

    /**
     * vas用户名
     */
   @Setter
   @Getter
   private String name;

    /**
     * vas密码
     */
    @Setter
    @Getter
    private String psw;

    /**
     * vas服务器ip
     */
    @Setter
    @Getter
    private String srvip;

    /**
     * vas端口
     */
    @Setter
    @Getter
    private String srvport;
}
