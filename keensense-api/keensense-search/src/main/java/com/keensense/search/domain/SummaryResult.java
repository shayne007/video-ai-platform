package com.keensense.search.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.Id;
import com.loocme.sys.annotation.database.Table;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

/**
 * Created by zhanx xiaohui on 2019-05-08.
 */
@Data
@Table(TableName = "summary_result")
@ToString(callSuper=true, includeFieldNames=true)
public class SummaryResult {
    @Id
    @JSONField(name= "Id")
    @Column(ColumnName = "id")
    private String id;

    @JSONField(name= "Serialnumber")
    @Column(ColumnName = "serialnumber")
    private String serialnumber;

    @JSONField(name= "AnalysisId")
    @Column(ColumnName = "analysisid")
    private String analysisId;

    @JSONField(name= "CameraId")
    @Column(ColumnName = "cameraid")
    private Long cameraId;

    @JSONField(name= "ImgUrl")
    @Column(ColumnName = "imgurl")
    private String imgUrl;

    @JSONField(name= "BigImgUrl")
    @Column(ColumnName = "bigimgurl")
    private String bigImgUrl;

    @JSONField(name= "ObjType")
    @Column(ColumnName = "objtype")
    private Long objType;

    @JSONField(name= "TubeId")
    @Column(ColumnName = "tubeid")
    private Long tubeId;

    @JSONField(name= "ObjId")
    @Column(ColumnName = "objid")
    private Integer objId;

    @JSONField(name= "StartframeIdx")
    @Column(ColumnName = "startframeidx")
    private String startframeIdx;

    @JSONField(name= "EndframeIdx")
    @Column(ColumnName = "endframeidx")
    private Long endframeIdx;

    @JSONField(name= "StartframePts")
    @Column(ColumnName = "startframepts")
    private Long startframePts;

    @JSONField(name= "EndframePts")
    @Column(ColumnName = "endframepts")
    private Long endframePts;

    @JSONField(name= "FrameIdx")
    @Column(ColumnName = "frameidx")
    private Long frameIdx;

    @JSONField(name= "Width")
    @Column(ColumnName = "width")
    private Long width;

    @JSONField(name= "Height")
    @Column(ColumnName = "height")
    private String height;

    @JSONField(name= "X")
    @Column(ColumnName = "x")
    private String x;

    @JSONField(name= "Y")
    @Column(ColumnName = "y")
    private String y;

    @JSONField(name= "W")
    @Column(ColumnName = "w")
    private String w;

    @JSONField(name= "H")
    @Column(ColumnName = "h")
    private String h;

    @JSONField(name= "Distance")
    @Column(ColumnName = "distance")
    private String distance;


    @JSONField(name= "CreateTime", format = "YYYYMMDDhhmmss")
    @Column(ColumnName = "createtime")
    private Date createTime;

    @JSONField(name= "InsertTime", format = "YYYYMMDDhhmmss")
    @Column(ColumnName = "inserttime")
    private Date insertTime;

    @JSONField(name= "SlaveIp")
    @Column(ColumnName = "slaveip")
    private String slaveIp;

    @JSONField(name= "Xywh")
    @Column(ColumnName = "xywh")
    private String xywh;

    @JSONField(name= "FaceImgUrl")
    @Column(ColumnName = "faceImgurl")
    private String faceImgUrl;

    @JSONField(name= "TaskId")
    @Column(ColumnName = "taskid")
    private String taskId;

    @JSONField(name= "FramePts")
    @Column(ColumnName = "framepts")
    private Long framePts;
}

/**
 * @program: data
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-05-08 09:13
 **/