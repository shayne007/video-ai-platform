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
@TableName("t_relaytrack_taskdetail")
public class TRelaytrackTaskdetail extends Model<TRelaytrackTaskdetail> {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务ID
	 */
	@TableId
	private String taskdetailId;
	/**
	 * 主任务ID
	 */
	private String taskId;
	/**
	 * 点位
	 */
	private String cameraId;
	/**
	 * 点位编码
	 */
	private String cameraAddress;
	/**
	 * 视频开始时间
	 */
	private Date startTime;
	/**
	 * 视频结束时间
	 */
	private Date endTime;
	/**
	 * 从天网下载的文件ID
	 */
	private String fileId;
	/**
	 * 提交转码文件ID
	 */
	private String videoId;
	/**
	 * 转码ID
	 */
	private String transcodeId;
	/**
	 * 转码进度
	 */
	private String transcodeProgress;
	/**
	 * 分析任务ID
	 */
	private String analyTaskId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 分析服务器IP
	 */
	private String slaveIp;
	/**
	 * 下载文件名
	 */
	private String fileName;
	/**
	 * 
	 */
	private String fileDownName;
	/**
	 * 
	 */
	private String fileDownProgress;
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
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.taskdetailId;
  }
}
