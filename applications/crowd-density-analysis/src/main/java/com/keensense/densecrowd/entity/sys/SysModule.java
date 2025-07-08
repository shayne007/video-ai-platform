package com.keensense.densecrowd.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_module_dev")
public class SysModule extends Model<SysModule> {
	private static final long serialVersionUID = 1L;

	/**
	 * 模块id
	 */
	@TableId(type = IdType.AUTO)
	private Long moduleId;
	/**
	 * 父节点id
	 */
	private Long parentId;
	/**
	 * 链接
	 */
	private String moduleUrl;
	/**
	 * 状态
	 */
	private String state;
	/**
	 * 是否可见
	 */
	private String isVisible;
	/**
	 * 
	 */
	private String actions;
	/**
	 * 备注
	 */
	private String longNumber;
	/**
	 * 部门id
	 */
	private Long moduleLevel;
	/**
	 * 显示名称
	 */
	private String displayName;
	/**
	 * 是否叶子
	 */
	private Long leaf;
	/**
	 * 排序
	 */
	private Long seq;
	/**
	 * 名称
	 */
	private String moduleName;
	/**
	 * 编号
	 */
	private String moduleNumber;
	/**
	 * 描述
	 */
	private String moduleDescription;
	/**
	 * 创建用户id
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
	 * 部门id
	 */
	private String ctrlUnitId;
	/**
	 * 图标
	 */
	private String moduleIcon;
	/**
	 * 是否首页显示
	 */
	private String isDsiplay;
	/**
	 * 显示顺序
	 */
	private Long displayOrder;
	/**
	 * 扩展字段
	 */
	private String info1;
	/**
	 * 扩展字段
	 */
	private String info2;
	/**
	 * 扩展字段
	 */
	private String info3;

	private String perms;

	private String type;

	@TableField(exist = false)
	private List<SysModule> children;
	/**
	 * 角色菜单编辑页勾选
	 */
	@TableField(exist = false)
	private boolean display = false;
	@TableField(exist = false)
	private boolean closed;
  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.moduleId;
  }
}
