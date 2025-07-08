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
@TableName("car_brand_model")
public class CarBrandModel extends Model<CarBrandModel> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId
	private Integer id;
	/**
	 * 品牌ID
	 */
	private String brandId;
	/**
	 * 品牌名称
	 */
	private String brandName;
	/**
	 * 车系码
	 */
	private String carSeriesId;
	/**
	 * 车系名称
	 */
	private String carSeriesName;
	/**
	 * 车年份码
	 */
	private String carYearId;
	/**
	 * 车年份名称
	 */
	private String carYearName;
	/**
	 * 车种类码
	 */
	private String carKindId;
	/**
	 * 车种类名称
	 */
	private String carKindName;
	/**
	 * 车辆款式码
	 */
	private String carFullId;
	/**
	 * 车辆款式名称
	 */
	private String carFullName;
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
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
