package com.keensense.picturestream.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;


/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/14 14:54
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Configuration
@Data
@RefreshScope
public class NacosConfig {
    @Value("${face.faceClassPath}")
    private String faceClassPath;
    @Value("${face.faceServiceUrl}")
    private String faceServiceUrl;

    @Value("${face.glStructPort}")
    private String glStructPort;
    @Value("${objext.url}")
    private String objextUrl;

    //业务对接
    @Value("${kafka.bootstrap}")
    private String kafkaBootstrap;

    //qst图片结构化
    @Value("${objext_picture_recog.objext_url}")
    private String objextPictureRecogObjextUrl;
    //GL车辆
    @Value("${vlpr_further_recog.vlpr_url}")
    private String vlprFurtherRecogVlprUrl;
    //QST人脸
    @Value("${face_qst_recog.face_url}")
    private String faceQstRecogFaceUrl;
    //程序设置
    @Value("${download.readTimeout}")
    private int downloadReadTimeout;
    @Value("${capability.queue.objext}")
    private int capabilityQueueObjext;
    @Value("${capability.queue.face}")
    private int capabilityQueueFace;
    @Value("${capability.queue.vlpr}")
    private int capabilityQueueVlpr;
    @Value("${capacity.image.download}")
    private int capabilityImageDownload;
    @Value("${capacity.send.upload}")
    private int capabilitySendUpload;
    @Value("${slave.image.url.local}")
    private String slaveImageUrlLocal;
    @Value("${slave.image.url}")
    private String slaveImageUrl;

    //数据推送(1:kafka 2:视图库 3:同时推送视图库和kafka)
    @Value("${push.data.warehouse}")
    private String pushDataWareHouse;
    //数据推送视图库ip:port
    @Value("${push.data.warehouse.path}")
    private String pushDataWareHousePath;
    //推送jm的kafka
    @Value("${kafka.bootstrap.qstsend}")
    private String kafkaBootstrapQstsend;
    @Value("${kafka.topic.qstsend}")
    private String kafkaTopicQstsend;
    @Value("${kafka.groupId.qstsend}")
    private String kafkaGroupIdQstsend;

}
