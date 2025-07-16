package com.keensense.image.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author fengsy
 * @date 7/5/21
 * @Description
 */
@RefreshScope
@Data
public class FastDfsConfig {
    @Value("${fastdfs.connecttimeout}")
    String connectTimeout;
    @Value("${fastdfs.networktimeout}")
    String networkTimeout;
    @Value("${fastdfs.trackerserver}")
    String trackerServer;
    @Value("${fastdfs.trackerserver.port}")
    String trackerServerPort;

    @Value("${image.url.remote}")
    String remoteUrl;

    @Value("${image.group.remote}")
    String group;

    @Value("${origin.es.datasource.port}")
    String port;
    @Value("${origin.es.datasource.host}")
    String host;
    @Value("${origin.es.datasource.username}")
    String username;
}
