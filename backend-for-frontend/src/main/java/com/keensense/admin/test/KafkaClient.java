package com.keensense.admin.test;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.lang.Thread.sleep;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/9/25 14:48
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class KafkaClient {

    public KafkaClient(){
        init();
    }

    private KafkaConsumer<String, String> consumer;

    public boolean init() {
        Properties props = new Properties();
        props.put("bootstrap.servers", KafkaSendTest.IP);
        props.put("group.id", KafkaSendTest.GROUP_ID);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //1.创建消费者
        consumer = new KafkaConsumer<>(props);
        List<String> subscribedTopics = new ArrayList<>();
        subscribedTopics.add(KafkaSendTest.FACE_TOPIC);
        consumer.subscribe(subscribedTopics);
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        KafkaClient kafkaClient = new KafkaClient();
        while (true){
            ConsumerRecords<String, String> records = kafkaClient.consumer.poll(100);
            if(records.isEmpty()){
                sleep(1000);
            }else
                System.out.println("获取数据条数：" + records.count());
        }
    }
}
