package com.keensense.picturestream;/**
 * Created by zhanx xiaohui on 2019/7/8.
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/7/8 16:59
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class KafkaSendTest {
    
    private static final SimpleDateFormat DATE_SDF = new SimpleDateFormat("yyyyMMddHHmmss");
    
    private static final String dataStr = DATE_SDF.format(new Date());
    
    private static final String JSON = "{\n"
        + "\"face_id\":\"http://192.168.0.78:8082/20190709/15/bg/vehicle_d9a547896d624d4fbddc13e660129d70-000320_st221560_end222520.jpg\",\n"
        + "\"gateway_id\":\"44060650001310079\",\n"
        + "\"location_id\":\"987654321\",\n"
        + "\"device_id\":\"44060650001310079\",\n"
        + "\"capture_time\":\""+dataStr+"\",\n"
        + "\"enter_time\":\""+dataStr+"\",\n"
        + "\"leave_time\":\""+dataStr+"\",\n"
        + "\"track_id\":\"7894658287\",\n"
        + "\"face_img\":\"http://192.168.0.78:8082/20190709/15/bg/vehicle_d9a547896d624d4fbddc13e660129d70-000320_st221560_end222520.jpg\",\n"
        + "\"frame_img\":\"http://172.16.1.29:8082/20190803/08/thumb/human_1225251144-045785_st1564792557581_end1564792559868_thumb.jpg\",\n"
        + "\"recv_capture_time\":190619200430767,\n"
        + "\"send_capture_time\":190619200430778,\n"
        + "\"face_rect\":{\n"
        + "\"x\":540,\n"
        + "\"y\":370,\n"
        + "\"width\":42,\n"
        + "\"height\":77\n"
        + "},\n"
        + "\"sex\":0,\n"
        + "\"age\":0,\n"
        + "\"with_glasses\":0,\n"
        + "\"ext\":{\"ext1\":11111},\n"
        + "\"serial_number\":1231,\n"
        + "\"picture_type\":1,\n"
        + "\"intercept_flag\":false,\n"
        + "\"keensense_flag\":true,\n"
        + "\"recog_type\":8\n"
        + "}";

    private static final String FACE_TOPIC = "dag_face_analysis";

    private static final String IP = "172.16.1.29:39092";

    private KafkaProducer<String, String> producer;

    public void initParams() {
        Properties props = new Properties();
        props.put("bootstrap.servers", IP);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all");
        props.put("retries", 3);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "JdFaceCjQst");
        producer = new KafkaProducer<>(props);
    }

    public void sendMessage(String message) {
        producer.send(new ProducerRecord<>(FACE_TOPIC, message + "\0"));
        producer.flush();
    }

//   public static void main(String[] args) {
//        KafkaSendTest kafkaSendTest = new KafkaSendTest();
//        kafkaSendTest.initParams();
//        for (int i=0;i<20;i++){
//
//            kafkaSendTest.sendMessage(JSON.replaceAll("\n", ""));
//        }
//    }
}

