package com.keensense.admin.mqtt.config;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Autowired
    PropsConfig propsConfig;

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(propsConfig.getConcurrency());
        //设置为批量消费
        factory.setBatchListener(true);
        ContainerProperties containerProperties = factory.getContainerProperties();
        //containerProperties.setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE);
        containerProperties.setPollTimeout(propsConfig.getPollTimeout());
        //有值就不监听
        if (null != propsConfig.getStopListener()){
            factory.setAutoStartup(false);
        }
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        // bootstrap.servers
        if( !Objects.isNull(propsConfig.getBroker())){
            propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, propsConfig.getBroker());
        }
        // enable.auto.commit
        if (!Objects.isNull(propsConfig.getEnableAutoCommit())){
            propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, propsConfig.getEnableAutoCommit());
        }
        // auto.commit.interval.ms
        if (!Objects.isNull(propsConfig.getAutoCommitInterval())) {
            propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, propsConfig.getAutoCommitInterval());
        }

        //propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        // key.deserializer
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // value.deserializer
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // group.id
        if (!Objects.isNull(propsConfig.getGroupId())) {
            propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, propsConfig.getGroupId());
        }
        // auto.offset.reset
        if (!Objects.isNull(propsConfig.getAutoOffsetReset())) {
            propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, propsConfig.getAutoOffsetReset());
        }
        // max.poll.records
        if (!Objects.isNull(propsConfig.getMaxPollRecords())) {
            propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, propsConfig.getMaxPollRecords());
        }
        // max.partition.fetch.bytes
        if (!Objects.isNull(propsConfig.getMaxPartitionFetchBytes())){
            propsMap.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, propsConfig.getMaxPartitionFetchBytes());
        }
        // fetch.max.bytes
        if (!Objects.isNull(propsConfig.getFetchMaxBytes())){
            propsMap.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, propsConfig.getFetchMaxBytes());
        }
        // fetch.min.bytes
        if (!Objects.isNull(propsConfig.getFetchMinBytes())){
            propsMap.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, propsConfig.getFetchMinBytes());
        }
        // fetch.max.wait.ms
        if (!Objects.isNull(propsConfig.getFetchMaxWaitMs())){
            propsMap.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG,propsConfig.getFetchMaxWaitMs());
        }

        return propsMap;
    }



}
