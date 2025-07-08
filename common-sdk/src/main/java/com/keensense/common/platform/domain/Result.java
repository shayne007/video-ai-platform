package com.keensense.common.platform.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;


@Data
public class Result {

    //主键
    @JSONField(name = "Id")
    protected String id;

    protected String ids;

    /**
     * 任务序列号
     */
    @JSONField(name = "Serialnumber")
    protected String serialnumber;

    /**
     * 位置标记时间：人工采集时有效
     */
    @JSONField(name = "MarkTime", format = "yyyyMMddHHmmss")
    protected Date markTime;

    /**
     * 目标出现时间：人工采集时有效
     */
    @JSONField(name = "AppearTime", format = "yyyyMMddHHmmss")
    protected Date appearTime;

    /**
     * 目标消失时间：人工采集时有效
     */
    @JSONField(name = "DisappearTime", format = "yyyyMMddHHmmss")
    protected Date disappearTime;

    /**
     * 信息分类：人工采集还是自动采集
     */
    @JSONField(name = "InfoKind")
    protected Long infoKind;

    /**
     * 来源标识：来源图像信息标识
     */
    @JSONField(name = "SourceID")
    protected String sourceId;

    /**
     * 设备编码：设备编码，自动采集必选
     */
    @JSONField(name = "DeviceID")
    protected String deviceId;

    /**
     * 左上角 X 坐标
     */
    @JSONField(name = "LeftTopX")
    protected Integer leftTopX;

    /**
     * 左上角 Y 坐标
     */
    @JSONField(name = "LeftTopY")
    protected Integer leftTopY;

    /**
     * 右下角 X 坐标
     */
    @JSONField(name = "RightBtmX")
    protected Integer rightBtmX;

    /**
     * 右下角 Y 坐标
     */
    @JSONField(name = "RightBtmY")
    protected Integer rightBtmY;

    /**
     * 离线视频，并且物体唯一id为0时填充该值为1
     */
    @JSONField(name = "FirstObj")
    protected Integer firstObj;

    /**
     * 记录分析的slave的IP地址
     */
    @JSONField(name = "SlaveIp")
    protected String slaveIp;

    /**
     * 图片在视频中的序号
     */
    @JSONField(name = "ObjId")
    protected Integer objId;

    /**
     * 目标出现的帧序号
     */
    @JSONField(name = "StartFrameIdx")
    protected Integer startFrameidx;

    /**
     * 目标消失的帧序号
     */
    @JSONField(name = "EndFrameIdx")
    protected Integer endFrameidx;

    /**
     * 结果入库时间
     */
    @JSONField(name = "InsertTime", format = "yyyyMMddHHmmss")
    protected Date insertTime;

    /**
     * 结果入库时间
     */
    protected String insertTimeStart;

    /**
     * 结果入库时间
     */
    protected String insertTimeEnd;
    /**
     * 截取代表帧的帧序
     */
    @JSONField(name = "FrameIdx")
    protected Integer frameIdx;

    //快照生成的时间戳
    @JSONField(name = "FramePts")
    protected Long framePts;

    /**
     * 图片特征数据流
     */
    @JSONField(name = "FeatureObject")
    protected String featureObject;

    /**
     * 图片特征 Bucket ID: -1是没有
     */
    @JSONField(name = "QueryBucketId")
    protected Integer queryBucketId;

    @JSONField(name = "AlgorithmVersion")
    protected String algorithmVersion;

    /**
     * 图像列表：可以包含 0 个或者多个子图像对象
     */
    @JSONField(name = "SubImageList")
    protected String subImageList;

    /**
     * 截图地址
     */
    @JSONField(name = "ImgUrl")
    protected String imgUrl;

    /**
     * 背景图地址
     */
    @JSONField(name = "BigImgUrl")
    protected String bigImgUrl;

    @JSONField(name = "ObjType")
    protected String objType;

    /**
     * sdk返回目标的时间
     */
    @JSONField(name = "CreateTime", format = "yyyyMMddHHmmss")
    protected Date createTime;

    /**
     * 快照图片长度
     */
    @JSONField(name = "Width")
    protected String width;

    /**
     * 快照图片宽度
     */
    @JSONField(name = "Height")
    protected String height;


    /**
     * 特征属性  低8位是否有图片特征数据 高8位是否有人脸特征数据
     */
    @JSONField(name = "FeatureType")
    protected String featureType;

    /**
     * 图片特征数据流（base64编码）(对于结构化分析，此字段为空字符串)
     */
    @JSONField(name = "QueryFeature")
    protected String queryFeature;


    /**
     *
     */
    @JSONField(name = "AssistSnapshot")
    protected String assistSnapshot;

    /**
     * 特征存储所对应的IP
     */
    @JSONField(name = "Ip")
    protected String ip;

    /**
     * 特征存储所对应的IP
     */
    @JSONField(name = "AnalysisId")
    protected String analysisId;

    private String pageNo;

    private String pageSize;

    private String order;

    private String startTime;

    private String endTime;

    private String cameraId;

    private String traces;
}
