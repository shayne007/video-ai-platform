package com.keensense.admin.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("sys_log")
public class SysLog extends Model<SysLog> {
	private static final long serialVersionUID = 1L;

	/**
	 * 日志id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 模块名称
	 */
	private String moduleName;
	/**
	 * 模块url
	 */
	private String moduleUrl;
	/**
	 * 操作类型 1查询 2新增 3修改 4删除 5登录 6退出 7其他 
	 */
	private Integer actionType;
	/**
	 * 创建用户用户名
	 */
	private String userName;
	/**
	 * 创建用户
	 */
	private String realName;
	/**
	 * ip地址
	 */
	private String ipaddr;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 预留字段1
	 */
	private String c1;
	/**
	 * 预留字段2
	 */
	private String c2;
	/**
	 * 预留字段3
	 */
	private String c3;
	/**
	 * 预留字段4
	 */
	private String c4;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
