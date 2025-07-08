package com.keensense.densecrowd.entity.sys;

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
@TableName("cfg_mem_props")
public class CfgMemProps extends Model<CfgMemProps> {
	private static final long serialVersionUID = 1L;

	/**
	 * 模块名
	 */
	@TableId
	private String moduleName;
	/**
	 * 参数key
	 */
	private String propKey;
	/**
	 * 参数name
	 */
	private String propName;
	/**
	 * 参数值
	 */
	private String propValue;
	/**
	 * 参数描述
	 */
	private String propDesc;
	/**
	 * 最后修改时间
	 */
	private Date updateTime;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.moduleName;
  }
}
