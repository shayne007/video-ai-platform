package com.keensense.search.domain;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Date;
import lombok.Data;
import lombok.ToString;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * Created by zhanx xiaohui on 2019-09-09.
 */
@Data
@Table(name = "event_result")
@ToString(callSuper = true, includeFieldNames = true)
public class EventResult {
    //主键
    @Id
    @JSONField(name = "Id")
    @Column(name = "id")
    private String id;

    //事件类型(1:抛洒物检测，2：标识牌设备破损检测,3:路面积水,4:路面坑洞,5:路面施工,6:车车事故,7流量溢出,8:拥堵,9:行人创入高速公路)'
    @JSONField(name = "EventType")
    @Column(name = "eventtype")
    private Integer eventType;

    //图片
    @JSONField(name = "ImgUrl")
    @Column(name = "imgurl")
    private String imgUrl;

    //备注
    @JSONField(name = "Remark")
    @Column(name = "remark")
    private String remark;

    //创建时间
    @JSONField(name = "CreateTime" ,format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "createtime")
    private Date createTime;

    //在1天中的时间
    @JSONField(name = "TimeInDay")
    @Column(name = "timeinday")
    private Integer timeInDay;

    //可信度
    @JSONField(name = "Reliability")
    @Column(name = "reliability")
    private String reliability;

    //位置信息
    @JSONField(name = "LocationInfo")
    @Column(name = "locationinfo")
    private String locationInfo;

    //任务序列号
    @JSONField(name = "Serialnumber")
    @Column(name = "serialnumber")
    private String serialnumber;

    //点位编号
    @JSONField(name = "CameraId")
    @Column(name = "cameraid")
    private String cameraId;

    //事件名称
    @JSONField(name = "EventName")
    @Column(name = "eventname")
    private String eventName;

    //开始帧序号
    @JSONField(name = "StartFrameIdx")
    @Column(name = "startframeidx")
    private Long startFrameIdx;

    //结束帧序号
    @JSONField(name = "EndFrameIdx")
    @Column(name = "endframeidx")
    private Long endFrameIdx;

    //起始时间标记
    @JSONField(name = "StartFramePts")
    @Column(name = "startframepts")
    private Long startFramePts;

    //终结时间标记
    @JSONField(name = "EndFramePts")
    @Column(name = "endframepts")
    private Long endFramePts;
}

/**
 * @program: platform
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-09-09 17:10
 **/