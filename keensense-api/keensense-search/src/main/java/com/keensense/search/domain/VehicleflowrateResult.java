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
@Table(TableName = "vehicleflowrate_result")
@ToString(callSuper = true, includeFieldNames = true)
public class VehicleflowrateResult {

    //主键
    @Id
    @JSONField(name = "Id")
    @Column(ColumnName = "id")
    private String id;

    //位置ID
    @JSONField(name = "LocationId")
    @Column(ColumnName = "locationid")
    private String locationId;

    //产生时间
    @JSONField(name = "Datetime", format = "yyyy-MM-dd HH:mm:ss")
    @Column(ColumnName = "datetime")
    private Date datetime;

    //路面平均占用百分点
    @JSONField(name = "Area")
    @Column(ColumnName = "area")
    private Double area;

    //每分钟离开的车辆数
    @JSONField(name = "Leave")
    @Column(ColumnName = "leave")
    private Integer leave;

    //最近一分钟，车道的车辆数
    @JSONField(name = "Stranding")
    @Column(ColumnName = "stranding")
    private Double stranding;

    @JSONField(name = "Image")
    @Column(ColumnName = "image")
    private String image;

    //#########################增加字段

    //监控点id
    @JSONField(name = "CameraId")
    @Column(ColumnName = "cameraid")
    private String cameraId;

    //任务序列号
    @JSONField(name = "Serialnumber")
    @Column(ColumnName = "serialnumber")
    private String serialnumber;

    //图片路径
    @JSONField(name = "ImgUrl")
    @Column(ColumnName = "imgurl")
    private String imgUrl;

    //拥堵级别:0-绿,1-黄,2-红,3-深红,4-黑
    @JSONField(name = "HeavyTrafficLevel")
    @Column(ColumnName = "heavytrafficlevel")
    private Integer heavyTrafficLevel;

    //开始的帧数
    @JSONField(name = "StartFrameIdx")
    @Column(ColumnName = "startframeidx")
    private Long startFrameIdx;

    //结束的帧数
    @JSONField(name = "EndFrameIdx")
    @Column(ColumnName = "endframeidx")
    private Long endFrameIdx;

    //开始的时间戳
    @JSONField(name = "StartFramePts")
    @Column(ColumnName = "startframepts")
    private Long startFramePts;

    //结束的时间戳
    @JSONField(name = "EndFramePts")
    @Column(ColumnName = "endframepts")
    private Long endFramePts;

    //处理详情:0-未处理,1-已处理,2-已忽略,3-自动舒缓
    @JSONField(name = "State")
    @Column(ColumnName = "state")
    private Integer state;

    //车道号
    @JSONField(name = "VehicleLaneIndex")
    @Column(ColumnName = "vehiclelaneindex")
    private Integer vehicleLaneIndex;

    //在N秒内车道上平均过车速度（km/h):-1:没有计算(只限车辆)
    @JSONField(name = "VehicleSpeed")
    @Column(ColumnName = "vehiclespeed")
    private String vehicleSpeed;

    //在N秒内车道上平均排队长度(米)
    @JSONField(name = "LaneQueueLength")
    @Column(ColumnName = "lanequeuelength")
    private Integer laneQueueLength;

    //目标经过区间段时长
    @JSONField(name = "IntervalDuration")
    @Column(ColumnName = "intervalduration")
    private Integer intervalDuration;

    //区间段内经过的总人数
    @JSONField(name = "HumanNum")
    @Column(ColumnName = "humannum")
    private Integer humanNum;

    //每个车道过车数量
    @JSONField(name = "LaneCarCount")
    @Column(ColumnName = "lanecarcount")
    private Integer laneCarCount;

    //车道阻塞状态(1/0):1:阻塞,0:畅通
    @JSONField(name = "LanesBlockage")
    @Column(ColumnName = "lanesblockage")
    private Integer lanesBlockage;

    //车道时间占有率
    @JSONField(name = "LaneTimeOccupancy")
    @Column(ColumnName = "lanetimeoccupancy")
    private Float laneTimeOccupancy;

    //车头间距(米)
    @JSONField(name = "VehicleHeadDist")
    @Column(ColumnName = "vehicleheaddist")
    private Integer vehicleHeadDist;

    //车头时距(米)
    @JSONField(name = "VehicleHeadTimeDist")
    @Column(ColumnName = "vehicleheadtimedist")
    private Integer vehicleHeadTimeDist;

    //交通灯状态，-1 行车道未检查，1 行车道红灯，2 行车道绿灯，3 行车道黑灯
    @JSONField(name = "TrafficLight")
    @Column(ColumnName = "trafficlight")
    private String trafficLight;

    //汽车类型 (依次序: 小 中 大)
    @JSONField(name = "AvgLeaveLaneCarCountByType")
    @Column(ColumnName = "avgleavelanecarcountbytype")
    private String avgLeaveLaneCarCountByType;

    //汽车类型 (依次序: 小 中 大)
    @JSONField(name = "AvgLeaveLaneCarCountByDir")
    @Column(ColumnName = "avgleavelanecarcountbydir")
    private String avgLeaveLaneCarCountByDir;

    //预留字段
    @JSONField(name = "Remark")
    @Column(ColumnName = "remark")
    private String remark;

    //预留字段1
    @JSONField(name = "Remark1")
    @Column(ColumnName = "remark1")
    private String remark1;

}
