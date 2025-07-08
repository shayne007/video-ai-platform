package com.keensense.densecrowd.entity.task;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.*;
import java.util.*;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("camera")
public class Camera extends Model<Camera> {
	private static final long serialVersionUID = 1L;

	/**
	 * 点位id
	 */
	@TableId
	private Long id;
	/**
	 * 点位名称
	 */
	private String name;
	/**
	 * 点位类型
	 */
	private Long cameratype;
	/**
	 * 点位类别
	 */
	private Long category;
	/**
	 * 机型
	 */
	private Long type;
	/**
	 * 行政区域
	 */
	private String region;
	/**
	 * 经度
	 */
	private String longitude;
	/**
	 * 纬度
	 */
	private String latitude;
	/**
	 * 方向
	 */
	private String direction;
	/**
	 * 地点
	 */
	private String location;
	/**
	 * 状态
	 */
	private Long status;
	/**
	 * 描述
	 */
	private String dsc;
	/**
	 * 品牌id
	 */
	private Long brandid;
	/**
	 * 品牌名称
	 */
	private String brandname;
	/**
	 * 型号
	 */
	private String model;
	/**
	 * ip地址
	 */
	private String ip;
	/**
	 * 端口1
	 */
	private Long port1;
	/**
	 * 端口2
	 */
	private Long port2;
	/**
	 * 登陆账号
	 */
	private String account;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 通道
	 */
	private Long channel;
	/**
	 * 设备编号
	 */
	private String extcameraid;
	/**
	 * 管理责任单位
	 */
	private String admindept;
	/**
	 * 责任人
	 */
	private String admin;
	/**
	 * 联系电话
	 */
	private String telephone;
	/**
	 * 联系地址
	 */
	private String address;
	/**
	 * url
	 */
	private String url;
	/**
	 * 允许区域
	 */
	private String follwarea;
	/**
	 * 缩略图
	 */
	private String thumbNail;
	/**
	 * 监控点组id
	 */
	private Long cameragroupid;
	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 告警阈值
	 */
	private Integer alarmThreshold;

	@TableField(exist = false)
	private String serialnumber;
  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
