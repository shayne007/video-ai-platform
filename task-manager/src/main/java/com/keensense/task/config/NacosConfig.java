package com.keensense.task.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;


/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/6/27 14:54
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Configuration
@Data
@RefreshScope
public class NacosConfig {
    @Value("${zookeeper.switch}")
    private Boolean zkSwitch;
    @Value("${zookeeper.capability}")
    private Integer capability;
    @Value("${zookeeper.retrycount}")
    private Integer retryCount;
    @Value("${zookeeper.server}")
    private String server;
    @Value("${zookeeper.sessionTimeoutMs}")
    private Integer sessionTimeoutMs;
    @Value("${zookeeper.connectionTimeoutMs}")
    private Integer connectionTimeoutMs;
    @Value("${zookeeper.maxRetries}")
    private Integer maxRetries;
    @Value("${zookeeper.baseSleepTimeMs}")
    private Integer baseSleepTimeMs;

    /**
     * 下发的任务类型
     */
    @Value("${zookeeper.taskTypes}")
    private String taskTypes;


    @Value("${vas.analysisMethod}")
    private Integer analysisMethod;
    @Value("${vas.ip}")
    private String vasIp;
    @Value("${vas.port}")
    private String vasPort;
    @Value("${vas.length}")
    private Integer analysisLength;
    @Value("${vas.timeout}")
    private Integer analysisTimeOut;
    @Value("${vas.retrySleep}")
    private Integer analysisRetrySleep;
    @Value("${vas.retryCount}")
    private Integer analysisRetryCount;
    @Value("${vas.threadCount}")
    private Integer analysisThreadCount;
    @Value("${vas.timeStep}")
    private Integer analysisTimeStep;
    @Value("${vas.useHistoryData}")
    private Boolean analysisUseHistoryData;
    @Value("${vas.transCodeSwitch}")
    private Boolean analysisTransCodeSwitch;

    @Value("${vas.error.downloadInit}")
    private String analysisDownloadInitError;
    @Value("${vas.error.transCode}")
    private String analysisTransCodeError;
    @Value("${vas.error.downloadProcess}")
    private String analysisDownloadProcess;
    @Value("${vas.error.downloadPath}")
    private String analysisDownloadPath;
    @Value("${vas.error.analysis}")
    private String analysisError;

    @Value("${clean.value}")
    private Integer cleanDiskValue;
    @Value("${clean.offline.switch}")
    private Boolean cleanOfflineSwitch;
    @Value("${clean.offline.saveDay}")
    private Integer cleanOfflineSaveDay;
    @Value("${clean.online.switch}")
    private Boolean cleanOnlineSwitch;
    @Value("${clean.online.saveDay}")
    private Integer cleanOnlineSaveDay;

    @Value("${bit.switch}")
    private Boolean bitSwitch;
    @Value("${bit.timeStep}")
    private Long bitTimeStep;

    @Value("${capture.url}")
    private String captureUrl;

    @Value("${objext.url}")
    private String objextUrl;

    @Value("${analysis.track.switch}")
    private Boolean analysisTrackSwitch;


}
