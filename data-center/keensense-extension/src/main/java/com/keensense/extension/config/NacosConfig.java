package com.keensense.extension.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;


/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/14 14:54
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Configuration
@Data
@RefreshScope
public class NacosConfig {
    @Value("${algo.switch}")
    private Boolean algoSwitch;
    @Value("${face.faceClassPath}")
    private String faceClassPath;
    @Value("${face.faceServiceUrl}")
    private String faceServiceUrl;
    @Value("${face.staticFeatureVersion}")
    private String faceStaticFeatureVersion;
    @Value("${face.monitorFeatureVersion}")
    private String faceMonitorFeatureVersion;
    @Value("${body.bodyClassPath}")
    private String bodyClassPath;
    @Value("${body.bodyServiceUrl}")
    private String bodyServiceUrl;
    @Value("${face.glLibHours}")
    private int libHours;
    @Value("${face.faceGlstTempPath}")
    private String faceGlstTempPath;
    /**
     * @description:是否开启一人一档
     */
    @Value("${docuemnt.service.exist}")
    private Boolean docuemntServiceExist;

    @Value("${face.glStructPort}")
    private String glStructPort;
    @Value("${face.glComparePort}")
    private String glComparePort;
    @Value("${face.glStructDetectMode}")
    private String glStructDetectMode;

    @Value("${spring.redis.host}")
    private String redisIp;
    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${feature.extract.url}")
    private String featureExtractUrl;
    @Value("${face.feature.extract.url}")
    private String faceFeatureExtractUrl;
    @Value("${face.stFastSearch}")
    private String stFastSearch;

    @Value("${algo.body.cluster}")
    private Integer algoBodyCluster;
    @Value("${archives.type}")
    private Integer archivesType;


}
