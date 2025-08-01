package com.keensense.admin.picturestream.input.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.keensense.admin.picturestream.entity.PictureInfo;
import com.keensense.admin.picturestream.input.IPictureStream;
import com.keensense.admin.util.IDUtil;
import com.loocme.sys.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class PictureStreamKafkaImpl implements IPictureStream {

    private KafkaConsumer<String, String> consumer;

    @Override
    public boolean init() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "172.16.1.29:39092");
        props.put("group.id", "JdFaceCjQst");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //1.创建消费者
        consumer = new KafkaConsumer<>(props);
        log.info("启动接收kafka数据线程....成功");
        String topic = "dag_face_analysis";
        List<String> subscribedTopics = new ArrayList<>();
        subscribedTopics.add(topic);
        consumer.subscribe(subscribedTopics);
        return true;
    }

    @Override
    public List<PictureInfo> loadPictureRecords() {
        ConsumerRecords<String, String> records = consumer.poll(100);
        if (records.isEmpty()) {
            return Collections.emptyList();
        }
        List<PictureInfo> pictureList = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            if (kafkaMessage.isPresent()) {
                String message = (String) kafkaMessage.get();
                PictureInfo pictureInfo = handleMessage(message);
                pictureList.add(pictureInfo);
            }
        }
        return pictureList;
    }

    private PictureInfo handleMessage(String message) {
        JSONObject jsonObject = JSON.parseObject(message);
        PictureInfo pictureInfo = new PictureInfo();
        pictureInfo.setId(IDUtil.uuid());
        pictureInfo.setDeviceId(jsonObject.getString("device_id"));
        pictureInfo.setPicUrl(jsonObject.getString("frame_img"));
        //pictureInfo.setSerialNumber(jsonObject.getString("serial_number"));
        pictureInfo.setPictureType(jsonObject.getInteger("picture_type"));
        pictureInfo.setExt(jsonObject.getString("ext"));//透传
        pictureInfo.setInterceptFlag(jsonObject.getBoolean("intercept_flag"));//url,base64
        pictureInfo.setKeensenseFlag(jsonObject.getBoolean("keensense_flag"));//false
        pictureInfo.setEnterTime(DateUtil.getDate(jsonObject.getString("enter_time")).getTime());//进入时间
        pictureInfo.setCaptureTime(DateUtil.getDate(jsonObject.getString("capture_time")).getTime());//抓怕时间
        pictureInfo.setLeaveTime(DateUtil.getDate(jsonObject.getString("leave_time")).getTime());//离开时间
        pictureInfo.addRecogTypeList(jsonObject.getString("recog_type"));//2
        return pictureInfo;
    }
}
