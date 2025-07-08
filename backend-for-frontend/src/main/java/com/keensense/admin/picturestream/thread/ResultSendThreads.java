package com.keensense.admin.picturestream.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.picturestream.entity.PictureInfo;
import com.keensense.admin.picturestream.output.IResultSend;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.util.DbPropUtil;
import com.keensense.admin.util.IDUtil;
import com.keensense.admin.util.StringUtils;
import com.keensense.common.config.SpringContext;
import com.keensense.common.platform.constant.WebserviceConstant;
import com.keensense.common.util.HttpU2sGetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class ResultSendThreads implements Runnable {
    private static IVsdTaskRelationService vsdTaskRelationService = SpringContext.getBean(IVsdTaskRelationService.class);
    private static ThreadUtil.ExecutorService service = null;
    private static IResultSend resultSendImpl;
    private static final int SEND_NUMBER = 100;
    private static int capacity = 3;
    private int type;
    private static KafkaConsumer<String, String> consumer;

    public ResultSendThreads() {
        service.execute(new ResultSendThreads(PictureInfo.QUEUE_OBJEXT));
    }

    public ResultSendThreads(int type) {
        this.type = type;
    }

    public static void initCapacity() {
        service = ThreadUtil.newFixedThreadPool(capacity);
        // 初始化数据来源实现方式
        Properties props = new Properties();
        props.put("bootstrap.servers", DbPropUtil.getString("kafka.url", "127.0.0.1:39092"));
        props.put("group.id", "test11");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //1.创建消费者
        consumer = new KafkaConsumer<>(props);
        log.info("启动接收kafka数据线程....成功");
        String topic = "test11";
        List<String> subscribedTopics = new ArrayList<>();
        subscribedTopics.add(topic);
        consumer.subscribe(subscribedTopics);
    }

    @Override
    public void run() {
        Thread.currentThread().setName(IDUtil.threadName("ResultSendThreads_" + type));
        boolean falg = true;
        while (falg) {
            try {
                List<VsdTaskRelation> vsdTaskRelationList = vsdTaskRelationService.list(new QueryWrapper<VsdTaskRelation>().eq("isvalid", 1));
                if (vsdTaskRelationList != null && vsdTaskRelationList.size() > 0) {
                    ConsumerRecords<String, String> records = consumer.poll(100);
                    for (ConsumerRecord<String, String> record : records) {
                        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
                        if (kafkaMessage.isPresent()) {
                            String message = (String) kafkaMessage.get();
                            JSONObject jsonObject = JSON.parseObject(message);
                            for (VsdTaskRelation vsdTaskRelation : vsdTaskRelationList) {
                                String deviceId = jsonObject.getString("device_id");
                                if (StringUtils.isNotEmptyString(vsdTaskRelation.getCameraId()) && vsdTaskRelation.getCameraId().equals(deviceId)) {
                                    jsonObject.put("serial_number", vsdTaskRelation.getSerialnumber());
                                    if (!jsonObject.isEmpty()) {
                                        //KafkaUtil.sendMessage("dag_face_analysis","JdFaceCjQst", jsonObject.toJSONString(),DbPropUtil.getString("kafka.url","127.0.0.1:39092"));
                                        //String requestUrl = "http://127.0.0.1:8890/rest/picturestream/loadPicture";
                                        String requestUrl = DbPropUtil.getString("picturestream.url", "127.0.0.1:8890");
                                        requestUrl += WebserviceConstant.LOAD_PICTURE_STREAM;
                                        String resultString = HttpU2sGetUtil.postContent(requestUrl, jsonObject.toJSONString());
                                        JSONObject parseObject = JSONObject.parseObject(resultString);
                                        String ret = parseObject.getString("ret");
                                        if (StringUtils.isEmptyString(ret) || !ret.equals("0")) {
                                            log.info("调用pictureStream处理图片数据失败:" + jsonObject.toString());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                log.error("PictureQueueTake error ,type=" + type, e);
            }
        }
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
        pictureInfo.setCaptureTime(DateUtil.getDate(jsonObject.getString("capture_time")).getTime());//抓拍时间
        pictureInfo.setLeaveTime(DateUtil.getDate(jsonObject.getString("leave_time")).getTime());//离开时间
        pictureInfo.addRecogTypeList(jsonObject.getString("recog_type"));//2
        return pictureInfo;
    }

}
