package com.keensense.search.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.Id;

import com.loocme.sys.annotation.database.Table;
import java.util.Date;
import lombok.Data;
import lombok.ToString;


@Data
@Table(TableName = "bike_result,objext_result,vlpr_result,face_result")
@ToString(callSuper=true, includeFieldNames=true)
public class Result {

    //主键
    @Id
    @JSONField(name = "Id")
    @Column(ColumnName = "id")
    protected String id;

    /**
     * 任务序列号
     */
    @JSONField(name= "Serialnumber")
    @Column(ColumnName = "serialnumber")
    protected String serialnumber;

    /**
     * 位置标记时间：人工采集时有效
     */
    @JSONField(name= "MarkTime", format = "yyyyMMddHHmmss")
    @Column(ColumnName = "marktime")
    protected Date markTime;

    /**
     * 目标出现时间：人工采集时有效
     */
    @JSONField(name= "AppearTime", format = "yyyyMMddHHmmss")
    @Column(ColumnName = "appeartime")
    protected Date appearTime;

    /**
     * 目标消失时间：人工采集时有效
     */
    @JSONField(name= "DisappearTime", format = "yyyyMMddHHmmss")
    @Column(ColumnName = "disappeartime")
    protected Date disappearTime;

    /**
     * 信息分类：人工采集还是自动采集
     */
    @JSONField(name= "InfoKind")
    @Column(ColumnName = "infokind")
    protected Integer infoKind;

    /**
     * 来源标识：来源图像信息标识
     */
    @JSONField(name= "SourceID")
    @Column(ColumnName = "sourceid")
    protected String sourceId;

    /**
     * 设备编码：设备编码，自动采集必选
     */
    @JSONField(name= "DeviceID")
    @Column(ColumnName = "deviceid")
    protected String deviceId;

    /**
     * 左上角 X 坐标
     */
    @JSONField(name= "LeftTopX")
    @Column(ColumnName = "lefttopx")
    protected Integer leftTopX;

    /**
     * 左上角 Y 坐标
     */
    @JSONField(name= "LeftTopY")
    @Column(ColumnName = "lefttopy")
    protected Integer leftTopY;

    /**
     * 右下角 X 坐标
     */
    @JSONField(name= "RightBtmX")
    @Column(ColumnName = "rightbtmx")
    protected Integer rightBtmX;

    /**
     * 右下角 Y 坐标
     */
    @JSONField(name= "RightBtmY")
    @Column(ColumnName = "rightbtmy")
    protected Integer rightBtmY;

    /**
     * 离线视频，并且物体唯一id为0时填充该值为1
     */
    @JSONField(name= "FirstObj")
    @Column(ColumnName = "firstobj")
    protected Integer firstObj;

    /**
     * 记录分析的slave的IP地址
     */
    @JSONField(name= "SlaveIp")
    @Column(ColumnName = "slaveip")
    protected String slaveIp;

    /**
     * 图片在视频中的序号
     */
    @JSONField(name= "ObjId")
    @Column(ColumnName = "objid")
    protected Integer objId;

    /**
     * 目标出现的帧序号
     */
    @JSONField(name= "StartFrameIdx")
    @Column(ColumnName = "startframeidx")
    protected Long startFrameidx;

    /**
     * 目标消失的帧序号
     */
    @JSONField(name= "EndFrameIdx")
    @Column(ColumnName = "endframeidx")
    protected Long endFrameidx;

    /**
     * 目标出现的帧序号
     */
    @JSONField(name= "StartFramePts")
//    @Column(ColumnName = "startframepts")
    protected Long startFramePts;

    /**
     * 结果入库时间
     */
    @JSONField(name= "InsertTime", format = "yyyyMMddHHmmss")
    @Column(ColumnName = "inserttime")
    protected Date insertTime;

    /**
     * 截取代表帧的帧序
     */
    @JSONField(name= "FrameIdx")
    @Column(ColumnName = "frameidx")
    protected Long frameIdx;

    //快照生成的时间戳
    @JSONField(name = "FramePts")
    @Column(ColumnName = "framepts")
    protected Long framePts;

    /**
     * 图片特征数据流
     */
    @JSONField(name= "FeatureObject")
    protected String featureObject;

    @JSONField(name= "Firm")
    protected Integer firm;

    /**
     * 图像列表：可以包含 0 个或者多个子图像对象
     */
    @JSONField(name= "SubImageList")
    @Column(ColumnName = "subimagelist")
    protected String subImageList;

    /**
     * 截图地址
     */
    @JSONField(name= "ImgUrl")
    @Column(ColumnName = "imgurl")
    protected String imgUrl;

    /**
     * 背景图地址
     */
    @JSONField(name= "BigImgUrl")
    @Column(ColumnName = "bigimgurl")
    protected String bigImgUrl;

    @JSONField(name = "ObjType")
    @Column(ColumnName = "objtype")
    protected Integer objType;

    /**
     * sdk返回目标的时间
     */
    @JSONField(name= "CreateTime", format = "yyyyMMddHHmmss")
    @Column(ColumnName = "createtime")
    protected Date createTime;

    /**
     * 人脸质量，取值范围[0,1]，1代表最高质量
     */
    @JSONField(name= "FaceQuality")
    @Column(ColumnName = "facequality")
    protected Float faceQuality = 1.0f;

    /**
     * 人形质量，取值范围[0,1]，1代表最高质量
     */
    @JSONField(name= "BodyQuality")
    @Column(ColumnName = "bodyquality")
    protected Float bodyQuality = 1.0f;

    /**
     *
     */
    @JSONField(name= "AssistSnapshot")
    @Column(ColumnName = "assistsnapshot")
    protected String assistSnapshot;

    /**
     * 特征存储所对应的IP
     */
    @JSONField(name= "Ip")
    @Column(ColumnName = "ip")
    protected String ip;

    /**
     * 特征存储所对应的IP
     */
    @JSONField(name= "AnalysisId")
    @Column(ColumnName = "analysisid")
    protected String analysisId;

    /**
     * 轨迹的url
     */
    @JSONField(name= "Traces")
    @Column(ColumnName = "traces")
    protected String traces;

}
