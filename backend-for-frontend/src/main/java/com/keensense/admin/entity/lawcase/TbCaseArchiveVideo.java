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
@TableName("tb_case_archive_video")
public class TbCaseArchiveVideo extends Model<TbCaseArchiveVideo> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long id;
	/**
	 * 图片名称/视频名称
	 */
	private String fileName;
	/**
	 * 文件类型：1 实时；2 离线
	 */
	private Integer fileType;
	/**
	 * 文件远程路径
	 */
	private String fileRemotePath;
	/**
	 * 文件本地路径
	 */
	private String fileLocalPath;
	/**
	 * 发生时间段：0事前 1事中 2事后
	 */
	private Integer happenPeriod;
	/**
	 * 是否证据视频(0 否 1 是)
	 */
	private Integer isProof;
	/**
	 * 视频开始时间
	 */
	private Date videoStartTime;
	/**
	 * 视频结束时间
	 */
	private Date videoEndTime;
	/**
	 * 视频关联图片url
	 */
	private String relatePictureUrl;
	/**
	 * 关联案件编号
	 */
	private String caseCode;
	/**
	 * 分析序列号
	 */
	private String serialnumber;
	/**
	 * 关联监控点编号
	 */
	private Long cameraId;
	/**
	 * 删除标识  1:已删除  0:正常
	 */
	private Integer deleted;
	/**
	 * 版本号
	 */
	private Integer version;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 创建人编号
	 */
	private String createUser;
	/**
	 * 最近更新时间
	 */
	private Date lastUpdateTime;
	/**
	 * 视频下载任务Id，关联tb_case_video_download表Id
	 */
	private String videoDownloadId;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
