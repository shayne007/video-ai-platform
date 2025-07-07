package com.keensense.search.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.Id;
import com.loocme.sys.annotation.database.Table;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

/**
 * Created by zhanx xiaohui on 2019-09-09.
 */
@Data
@Table(TableName = "event_result")
@ToString(callSuper = true, includeFieldNames = true)
public class EventResult {
    //主键
    @Id
    @JSONField(name = "Id")
    @Column(ColumnName = "id")
    private String id;

    //事件类型(1:抛洒物检测，2：标识牌设备破损检测,3:路面积水,4:路面坑洞,5:路面施工,6:车车事故,7流量溢出,8:拥堵,9:行人创入高速公路)'
    @JSONField(name = "EventType")
    @Column(ColumnName = "eventtype")
    private Integer eventType;

    //图片
    @JSONField(name = "ImgUrl")
    @Column(ColumnName = "imgurl")
    private String imgUrl;

    //备注
    @JSONField(name = "Remark")
    @Column(ColumnName = "remark")
    private String remark;

    //创建时间
    @JSONField(name = "CreateTime" ,format = "yyyy-MM-dd HH:mm:ss")
    @Column(ColumnName = "createtime")
    private Date createTime;

    //在1天中的时间
    @JSONField(name = "TimeInDay")
    @Column(ColumnName = "timeinday")
    private Integer timeInDay;

    //可信度
    @JSONField(name = "Reliability")
    @Column(ColumnName = "reliability")
    private String reliability;

    //位置信息
    @JSONField(name = "LocationInfo")
    @Column(ColumnName = "locationinfo")
    private String locationInfo;

    //任务序列号
    @JSONField(name = "Serialnumber")
    @Column(ColumnName = "serialnumber")
    private String serialnumber;

    //点位编号
    @JSONField(name = "CameraId")
    @Column(ColumnName = "cameraid")
    private String cameraId;

    //事件名称
    @JSONField(name = "EventName")
    @Column(ColumnName = "eventname")
    private String eventName;

    //开始帧序号
    @JSONField(name = "StartFrameIdx")
    @Column(ColumnName = "startframeidx")
    private Long startFrameIdx;

    //结束帧序号
    @JSONField(name = "EndFrameIdx")
    @Column(ColumnName = "endframeidx")
    private Long endFrameIdx;

    //起始时间标记
    @JSONField(name = "StartFramePts")
    @Column(ColumnName = "startframepts")
    private Long startFramePts;

    //终结时间标记
    @JSONField(name = "EndFramePts")
    @Column(ColumnName = "endframepts")
    private Long endFramePts;
}

/**
 * @program: platform
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-09-09 17:10
 **/