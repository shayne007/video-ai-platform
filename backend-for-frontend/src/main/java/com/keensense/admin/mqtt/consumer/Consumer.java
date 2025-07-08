package com.keensense.admin.mqtt.consumer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.entity.task.CtrlUnit;
import com.keensense.admin.mapper.task.CameraMapper;
import com.keensense.admin.mapper.task.CtrlUnitMapper;
import com.keensense.admin.mqtt.ServerMQTT;
import com.keensense.admin.mqtt.config.MqttConfig;
import com.keensense.admin.mqtt.domain.Community;
import com.keensense.admin.mqtt.domain.FaceResult;
import com.keensense.admin.mqtt.domain.MessageSource;
import com.keensense.admin.mqtt.domain.NonMotorVehiclesResult;
import com.keensense.admin.mqtt.domain.PersonResult;
import com.keensense.admin.mqtt.domain.VlprResult;
import com.keensense.admin.mqtt.enums.ResultEnums;
import com.keensense.admin.mqtt.service.impl.BikeConvertService;
import com.keensense.admin.mqtt.service.impl.CarConvertService;
import com.keensense.admin.mqtt.service.impl.FaceConvertService;
import com.keensense.admin.mqtt.service.impl.PersonConvertService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class Consumer {

    private static final String TOPIC = "objext";

    private static final String PARTITION_0 = "0";

    private static final String PARTITION_1 = "1";

    private static final String PARTITION_2 = "2";

    private static final String PARTITION_3 = "3";

    @Resource
    private PersonConvertService personConvertService;

    @Resource
    private FaceConvertService faceConvertService;

    @Resource
    private CarConvertService carConvertService;

    @Resource
    private BikeConvertService bikeConvertService;

    @Autowired
    private MqttConfig mqttConfig;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private CameraMapper cameraMapper;

    @Resource
    private CtrlUnitMapper ctrlUnitMapper;

    private static final Long KAKOU_TIME = 60L * 60L * 1L;

    /**
     * Kafka监听结构化数据 分区 0
     */
    @KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = {PARTITION_0})})
    public void consumerPartition0(List<ConsumerRecord> consumerRecords) {
        handleMessage(consumerRecords, PARTITION_0);
    }

    /**
     * Kafka监听结构化数据 分区 1
     */
    @KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = {PARTITION_1})})
    public void consumerPartition1(List<ConsumerRecord> consumerRecords) {
        handleMessage(consumerRecords, PARTITION_1);
    }

    /**
     * Kafka监听结构化数据 分区 2
     */
    @KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = {PARTITION_2})})
    public void consumerPartition2(List<ConsumerRecord> consumerRecords) {
        handleMessage(consumerRecords, PARTITION_2);
    }

    /**
     * Kafka监听结构化数据 分区 3
     */
    @KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = {PARTITION_3})})
    public void consumerPartition3(List<ConsumerRecord> consumerRecords) {
        handleMessage(consumerRecords, PARTITION_3);
    }

    /**
     * 处理消息
     */
    private void handleMessage(List<ConsumerRecord> consumerRecords, String partition) {
        Long startTime = System.currentTimeMillis();
        log.info("分区{}收到消息时间:{},消息数量:{},线程名:{}", partition, startTime, consumerRecords.size(), Thread.currentThread().getName());
        if (StringUtils.isEmpty(mqttConfig.getGbStandard())){
            handleGeneralStandardMessage(consumerRecords);
        }else {
            // 批量发送
            JSONObject outerPersonAndFaceObject = new JSONObject();
            JSONObject outerCarObject = new JSONObject();
            JSONObject outerBikeObject = new JSONObject();

            JSONObject innerPersonObject = new JSONObject();
            JSONObject innerFaceObject = new JSONObject();
            JSONObject innerCarObject = new JSONObject();
            JSONObject innerBikeObject = new JSONObject();

            JSONArray personArray = new JSONArray();
            JSONArray faceArray = new JSONArray();
            JSONArray carArray = new JSONArray();
            JSONArray bikeArray = new JSONArray();

            for (ConsumerRecord consumerRecord : consumerRecords) {
                Optional<?> kafkaMessage = Optional.ofNullable(consumerRecord.value());
                if (kafkaMessage.isPresent()) {
                    String message = (String) kafkaMessage.get();
                    log.debug("分区{}消费消息:{}", partition, message);
                    // 转换数据为1400标准
                    try {
                        convertDataToGB1400(message, personArray, faceArray, carArray, bikeArray);
                    } catch (Exception e) {
                        log.error("error message:", e);
                    }
                }
            }

            // 封装Object数据
            getObjectParams(outerPersonAndFaceObject, innerPersonObject, personArray,
                    ResultEnums.PERSON_LIST_OBJECT_NAME.getValue(), ResultEnums.PERSON_OBJECT_NAME.getValue());

            getObjectParams(outerPersonAndFaceObject, innerFaceObject, faceArray,
                    ResultEnums.FACE_LIST_OBJECT_NAME.getValue(), ResultEnums.FACE_OBJECT_NAME.getValue());

            getObjectParams(outerCarObject, innerCarObject, carArray,
                    ResultEnums.MOTORVEHICLE_LIST_OBJECT_NAME.getValue(), ResultEnums.MOTORVEHICLE_OBJECT_NAME.getValue());

            getObjectParams(outerBikeObject, innerBikeObject, bikeArray,
                    ResultEnums.NONMOTORVEHICLE_LIST_OBJECT_NAME.getValue(), ResultEnums.NONMOTORVEHICLE_OBJECT_NAME.getValue());

            // 批量发送至视图库接口
            try {
                if (outerPersonAndFaceObject.size() > 0){
                    log.debug("发往视图库前 人形和人脸数据:=====>{}",outerPersonAndFaceObject.toJSONString());
                    ServerMQTT server = new ServerMQTT(mqttConfig);
                    server.setmessage(server, outerPersonAndFaceObject.toJSONString());
                }

                if (outerCarObject.size() > 0) {
                    log.debug("发往视图库前 机动车数据:=====>{}",outerCarObject.toJSONString());
                    ServerMQTT server = new ServerMQTT(mqttConfig);
                    server.setmessage(server, outerCarObject.toJSONString());
                }

                if (outerBikeObject.size() > 0) {
                    log.debug("发往视图库前 非机动车数据:=====>{}",outerBikeObject.toJSONString());
                    ServerMQTT server = new ServerMQTT(mqttConfig);
                    server.setmessage(server, outerBikeObject.toJSONString());
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }

        Long endTime = System.currentTimeMillis();
        log.info("分区{}消息转化消耗时间：{}", partition, endTime - startTime);
    }

    /**
     * 转换数据为GB1400标准
     */
    private void convertDataToGB1400(String message, JSONArray personArray, JSONArray faceArray, JSONArray carArray, JSONArray bikeArray) {
        JSONObject dataObject = JSONObject.parseObject(message);
        String serialNumber = String.valueOf(dataObject.get("serialNumber"));
        log.info("----任务号serialNumber:{}----", serialNumber);

        PersonResult person = null;
        VlprResult car = null;
        NonMotorVehiclesResult bike = null;
        FaceResult personFace = null;

        Integer objType = (Integer) dataObject.get("objType");

        switch (objType) {
            case 1:
                // 人
                person = new PersonResult();
                personConvertService.dataConvert(dataObject, person);
                personFace = new FaceResult();
                faceConvertService.dataConvert(dataObject, personFace);
                // 设置人脸关联人形ID personID
                personFace.setConnectObjectId(person.getPersonID());
                // 设置人形关联人脸ID
                person.setFaceUUID(personFace.getFaceID());
                break;
            case 2:
                // 车
                car = new VlprResult();
                carConvertService.dataConvert(dataObject, car);
                break;
            case 4:
                // 人骑车
                bike = new NonMotorVehiclesResult();
                bikeConvertService.dataConvert(dataObject, bike);
                break;
            default:
                break;
        }

        // 封装数据
        packageData(serialNumber, person, car, bike, personFace, personArray, faceArray, carArray, bikeArray);

    }

    /**
     * 封装Object参数
     */
    private void getObjectParams(JSONObject outerObject, JSONObject innerObject, JSONArray jsonArray, String innerObjectName, String outerObjectName) {
        if(jsonArray.size() > 0){
            innerObject.put(innerObjectName, jsonArray);
            outerObject.put(outerObjectName, innerObject);
        }
    }

    /**
     * 封装Array参数
     */
    @SuppressWarnings("all")
    private void packageData(String serialNumber, PersonResult person, VlprResult car, NonMotorVehiclesResult bike, FaceResult personFace,
                             JSONArray personArray, JSONArray faceArray, JSONArray carArray, JSONArray bikeArray) {
        if (!Objects.isNull(person)) {
            // 人形数据
            personArray.add(person);
            if (!StringUtils.isEmpty(personFace.getFaceUrl())) {
                // 有人脸数据
                faceArray.add(personFace);
            }
        }

        if (!Objects.isNull(car)) {
            // 机动车数据
            carArray.add(car);
        }

        if (!Objects.isNull(bike)) {
            // 非机动车数据
            bikeArray.add(bike);
        }
    }

    private void handleGeneralStandardMessage(List<ConsumerRecord> consumerRecords) {
        JSONObject resultJSONObject = new JSONObject();
        for (ConsumerRecord consumerRecord : consumerRecords) {
            Optional<?> kafkaMessage = Optional.ofNullable(consumerRecord.value());
            if (kafkaMessage.isPresent()) {
                String message = (String) kafkaMessage.get();
                try {
                    JSONObject dataObject = JSONObject.parseObject(message);
                    resultJSONObject.put("payload",dataObject);
                    JSONObject customJSONObject = dataObject.getJSONObject("custom");
                    String cameraId = customJSONObject.get("cameraId").toString();
                    if(StringUtils.isEmpty(cameraId)){
                        cameraId = "1";
                    }
                    //同步点位,区域字段
                    Map<String,Object> resultMap = (Map<String, Object>)  redisTemplate.opsForValue().get("kakou_" + cameraId);
                    if(Objects.isNull(resultMap) || resultMap.size() == 0){
                        resultMap = new HashMap<>();
                        Camera camera = cameraMapper.selectById(cameraId);
                        if (camera != null){
                            CtrlUnit ctrlUnit = ctrlUnitMapper.selectOne(new QueryWrapper<CtrlUnit>().eq("unit_identity", camera.getRegion()));
                            if (ctrlUnit != null) {
                                Community community = new Community();
                                community.setId(ctrlUnit.getId().toString());
                                community.setName(ctrlUnit.getUnitName());
                                resultMap.put("community",JSONObject.toJSONString(community));
                            }
                            MessageSource messageSource = new MessageSource();
                            messageSource.setEquipmentId(camera.getId().toString());
                            messageSource.setEquipmentName(camera.getName());
                            messageSource.setLat(camera.getLatitude());
                            messageSource.setLng(camera.getLongitude());
                            resultMap.put("messageSource",JSONObject.toJSONString(messageSource));
                            // 存入缓存
                            redisTemplate.opsForValue().set("kakou_" + cameraId, resultMap, KAKOU_TIME, TimeUnit.SECONDS);
                        }

                    }
                    String communityStr = resultMap.get("community").toString();
                    Community community = JSONObject.parseObject(communityStr,Community.class);
                    MessageSource messageSource = JSONObject.parseObject(resultMap.get("messageSource").toString(), MessageSource.class);
                    resultJSONObject.put("messageSource",messageSource);
                    SimpleDateFormat idFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
                    resultJSONObject.put("sequenceNo",idFormat.format(new Date()));
                    resultJSONObject.put("packageNo",idFormat.format(new Date()));
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+08:00");
                    resultJSONObject.put("messagetTime",sf.format(new Date()));
                    resultJSONObject.put("community",community);
                    Integer objType = (Integer) dataObject.get("objType");
                    resultJSONObject.put("messageKind",getMessageKind(objType));//获取消息类型
                    // 推送mqtt消息
                    try {
                        if (resultJSONObject.size() > 0){
                            ServerMQTT server = new ServerMQTT(mqttConfig);
                            server.setmessage(server, resultJSONObject.toJSONString());
                        }
                    } catch (Exception e) {
                        log.error("", e);
                    }
                } catch (Exception e) {
                    log.error("error message:", e);
                }
            }
        }
    }

    private String getMessageKind(Integer messageKind){
        switch (messageKind){
            case 1:
                return "102";
            case 2:
                return "101";
            case 4:
                return "104";
            default:
                return "102";
        }
    }
}
