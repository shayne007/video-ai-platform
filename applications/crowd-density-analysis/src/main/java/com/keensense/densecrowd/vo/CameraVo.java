package com.keensense.densecrowd.vo;

import lombok.Data;

import java.io.*;
import java.util.*;

@Data
public class CameraVo implements Serializable {
    private Long id;

    private String name;

    private Long cameratype;

    private Long category;

    private Long type;

    private String region;

    private String regionName;

    private String longitude;

    private String latitude;

    private String direction;

    private String location;

    private Long status;

    private String dsc;

    private Long brandid;

    private String brandname;

    private String model;

    private String ip;

    private Long port1;

    private Long port2;

    private String account;

    private String password;

    private Long channel;

    private String extcameraid;

    private String admindept;

    private String admin;

    private String telephone;

    private String address;

    private String url;

    private String follwarea;

    private Long cameragroupid;

    private String thumbNail;

    private Date createTime;

    /**
     * 自定义字段
     */
    private int taskCount;

    private List<VsdTaskVo> vsdTaskList;

    /**
     * 地区id
     */
    private String regionExt;

    // 是否有任务正在进行
    private boolean taskProcessFlag;

    private boolean insertFlag = false;
    private String serialnumber;
    private Integer isvalid;
    private String taskId;
    private String[] converseCoords;

    private Integer alarmThreshold;
}