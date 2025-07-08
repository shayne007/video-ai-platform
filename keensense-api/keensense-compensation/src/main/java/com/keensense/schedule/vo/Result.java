package com.keensense.schedule.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Table(name = "bike_result,objext_result,vlpr_result,face_result")
@ToString(callSuper = true, includeFieldNames = true)
public class Result {
    
    //主键
    @Id
    @JSONField(name = "Id")
    @Column(name = "id")
    protected String id;
    
    /**
     * 任务序列号
     */
    @JSONField(name = "Serialnumber")
    @Column(name = "serialnumber")
    protected String serialnumber;
    
    /**
     * 位置标记时间：人工采集时有效
     */
    @JSONField(name = "MarkTime", format = "yyyyMMddHHmmss")
    @Column(name = "marktime")
    protected Date markTime;
    
    /**
     * 目标出现时间：人工采集时有效
     */
    @JSONField(name = "AppearTime", format = "yyyyMMddHHmmss")
    @Column(name = "appeartime")
    protected Date appearTime;
    
    /**
     * 目标消失时间：人工采集时有效
     */
    @JSONField(name = "DisappearTime", format = "yyyyMMddHHmmss")
    @Column(name = "disappeartime")
    protected Date disappearTime;
    
    /**
     * 信息分类：人工采集还是自动采集
     */
    @JSONField(name = "InfoKind")
    @Column(name = "infokind")
    protected Integer infoKind;
    
    /**
     * 来源标识：来源图像信息标识
     */
    @JSONField(name = "SourceID")
    @Column(name = "sourceid")
    protected String sourceId;
    
    /**
     * 设备编码：设备编码，自动采集必选
     */
    @JSONField(name = "DeviceID")
    @Column(name = "deviceid")
    protected String deviceId;
    
    /**
     * 左上角 X 坐标
     */
    @JSONField(name = "LeftTopX")
    @Column(name = "lefttopx")
    protected Integer leftTopX;
    
    /**
     * 左上角 Y 坐标
     */
    @JSONField(name = "LeftTopY")
    @Column(name = "lefttopy")
    protected Integer leftTopY;
    
    /**
     * 右下角 X 坐标
     */
    @JSONField(name = "RightBtmX")
    @Column(name = "rightbtmx")
    protected Integer rightBtmX;
    
    /**
     * 右下角 Y 坐标
     */
    @JSONField(name = "RightBtmY")
    @Column(name = "rightbtmy")
    protected Integer rightBtmY;
    
    /**
     * 离线视频，并且物体唯一id为0时填充该值为1
     */
    @JSONField(name = "FirstObj")
    @Column(name = "firstobj")
    protected Integer firstObj;
    
    /**
     * 记录分析的slave的IP地址
     */
    @JSONField(name = "SlaveIp")
    @Column(name = "slaveip")
    protected String slaveIp;
    
    /**
     * 图片在视频中的序号
     */
    @JSONField(name = "ObjId")
    @Column(name = "objid")
    protected Integer objId;
    
    /**
     * 目标出现的帧序号
     */
    @JSONField(name = "StartFrameIdx")
    @Column(name = "startframeidx")
    protected Long startFrameidx;
    
    /**
     * 目标消失的帧序号
     */
    @JSONField(name = "EndFrameIdx")
    @Column(name = "endframeidx")
    protected Long endFrameidx;
    
    /**
     * 目标出现的帧序号
     */
    @JSONField(name = "StartFramePts")
//    @Column(name = "startframepts")
    protected Long startFramePts;
    
    /**
     * 结果入库时间
     */
    @JSONField(name = "InsertTime", format = "yyyyMMddHHmmss")
    @Column(name = "inserttime")
    protected Date insertTime;
    
    /**
     * 截取代表帧的帧序
     */
    @JSONField(name = "FrameIdx")
    @Column(name = "frameidx")
    protected Long frameIdx;
    
    //快照生成的时间戳
    @JSONField(name = "FramePts")
    @Column(name = "framepts")
    protected Long framePts;
    
    /**
     * 图片特征数据流
     */
    @JSONField(name = "FeatureObject")
    protected String featureObject;
    
    @JSONField(name = "Firm")
    protected Integer firm;
    
    /**
     * 图像列表：可以包含 0 个或者多个子图像对象
     */
    @JSONField(name = "SubImageList")
    @Column(name = "subimagelist")
    protected String subImageList;
    
    /**
     * 截图地址
     */
    @JSONField(name = "ImgUrl")
    @Column(name = "imgurl")
    protected String imgUrl;
    
    /**
     * 背景图地址
     */
    @JSONField(name = "BigImgUrl")
    @Column(name = "bigimgurl")
    protected String bigImgUrl;
    
    @JSONField(name = "ObjType")
    @Column(name = "objtype")
    protected Integer objType;
    
    /**
     * sdk返回目标的时间
     */
    @JSONField(name = "CreateTime", format = "yyyyMMddHHmmss")
    @Column(name = "createtime")
    protected Date createTime;
    
    /**
     * 人脸质量，取值范围[0,1]，1代表最高质量
     */
    @JSONField(name = "FaceQuality")
    @Column(name = "facequality")
    protected Float faceQuality = 1.0f;
    
    /**
     * 人形质量，取值范围[0,1]，1代表最高质量
     */
    @JSONField(name = "BodyQuality")
    @Column(name = "bodyquality")
    protected Float bodyQuality = 1.0f;
    
    /**
     *
     */
    @JSONField(name = "AssistSnapshot")
    @Column(name = "assistsnapshot")
    protected String assistSnapshot;
    
    /**
     * 特征存储所对应的IP
     */
    @JSONField(name = "Ip")
    @Column(name = "ip")
    protected String ip;
    
    /**
     * 特征存储所对应的IP
     */
    @JSONField(name = "AnalysisId")
    @Column(name = "analysisid")
    protected String analysisId;
    
    /**
     * 轨迹的url
     */
    @JSONField(name = "Traces")
    @Column(name = "traces")
    protected String traces;
    
}
