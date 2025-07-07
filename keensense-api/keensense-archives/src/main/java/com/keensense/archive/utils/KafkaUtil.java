package com.keensense.archive.utils;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.TopicPartition;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by memory_fu on 2019/7/8.
 */
@Slf4j
public class KafkaUtil {

    private static KafkaProducer<String, String> producer;

    /**
     * 初始化producer实例
     */
    private static void initProducerParams(String brokerList) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        producer = new KafkaProducer<>(props);
    }

    /**
     * 初始化consumer实例
     */
    public static KafkaConsumer<String, String> initConsumerParams(String brokerList, String topic, String groupId,
                                                                   Integer partition) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "false");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        // 1.创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        if (partition != null) {
            TopicPartition topicPartition = new TopicPartition(topic, partition);
            consumer.assign(Arrays.asList(topicPartition));
        } else {
        }
        consumer.subscribe(Arrays.asList(topic));

        return consumer;
    }

    /**
     * 单例方式获取producer实例
     */
    private static KafkaProducer<String, String> getProducer(String brokerList) {
        if (null == producer) {
            initProducerParams(brokerList);
        }
        return producer;
    }

    /**
     * 推送消息
     */
    public static boolean sendMessage(String topic, String value, String brokerList) {
        boolean result = true;
        KafkaProducer<String, String> producer = getProducer(brokerList);
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
            producer.send(record, (recordMetadata, e) -> {
                if (e != null) {
                    log.error("kafka send message failed!");
                    // 1. 记录失败消息到死信队列
                    // 2. 触发告警系统（集成Prometheus+AlertManager）
//                    alertService.send("PRODUCER_FAIL", record.key());
                } else {
                    log.info("kafka send message success!");
                }
            });
            producer.flush();
        } catch (Exception e) {
            result = false;
            log.error("======sendMessage Exception:", e);
        }
        return result;
    }

    /**
     * 接收消息
     */
    public static ConsumerRecords<String, String> receiveMessage(String brokerList, String topic, String groupId,
                                                                 Long timeout, Integer partition) {

        long setTimeout = 100;
        if (timeout != null) {
            setTimeout = timeout;
        }

        KafkaConsumer<String, String> kafkaConsumer = initConsumerParams(brokerList, topic, groupId, partition);
        return kafkaConsumer.poll(setTimeout);
    }

    // public static void main(String[] args) {
    // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //
    // String message1 = "{\"RunTime\": \""+dateFormat.format(new Date())+"\",\"data\": [{\"clusterIndex\":
    // \"1\",\"uuid\": \"1\",\"clusterFlag\": \"true\",\"combineCluster\": [\"1\",\"2\",\"3\"]}]}";
    // String message2 = "{\"RunTime\": \""+dateFormat.format(new Date())+"\",\"data\": [{\"clusterIndex\":
    // \"3\",\"uuid\": \"3\",\"clusterFlag\": \"true\",\"combineCluster\": [\"3\"]}]}";
    // String message3 = "{\"RunTime\": \""+dateFormat.format(new Date())+"\",\"data\": [{\"clusterIndex\":
    // \"2\",\"uuid\": \"2\",\"clusterFlag\": \"false\",\"combineCluster\": [\"2\"]}]}";
    //
    // KafkaUtil.sendMessage("receive_cluster_topic","1",message1,"172.16.1.118:39092");
    // KafkaUtil.sendMessage("receive_cluster_topic","1",message2,"172.16.1.118:39092");
    // KafkaUtil.sendMessage("receive_cluster_topic","1",message3,"172.16.1.118:39092");
    //
    // }

}
