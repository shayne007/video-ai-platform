package com.keensense.search.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zhanx xiaohui on 2019-07-09.
 */
@Data
@Table(name = "vehicleflowrate_result")
@ToString(callSuper = true, includeFieldNames = true)
public class VehicleflowrateResult {

    //主键
    @Id
    @JSONField(name = "Id")
    @Column(name = "id")
    private String id;

    //位置ID
    @JSONField(name = "LocationId")
    @Column(name = "locationid")
    private String locationId;

    //产生时间
    @JSONField(name = "Datetime", format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "datetime")
    private Date datetime;

    //路面平均占用百分点
    @JSONField(name = "Area")
    @Column(name = "area")
    private Double area;

    //每分钟离开的车辆数
    @JSONField(name = "Leave")
    @Column(name = "leave")
    private Integer leave;

    //最近一分钟，车道的车辆数
    @JSONField(name = "Stranding")
    @Column(name = "stranding")
    private Double stranding;

    @JSONField(name = "Image")
    @Column(name = "image")
    private String image;

    //#########################增加字段

    //监控点id
    @JSONField(name = "CameraId")
    @Column(name = "cameraid")
    private String cameraId;

    //任务序列号
    @JSONField(name = "Serialnumber")
    @Column(name = "serialnumber")
    private String serialnumber;

    //图片路径
    @JSONField(name = "ImgUrl")
    @Column(name = "imgurl")
    private String imgUrl;

    //拥堵级别:0-绿,1-黄,2-红,3-深红,4-黑
    @JSONField(name = "HeavyTrafficLevel")
    @Column(name = "heavytrafficlevel")
    private Integer heavyTrafficLevel;

    //开始的帧数
    @JSONField(name = "StartFrameIdx")
    @Column(name = "startframeidx")
    private Long startFrameIdx;

    //结束的帧数
    @JSONField(name = "EndFrameIdx")
    @Column(name = "endframeidx")
    private Long endFrameIdx;

    //开始的时间戳
    @JSONField(name = "StartFramePts")
    @Column(name = "startframepts")
    private Long startFramePts;

    //结束的时间戳
    @JSONField(name = "EndFramePts")
    @Column(name = "endframepts")
    private Long endFramePts;

    //处理详情:0-未处理,1-已处理,2-已忽略,3-自动舒缓
    @JSONField(name = "State")
    @Column(name = "state")
    private Integer state;

    //车道号
    @JSONField(name = "VehicleLaneIndex")
    @Column(name = "vehiclelaneindex")
    private Integer vehicleLaneIndex;

    //在N秒内车道上平均过车速度（km/h):-1:没有计算(只限车辆)
    @JSONField(name = "VehicleSpeed")
    @Column(name = "vehiclespeed")
    private String vehicleSpeed;

    //在N秒内车道上平均排队长度(米)
    @JSONField(name = "LaneQueueLength")
    @Column(name = "lanequeuelength")
    private Integer laneQueueLength;

    //目标经过区间段时长
    @JSONField(name = "IntervalDuration")
    @Column(name = "intervalduration")
    private Integer intervalDuration;

    //区间段内经过的总人数
    @JSONField(name = "HumanNum")
    @Column(name = "humannum")
    private Integer humanNum;

    //每个车道过车数量
    @JSONField(name = "LaneCarCount")
    @Column(name = "lanecarcount")
    private Integer laneCarCount;

    //车道阻塞状态(1/0):1:阻塞,0:畅通
    @JSONField(name = "LanesBlockage")
    @Column(name = "lanesblockage")
    private Integer lanesBlockage;

    //车道时间占有率
    @JSONField(name = "LaneTimeOccupancy")
    @Column(name = "lanetimeoccupancy")
    private Float laneTimeOccupancy;

    //车头间距(米)
    @JSONField(name = "VehicleHeadDist")
    @Column(name = "vehicleheaddist")
    private Integer vehicleHeadDist;

    //车头时距(米)
    @JSONField(name = "VehicleHeadTimeDist")
    @Column(name = "vehicleheadtimedist")
    private Integer vehicleHeadTimeDist;

    //交通灯状态，-1 行车道未检查，1 行车道红灯，2 行车道绿灯，3 行车道黑灯
    @JSONField(name = "TrafficLight")
    @Column(name = "trafficlight")
    private String trafficLight;

    //汽车类型 (依次序: 小 中 大)
    @JSONField(name = "AvgLeaveLaneCarCountByType")
    @Column(name = "avgleavelanecarcountbytype")
    private String avgLeaveLaneCarCountByType;

    //汽车类型 (依次序: 小 中 大)
    @JSONField(name = "AvgLeaveLaneCarCountByDir")
    @Column(name = "avgleavelanecarcountbydir")
    private String avgLeaveLaneCarCountByDir;

    //预留字段
    @JSONField(name = "Remark")
    @Column(name = "remark")
    private String remark;

    //预留字段1
    @JSONField(name = "Remark1")
    @Column(name = "remark1")
    private String remark1;

}
