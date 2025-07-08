package com.keensense.admin.entity.task;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("camera_dept")
public class CameraDept extends Model<CameraDept> {
	private static final long serialVersionUID = 1L;

	/**
	 * 部门id
	 */
	@TableId
	private Long deptId;
	/**
	 * 业务行政部门代码
	 */
	private String longNumber;
	/**
	 * 名称
	 */
	private String deptName;
	/**
	 * 部门编码
	 */
	private String deptNumber;
	/**
	 * 父节点id
	 */
	private String parentId;
	/**
	 * 层级
	 */
	private Long deptLevel;
	/**
	 * 显示名称
	 */
	private String displayName;
	/**
	 * 是否叶子节点
	 */
	private Long isLeaf;
	/**
	 * 状态 0 禁用1启用
	 */
	private Long deptState;
	/**
	 * 行政组织代码
	 */
	private Long adminIdentity;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 联系电话
	 */
	private Long tel;
	/**
	 * 是否业务行政部门 0 否 1 是
	 */
	private Long isBizOrg;
	/**
	 * 行政部门id
	 */
	private String bizCode;
	/**
	 * 序号
	 */
	private Long seq;
	/**
	 * 备注
	 */
	private Long description;
	/**
	 * 端口2
	 */
	private Long creatorId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 最新更新用户id
	 */
	private Long lastUpdateUserId;
	/**
	 * 最新更新时间
	 */
	private Date lastUpdatedTime;
	/**
	 * 行政区域id
	 */
	private String ctrlUnitId;
	/**
	 * 
	 */
	private Integer allcamera;
	/**
	 * 
	 */
	private Integer onlinecamera;
	/**
	 * 经度
	 */
	private String longitude;
	/**
	 * 纬度
	 */
	private String latitude;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.deptId;
  }
}
