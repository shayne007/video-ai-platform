package com.keensense.search.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Created by memory_fu on 2019/7/8.
 */
@Slf4j
public class KafkaUtil {
    
    private static KafkaProducer<String, String> producer;
    
    /**
     * 初始化producer实例
     */
    private static void initProducerParams(String brokerList, String groupId) {
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
    
    /**
     * 初始化consumer实例
     */
    public static KafkaConsumer<String, String> initConsumerParams(String brokerList, String topic,
        String groupId) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokerList);
        props.put("group.id", groupId);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //1.创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        List<String> subscribedTopics = new ArrayList<>();
        subscribedTopics.add(topic);
        consumer.subscribe(subscribedTopics);
        
        return consumer;
    }
    
    /**
     * 单例方式获取producer实例
     */
    private static KafkaProducer<String, String> getProducer(String brokerList, String groupId) {
        if (null == producer) {
            initProducerParams(brokerList, groupId);
        }
        return producer;
    }
    
    /**
     * 推送消息
     */
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
    
    /**
     * 接收消息
     */
    public static ConsumerRecords<String, String> receiveMessage(String brokerList, String topic,
        String groupId, Long timeout) {
        
        long setTimeout = 100;
        if (timeout != null) {
            setTimeout = timeout;
        }
        
        KafkaConsumer<String, String> kafkaConsumer = initConsumerParams(brokerList,
            topic, groupId);
        ConsumerRecords<String, String> poll = kafkaConsumer.poll(setTimeout);
        return poll;
    }
    
/*    public static void main(String[] args) {
        String message1 = "{\"RunTime\": \"运行时间\",\"data\": [{\"clusterIndex\": \"1\",\"uuid\": \"1\",\"clusterFlag\": \"true\",\"combineCluster\": [\"1\",\"2\",\"3\"]}]}";
        String message2 = "{\"RunTime\": \"运行时间\",\"data\": [{\"clusterIndex\": \"3\",\"uuid\": \"3\",\"clusterFlag\": \"true\",\"combineCluster\": [\"3\"]}]}";
        String message3 = "{\"RunTime\": \"运行时间\",\"data\": [{\"clusterIndex\": \"2\",\"uuid\": \"2\",\"clusterFlag\": \"false\",\"combineCluster\": [\"2\"]}]}";
        
        
        KafkaUtil.sendMessage("fuhao1","1",message1,"172.16.1.20:39092");
        KafkaUtil.sendMessage("fuhao1","1",message2,"172.16.1.20:39092");
        KafkaUtil.sendMessage("fuhao1","1",message3,"172.16.1.20:39092");
    
    }*/
    
}
