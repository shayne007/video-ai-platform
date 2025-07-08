package com.keensense.admin.controller.common;

import com.alibaba.fastjson.JSONArray;
import com.keensense.admin.util.DbPropUtil;
import com.keensense.common.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@Api(tags = "模拟推送kafka数据")
@RestController
@Slf4j
@RequestMapping("/pushData")
public class TestKafkaController {

    public static final String FACE_TOPIC = "test11";

    public static final String GROUP_ID = "test11";

    private KafkaProducer<String, String> producer;

    @ApiOperation(value = "模拟推送kafka数据")
    @PostMapping(value = "/pushKafkaData")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsonData", value = "json数据"),
    })
    public R pushKafkaData(@RequestBody String jsonData) {
        R result = R.ok();
        initParams();
        JSONArray jsonArray = JSONArray.parseArray(jsonData);
        for (int i = 0; i < jsonArray.size(); i++) {
            sendMessage(jsonArray.getString(i).replaceAll("\n", ""));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void initParams() {
        Properties props = new Properties();
        props.put("bootstrap.servers",  DbPropUtil.getString("kafka.url", "127.0.0.1:39092"));
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all");
        props.put("retries", 3);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        producer = new KafkaProducer<>(props);
    }

    public void sendMessage(String message) {
        producer.send(new ProducerRecord<>(FACE_TOPIC, message + "\0"));
        producer.flush();
    }
}
