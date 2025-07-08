package com.keensense.admin.entity.task;

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
@TableName("bike_model")
public class BikeModel extends Model<BikeModel> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId
	private Integer id;
	/**
	 * 类型ID
	 */
	private Integer kindId;
	/**
	 * 类型名称
	 */
	private String kindName;
	/**
	 * 款式ID
	 */
	private Integer modelId;
	/**
	 * 款式名称
	 */
	private String modelName;
	/**
	 * 扩展字段1
	 */
	private String info1;
	/**
	 * 扩展字段2
	 */
	private String info2;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
