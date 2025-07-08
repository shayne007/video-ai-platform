package com.keensense.admin.entity.task;

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
@TableName("tb_imagesearch_record")
public class TbImagesearchRecord extends Model<TbImagesearchRecord> {
	private static final long serialVersionUID = 1L;

	/**
	 * 记录id，主键
	 */
	@TableId
	private String id;
	/**
	 * 任务编号
	 */
	private String taskId;
	/**
	 * 结果记录创建日期
	 */
	private String createDate;
	/**
	 * 记录uuid
	 */
	private String recordId;
	/**
	 * 类型：1-置顶；2-删除
	 */
	private Integer type;
	/**
	 * 提交时间
	 */
	private Date createTime;
	/**
	 * 相似度
	 */
	private String score;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
