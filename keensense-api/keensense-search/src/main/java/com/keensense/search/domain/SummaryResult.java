package com.keensense.search.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zhanx xiaohui on 2019-05-08.
 */
@Data
@Table(name = "summary_result")
@ToString(callSuper=true, includeFieldNames=true)
public class SummaryResult {
    @Id
    @JSONField(name= "Id")
    @Column(name = "id")
    private String id;

    @JSONField(name= "Serialnumber")
    @Column(name = "serialnumber")
    private String serialnumber;

    @JSONField(name= "AnalysisId")
    @Column(name = "analysisid")
    private String analysisId;

    @JSONField(name= "CameraId")
    @Column(name = "cameraid")
    private Long cameraId;

    @JSONField(name= "ImgUrl")
    @Column(name = "imgurl")
    private String imgUrl;

    @JSONField(name= "BigImgUrl")
    @Column(name = "bigimgurl")
    private String bigImgUrl;

    @JSONField(name= "ObjType")
    @Column(name = "objtype")
    private Long objType;

    @JSONField(name= "TubeId")
    @Column(name = "tubeid")
    private Long tubeId;

    @JSONField(name= "ObjId")
    @Column(name = "objid")
    private Integer objId;

    @JSONField(name= "StartframeIdx")
    @Column(name = "startframeidx")
    private String startframeIdx;

    @JSONField(name= "EndframeIdx")
    @Column(name = "endframeidx")
    private Long endframeIdx;

    @JSONField(name= "StartframePts")
    @Column(name = "startframepts")
    private Long startframePts;

    @JSONField(name= "EndframePts")
    @Column(name = "endframepts")
    private Long endframePts;

    @JSONField(name= "FrameIdx")
    @Column(name = "frameidx")
    private Long frameIdx;

    @JSONField(name= "Width")
    @Column(name = "width")
    private Long width;

    @JSONField(name= "Height")
    @Column(name = "height")
    private String height;

    @JSONField(name= "X")
    @Column(name = "x")
    private String x;

    @JSONField(name= "Y")
    @Column(name = "y")
    private String y;

    @JSONField(name= "W")
    @Column(name = "w")
    private String w;

    @JSONField(name= "H")
    @Column(name = "h")
    private String h;

    @JSONField(name= "Distance")
    @Column(name = "distance")
    private String distance;


    @JSONField(name= "CreateTime", format = "YYYYMMDDhhmmss")
    @Column(name = "createtime")
    private Date createTime;

    @JSONField(name= "InsertTime", format = "YYYYMMDDhhmmss")
    @Column(name = "inserttime")
    private Date insertTime;

    @JSONField(name= "SlaveIp")
    @Column(name = "slaveip")
    private String slaveIp;

    @JSONField(name= "Xywh")
    @Column(name = "xywh")
    private String xywh;

    @JSONField(name= "FaceImgUrl")
    @Column(name = "faceImgurl")
    private String faceImgUrl;

    @JSONField(name= "TaskId")
    @Column(name = "taskid")
    private String taskId;

    @JSONField(name= "FramePts")
    @Column(name = "framepts")
    private Long framePts;
}

/**
 * @program: data
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-05-08 09:13
 **/