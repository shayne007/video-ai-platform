package com.keensense.commonlib.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:配置文件
 * @Author: jingege
 * @CreateDate: 2019/5/14 14:54
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Configuration
@Data
@RefreshScope
public class NacosConfig  {
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

    @Value("${face.glStructPort}")
    private String glStructPort;
    @Value("${face.glComparePort}")
    private String glComparePort;
    @Value("${face.glStructDetectMode}")
    private String glStructDetectMode;

    @Value("${feature.extract.url}")
    private String featureExtractUrl;
    @Value("${face.feature.extract.url}")
    private String faceFeatureExtractUrl;

    @Value("${face.stFastSearch}")
    private String stFastSearch;

}
