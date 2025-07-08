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
@TableName("image_search_relation")
public class ImageSearchRelation extends Model<ImageSearchRelation> {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 任务关联序列号
	 */
	private String serialnumber;
	/**
	 * 关联目标id
	 */
	private String objextId;
	/**
	 * 
	 */
	private String imgurl;
	/**
	 * 当前任务排序号
	 */
	private Integer seqnumber;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
