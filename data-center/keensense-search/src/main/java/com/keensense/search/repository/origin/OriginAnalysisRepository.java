package com.keensense.search.repository.origin;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.repository.ImageAnalysisRepository;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;

/**
 * Created by zhanx xiaohui on 2019-08-08.
 */
@Repository
@RefreshScope
@Slf4j
public class OriginAnalysisRepository implements ImageAnalysisRepository {

    @Value("${origin.kafka.image.analysis.brokerList}")
    private String brokerList;
    @Value("${origin.kafka.image.analysis.topic}")
    private String topic;

    private Producer producer;

    public void init() {
        synchronized (this) {
            if (producer == null) {
                Properties properties = new Properties();
                properties.put("bootstrap.servers", brokerList);
                properties
                    .put("key.serializer",
                        "org.apache.kafka.common.serialization.StringSerializer");
                properties
                    .put("value.serializer",
                        "org.apache.kafka.common.serialization.StringSerializer");
                properties.put("acks", "all");
                properties.put("retries", 3);
                properties.put("batch.size", 16384);
                properties.put("linger.ms", 100);
                properties.put("buffer.memory", 33554432);

                try {
                    producer = new KafkaProducer<String, String>(properties);
                } catch (Exception e) {
                    log.error("can not connect to kafka.", e);
                }
            }
        }
    }

    @Override
    public void sendRequest(JSONObject object) {
        init();
        producer.send(new ProducerRecord(topic, object.toJSONString() + "\n"));
    }
}

/**
 * @program: platform
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-08-08 11:28
 **/