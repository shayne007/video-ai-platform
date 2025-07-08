package com.keensense.admin.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "视频点位参数")
public class CameraRequest implements Serializable {

    @ApiModelProperty(value = "点位id")
    private Long id;

    @ApiModelProperty(value = "点位名称")
    private String name;

    @ApiModelProperty(value = "监控点类型: 1,联网实时视频 2,IPC直连 3,抓拍机")
    private Long cameratype;

    @ApiModelProperty(value = "点位类型 [1卡口, 2电警, 0其他]")
    private Long category;

    @ApiModelProperty(value = "机型 [1球机, 2枪机, 0其他]")
    private Long type;

    @ApiModelProperty(value = "行政区域Id")
    private String region;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "方向")
    private String direction;

    @ApiModelProperty(value = "地点")
    private String location;

    @ApiModelProperty(value = "状态 [0:失效，1:激活]")
    private Long status;

    @ApiModelProperty(value = "描述")
    private String dsc;

    @ApiModelProperty(value = "监控点编码")
    private String extcameraid;

    @ApiModelProperty(value = "监控点厂商")
    private String brandname;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "url链接")
    private String url;

    @ApiModelProperty(value = "品牌id")
    private Long brandid;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "端口1")
    private Long port1;

    @ApiModelProperty(value = "端口1")
    private Long port2;

    @ApiModelProperty(value = "登陆账号")
    private String account;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "通道")
    private Long channel;

    @ApiModelProperty(value = "管理责任单位")
    private String admindept;

    @ApiModelProperty(value = "责任人")
    private String admin;

    @ApiModelProperty(value = "联系电话")
    private String telephone;

    @ApiModelProperty(value = "允许区域")
    private String follwarea;

    @ApiModelProperty(value = "缩略图")
    private String thumbNail;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "监控点组id")
    private Long cameragroupid;

    /**
     * 自定义字段
     */
    private String regionName;

    private int taskCount;

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
    private String starttask;
}