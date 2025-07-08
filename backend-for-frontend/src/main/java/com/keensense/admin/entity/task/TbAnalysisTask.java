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
@TableName("tb_analysis_task")
public class TbAnalysisTask extends Model<TbAnalysisTask> {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务ID
	 */
	@TableId
	private String id;
	/**
	 * 任务名称
	 */
	private String name;
	/**
	 * 任务描述
	 */
	private String desc;
	/**
	 * 分析类型 objext vlpr
	 */
	private String analyType;
	/**
	 * 分析参数
	 */
	private String analyParam;
	/**
	 * 分片数量
	 */
	private Integer sliceNumber;
	/**
	 * 提交时间
	 */
	private Date createTime;
	/**
	 * 完成时间
	 */
	private Date finishTime;
	/**
	 * 最后更改时间
	 */
	private Date lastupdateTime;
	/**
	 * 创建用户
	 */
	private String createUserid;
	/**
	 * 任务类型 1 实时流分析  2 离线文件分析  3 录像分析
	 */
	private Integer taskType;
	/**
	 * 所属监控点id
	 */
	private String cameraId;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 任务状态 0 运行中 1 已停止 2 已删除
	 */
	private Integer status;
	/**
	 * 设备ID
	 */
	private String deviceId;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
