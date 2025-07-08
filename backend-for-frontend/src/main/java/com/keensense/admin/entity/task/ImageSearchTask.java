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
@TableName("image_search_task")
public class ImageSearchTask extends Model<ImageSearchTask> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long id;
	/**
	 * 序列号，关联si_task的serialNum
	 */
	private String serialnumber;
	/**
	 * 以图搜图目标图片 http:// 或者 file://
	 */
	private String targetImageUrl;
	/**
	 * 处理状态 0 等待处理 1 处理成功 2 处理失败
	 */
	private Integer status;
	/**
	 * 处理进度
	 */
	private Integer progress;
	/**
	 * 任务名称
	 */
	private String taskname;
	/**
	 * 任务描述
	 */
	private String remark;
	/**
	 * 目标类型,1:人,2:汽车,4：人骑车
	 */
	private Integer tasktype;
	/**
	 * 算法类别：0 综合特征(需要 cuda 设备支持) 1 颜色优先特征
	 */
	private Integer algorithmType;
	/**
	 * 结果分表的表名，为空时，结果输出到si_task
	 */
	private String resultTblName;
	/**
	 * 备选集的总数
	 */
	private Long totalCount;
	/**
	 * 任务创建时间
	 */
	private Date createtime;
	/**
	 * 添加以图搜图任务接口output参数(定义以图搜图比对结果的输出方式和格式)
	 */
	private String output;
	/**
	 * 搜图范围：起始时间
	 */
	private String qstarttime;
	/**
	 * 搜图范围：结束时间
	 */
	private String qendtime;
	/**
	 * 搜图范围：监控点列表
	 */
	private String qcameraids;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
