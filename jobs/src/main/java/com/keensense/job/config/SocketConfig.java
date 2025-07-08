package com.keensense.job.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author cuiss
 * @Description //socket长连接 所需配置
 * @Date 2018/11/5
 */
@Component
@ConfigurationProperties(prefix = "vas-server")
public class SocketConfig {

    /**
     * ip地址
     */
    @Getter
    @Setter
    private String ip;

    /**
     * 端口
     */
    @Getter
    @Setter
    private Integer port;

    /**
     * 用户名
     */
    @Getter
    @Setter
    private String username;

    /**
     * 密码
     */
    @Getter
    @Setter
    private String password;
}
