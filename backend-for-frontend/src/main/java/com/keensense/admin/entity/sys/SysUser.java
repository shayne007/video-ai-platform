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
@TableName("sys_user")
public class SysUser extends Model<SysUser> {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	@TableId(type = IdType.AUTO)
	private Long userId;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 姓名
	 */
	private String realName;
	/**
	 * 电话
	 */
	private String tel;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 部门id
	 */
	private String deptId;
	/**
	 * 状态
	 */
	private String isvalid;
	/**
	 * 部门管理员
	 */
	private String isDeptAdmin;
	/**
	 * 主题
	 */
	private String themes;
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

	@TableField(exist = false)
	private String roleId;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.userId;
  }
}
