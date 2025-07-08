package com.keensense.admin.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_permission")
public class SysPermission extends Model<SysPermission> {
	private static final long serialVersionUID = 1L;

	/**
	 * 权限id
	 */
	@TableId
	private Long permissionId;
	/**
	 * 资源id
	 */
	private Long resourceId;
	/**
	 * 资源名称
	 */
	private String resourceName;
	/**
	 * 资源类型
	 */
	private Long resourceType;
	/**
	 * 动作
	 */
	private Long actionId;
	/**
	 * 
	 */
	private String actionName;
	/**
	 * 功能编码
	 */
	private String functionCode;
	/**
	 * 权限名
	 */
	private String permissionName;
	/**
	 * 权限标识,程序中判断使用,如"user:create"
	 */
	private String permissionSign;
	/**
	 * 权限描述,UI界面显示使用
	 */
	private String description;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.permissionId;
  }
}
