package com.keensense.admin.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created by memory_fu on 2019/7/8.
 */
@Slf4j
public class KafkaUtil {

    private KafkaUtil(){}
    
    private static KafkaProducer<String, String> producer;

    private static void initParams(String brokerList, String groupId) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokerList);
        props.put("group.id", groupId);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all");
        props.put("retries", 3);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        producer = new KafkaProducer<>(props);
    }
    
    private static KafkaProducer<String, String> getProducer(String brokerList, String groupId) {
        if (null == producer) {
            initParams(brokerList, groupId);
        }
        return producer;
    }
    
    public static boolean sendMessage(String topic, String groupId, String value,
        String brokerList) {
        boolean result = true;
        KafkaProducer<String, String> producer = getProducer(brokerList, groupId);
        try {
            producer.send(new ProducerRecord<>(topic, value));
            producer.flush();
        } catch (Exception e) {
            result = false;
            log.error("======sendMessage Exception:", e);
        }
        return result;
    }

}
