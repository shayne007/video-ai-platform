package com.keensense.admin.mqtt.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@RefreshScope
public class PropsConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String broker;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private String enableAutoCommit;

    @Value("${spring.kafka.consumer.auto-commit-interval}")
    private String autoCommitInterval;

    @Value("${spring.kafka.consumer.max-poll-records}")
    private String maxPollRecords;

    @Value("${spring.kafka.listener.concurrency}")
    private Integer concurrency;

    @Value("${spring.kafka.listener.poll-timeout}")
    private Long pollTimeout;

    @Value("${spring.kafka.consumer.max-partition-fetch-bytes}")
    private String maxPartitionFetchBytes;

    @Value("${spring.kafka.consumer.fetch-max-bytes}")
    private String fetchMaxBytes;

    @Value("${spring.kafka.consumer.fetch-min-bytes}")
    private String fetchMinBytes;

    @Value("${spring.kafka.consumer.fetch-max-wait-ms}")
    private Integer fetchMaxWaitMs;

    @Value("${spring.kafka.consumer.stop-listener}")
    private Integer stopListener;

    public String getMaxPartitionFetchBytes(){
        String value = maxPartitionFetchBytes;
        return getBytesValue(value);
    }

    public String getFetchMaxBytes() {
        String value = fetchMaxBytes;
        return getBytesValue(value);
    }

    public String getFetchMinBytes() {
        String value = fetchMinBytes;
        return getBytesValue(value);
    }

    private String getBytesValue(String value) {
        value = value.trim();
        if(value.contains("M") || value.contains("m")){
            value = value.substring(0,value.length() - 1);
            return String.valueOf(Long.parseLong(value) * 1024 * 1024);
        } else if(value.contains("G") || value.contains("g")) {
            value = value.substring(0,value.length() - 1);
            return String.valueOf(Long.parseLong(value) *1024 * 1024 * 1024);
        }
        return null;
    }
}
