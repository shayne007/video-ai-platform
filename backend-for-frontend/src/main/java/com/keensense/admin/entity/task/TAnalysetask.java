package com.keensense.admin.entity.task;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_analysetask")
public class TAnalysetask extends Model<TAnalysetask> {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务ID
	 */
	@TableId
	private String taskId;
	/**
	 * 
	 */
	private String fileId;
	/**
	 * 任务名称
	 */
	private String taskName;
	/**
	 * 任务描述
	 */
	private String taskDes;
	/**
	 * 任务状态： 1 待提交 2 正在处理 3已完成 4处理失败
	 */
	private BigDecimal tastStatus;
	/**
	 * 分析进度
	 */
	private BigDecimal analyseProgress;
	/**
	 * 分析参数
	 */
	private String analyseParam;
	/**
	 * 
	 */
	private String flowNumber;
	/**
	 * 提交时间
	 */
	private Date submitTime;
	/**
	 * 完成时间
	 */
	private Date finishTime;
	/**
	 * 最后更改时间
	 */
	private Date lastupdateTime;
	/**
	 * 
	 */
	private String httpUrlparam;
	/**
	 * 用户参数
	 */
	private String userData;
	/**
	 * 主任务ID
	 */
	private String mainId;
	/**
	 * 创建用户
	 */
	private String createUserId;
	/**
	 * 任务类型 1 目标检索  2 视频浓缩
	 */
	private BigDecimal taskType;
	/**
	 * 检索类型： 1 检索人 2 检索车 3 人骑车 4 以图搜图 5 浓缩
	 */
	private BigDecimal targetType;
	/**
	 * 分析服务器任务ID
	 */
	private String analysisTaskId;
	/**
	 * 
	 */
	private String c1;
	/**
	 * 
	 */
	private String c2;
	/**
	 * 
	 */
	private String c3;
	/**
	 * 
	 */
	private String c4;
	/**
	 * 
	 */
	private String c5;
	/**
	 * 
	 */
	private String c6;
	/**
	 * 
	 */
	private String url;
	/**
	 * 
	 */
	private String cameraId;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.taskId;
  }
}
