package com.keensense.densecrowd.vo;

import lombok.Data;

import java.io.*;
import java.util.*;

@Data
public class VsdTaskVo implements Serializable {

    private String id;
    private String idStr;
    private String serialnumber;
    private String userSerialnumber;

    private String type;

    private String typeName;

    private Integer status;

    private String reserve;

    private String slaveip;

    private Date createtime;

    private Date endtime;// 结束时间

    private String param;

    private Integer progress;
    /**
     * 自定义字段
     */
    private Long cameraFileId;

    private String fileName;

    private String cameraId;

    private String cameraName;

    private String filefromtype;

    private Integer isValid;

    private String cameraType;

    private String region;

    private String createTimeStr;
    private String endTimeStr;// 结束时间
    private String taskMode;
    private String outputDSFactor;

    private String objMinTimeInMs;
    private String sensitivity;
    private String objMinSize;
    private String objInterested; //是否感兴趣区域
    private String objVertices;//坐标值
    private String thumbNail;//任务详情展示图片字段
    private String entryTime;
    //任务类型
    private long fromType;
    //创建者name
    private String creator;
}