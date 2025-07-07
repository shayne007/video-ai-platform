package com.keensense.search.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.Id;
import com.loocme.sys.annotation.database.Table;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

/**
 * Created by zhanx xiaohui on 2019-07-09.
 */
@Data
@Table(TableName = "violation_result")
@ToString(callSuper=true, includeFieldNames=true)
public class ViolationResult {
    //主键
    @Id
    @JSONField(name = "Id")
    @Column(ColumnName = "id")
    private String id;

    //自定义方向
    @JSONField(name = "CustomDirection")
    @Column(ColumnName = "customdirection")
    private String customDirection;

    //位置标识
    @JSONField(name = "LocationId")
    @Column(ColumnName = "locationid")
    private String locationId;

    //是否是高速路口
    @JSONField(name = "IsHighSpeedIntersection")
    @Column(ColumnName = "ishighspeedintersection")
    private Integer isHighSpeedIntersection;

    //车道
    @JSONField(name = "Lane")
    @Column(ColumnName = "lane")
    private String lane;

    //是否右转车道
    @JSONField(name = "LightCarRight")
    @Column(ColumnName = "lightcarright")
    private Integer lightCarRight;

    //是否可以掉头
    @JSONField(name = "IsStopHead")
    @Column(ColumnName = "isstophead")
    private String isStopHead;

    //任务序列号
    @JSONField(name = "Serialnumber")
    @Column(ColumnName = "serialnumber")
    private String serialnumber;

    //当时产生违章的时间
    @JSONField(name = "Datetime", format = "yyyy-MM-dd HH:mm:ss")
    @Column(ColumnName = "datetime")
    private Date datetime;

    //在1天中的时间
    @JSONField(name = "TimeInDay")
    @Column(ColumnName = "timeinday")
    private Integer timeInDay;

    //物体uuid号
    @JSONField(name = "RecordId")
    @Column(ColumnName = "recordid")
    private String recordId;

    //交通违规情况
    @JSONField(name = "ViolationType")
    @Column(ColumnName = "violationtype")
    private String violationType;

    //物体移动方向
    @JSONField(name = "Direction")
    @Column(ColumnName = "direction")
    private Integer direction;

    //物体类型
    @JSONField(name = "ObjType")
    @Column(ColumnName = "objtype")
    private Integer objType;

    //目标出现的帧序号
    @JSONField(name = "StartFrameIdx")
    @Column(ColumnName = "startframeidx")
    private Long startframeIdx;

    //目标消失的帧序号
    @JSONField(name = "EndFrameIdx")
    @Column(ColumnName = "endframeidx")
    private Long endFrameIdx;

    //目标抠图地址
    @JSONField(name = "ImageDetail")
    @Column(ColumnName = "imagedetail")
    private String imageDetail;

    //背景图地址
    @JSONField(name = "Image1")
    @Column(ColumnName = "image1")
    private String image1;

    //
    @JSONField(name = "Image2")
    @Column(ColumnName = "image2")
    private String image2;

    //
    @JSONField(name = "Image3")
    @Column(ColumnName = "image3")
    private String image3;

    //违章视频存储地址
    @JSONField(name = "ViolationVideoPath")
    @Column(ColumnName = "violationvideopath")
    private String violationVideoPath;

    //违章合成图地址
    @JSONField(name = "ImageCompose")
    @Column(ColumnName = "imagecompose")
    private String imageCompose;

    //车牌号码
    @JSONField(name = "PlateLicence")
    @Column(ColumnName = "platelicence")
    private String plateLicence;

    //车牌类型
    @JSONField(name = "PlateClassCode")
    @Column(ColumnName = "plateclasscode")
    private String plateClassCode;

    //车牌颜色
    @JSONField(name = "PlateColorName")
    @Column(ColumnName = "platecolorname")
    private String plateColorName;

    //车辆类型id
    @JSONField(name = "VehicleKindCode")
    @Column(ColumnName = "vehiclekindcode")
    private String vehicleKindCode;

    //车辆类型
    @JSONField(name = "VehicleKind")
    @Column(ColumnName = "vehiclekind")
    private String vehicleKind;

    //主品牌
    @JSONField(name = "VehicleBrand")
    @Column(ColumnName = "vehiclebrand")
    private String vehicleBrand;

    //子品牌
    @JSONField(name = "VehicleSeries")
    @Column(ColumnName = "vehicleseries")
    private String vehicleSeries;

    //年款
    @JSONField(name = "VehicleYear")
    @Column(ColumnName = "vehicleyear")
    private String vehicleYear;

    //车身颜色
    @JSONField(name = "VehicleColorName")
    @Column(ColumnName = "vehiclecolorname")
    private String vehicleColorName;

    //扩展字段1
    @JSONField(name = "ExtInfo1")
    @Column(ColumnName = "extinfo1")
    private String extInfo1;

    //扩展字段2
    @JSONField(name = "ExtInfo2")
    @Column(ColumnName = "extinfo2")
    private String extInfo2;

    //扩展字段3
    @JSONField(name = "ExtInfo3")
    @Column(ColumnName = "extinfo3")
    private String extInfo3;

    /**
     * 结果入库时间
     */
    @JSONField(name= "InsertTime", format = "yyyyMMddHHmmss")
    @Column(ColumnName = "inserttime")
    protected Date insertTime;
}