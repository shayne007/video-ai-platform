package com.keensense.archive.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by memory_fu on 2019/12/31.
 */
@Configuration
@Data
@RefreshScope
public class ArchiveConfig {
    
    @Value("${archive.kafka.topic.firm.receive}")
    private String kafkaTopicFirmReceive;
    @Value("${archive.kafka.groupId.firm.receive}")
    private String kafkaGroupIdFirmReceive;
    
    @Value("${archive.kafka.topic.professional.send}")
    private String kafkaTopicProfessionalSend;
    @Value("${archive.kafka.groupId.professional.send}")
    private String kafkaGroupIdProfessionalSend;
    
    @Value("${kafka.bootstrap}")
    private String kafkaBrokerList;
    
    @Value("${feature.search.ip}")
    private String featureHost;
    
    public static final String featurePort = "39081";
    
    @Value("${archive.featureSearch.task.num}")
    private String taskNum;
    
    @Value("${face.feature.firm}")
    private Integer firm;
    
}
