package com.keensense.sdk.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 属性配置表
 * </p>
 *
 * @author jobob
 * @since 2019-05-09
 */
@Data
@Accessors(chain = true)
@TableName("cfg_mem_props")
public class CfgMemProps{

	/**
	 * 模块名
	 */
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
	private Timestamp updateTime;

}
