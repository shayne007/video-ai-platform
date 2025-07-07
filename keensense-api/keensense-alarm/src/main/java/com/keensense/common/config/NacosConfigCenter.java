package com.keensense.common.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author ycl
 * @date 2019/5/25
 */

@Configuration
@Data
@ToString
@RefreshScope
public class NacosConfigCenter {

    @Value("${alarm.start.switch}")
    private boolean alarmStart;

    @Value("${alarm.quality.threshold}")
    private float quality;
    @Value("${alarm.score.threshold}")
    private float score;
    @Value("${alarm.subscriber.url}")
    private String subscriberUrl;
    @Value("${alarm.quality.body-threshold}")
    private float bodyQuality;



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

    @Value("${face.feature.firm}")
    private Integer firm;


}
