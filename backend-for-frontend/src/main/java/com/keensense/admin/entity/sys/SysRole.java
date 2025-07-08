package com.keensense.admin.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("sys_role")
public class SysRole extends Model<SysRole> {
	private static final long serialVersionUID = 1L;

	/**
	 * 角色id
	 */
	@TableId(type = IdType.AUTO)
	private Long roleId;
	/**
	 * 角色名
	 */
	private String roleName;
	/**
	 * 角色描述
	 */
	private String roleRemark;
	/**
	 * 状态
	 */
	private Long state;
	/**
	 * 创建用户id
	 */
	private Long createUserId;
	/**
	 * 创建用户
	 */
	private String createUserName;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 角色标识,程序中判断使用,如"admin"
	 */
	private String roleSign;

	@TableField(exist = false)
	private String permissionIds;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.roleId;
  }
}
