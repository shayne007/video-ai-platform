package com.keensense.admin.entity.lawcase;

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
@TableName("tb_case_video_download")
public class TbCaseVideoDownload extends Model<TbCaseVideoDownload> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键，下载任务ID
	 */
	@TableId
	private String id;
	/**
	 * 任务进度
	 */
	private Integer progress;
	/**
	 * 校准时间
	 */
	private String entryTime;
	/**
	 * 视频转移地址
	 */
	private String transferUrl;
	/**
	 * 转移ID
	 */
	private String transferId;
	/**
	 * 转移状态： 1 等待处理 2 已完成 3 处理失败
	 */
	private Integer transferStatus;
	/**
	 * 转移重试次数
	 */
	private Integer transferRetry;
	/**
	 * 录像下载地址
	 */
	private String downloadUrl;
	/**
	 * 录像下载id
	 */
	private String downloadId;
	/**
	 * 下载状态： 1 待提交 2 正在处理 3已完成 4处理失败
	 */
	private Integer downloadStatus;
	/**
	 * 下载进度
	 */
	private Integer downloadProgress;
	/**
	 * 录像下载后文件信息
	 */
	private String downloadFile;
	/**
	 * 录像下载重试次数
	 */
	private Integer downloadRetry;
	/**
	 * 提交转码url地址
	 */
	private String transcodeUrl;
	/**
	 * 转码id
	 */
	private String transcodeId;
	/**
	 * 任务状态： 0等待 1完成 2转码中 3失败
	 */
	private Integer transcodeStatus;
	/**
	 * 转码进度
	 */
	private Integer transcodeProgress;
	/**
	 * 转码后文件信息
	 */
	private String transcodeFile;
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
	 * 备注
	 */
	private String remark;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
