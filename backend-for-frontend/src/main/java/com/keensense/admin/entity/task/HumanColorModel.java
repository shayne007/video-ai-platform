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
@TableName("human_color_model")
public class HumanColorModel extends Model<HumanColorModel> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId
	private Integer id;
	/**
	 * 颜色ID
	 */
	private Integer colorId;
	/**
	 * RGB颜色标签
	 */
	private Integer colorRgbTag;
	/**
	 * BGR颜色标签
	 */
	private Integer colorBgrTag;
	/**
	 * 十六进制颜色标签
	 */
	private String colorHexTag;
	/**
	 * 颜色名
	 */
	private String colorName;
	/**
	 * 模糊颜色名
	 */
	private String fuzzyColor;
	/**
	 * 色号
	 */
	private String colorNumber;
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
