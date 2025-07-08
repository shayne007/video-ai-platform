package com.keensense.admin.entity.lawcase;

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
@TableName("case_camera_media")
public class CaseCameraMedia extends Model<CaseCameraMedia> {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private String id;
	/**
	 * 1 img,2 video
	 */
	private String fileType;
	/**
	 * 不包含小数点，例如：avi
	 */
	private String fileSuffix;
	/**
	 * 例如：五一路卡口视频.avi
	 */
	private String fileName;
	/**
	 * 上传后的文件名
	 */
	private String fileNameafterupload;
	/**
	 * 上传后文件详细路径
	 */
	private String filePathafterupload;
	/**
	 * 上传后的大图文件名
	 */
	private String fileBigImage;
	/**
	 * 
	 */
	private Long videobitId;
	/**
	 * 
	 */
	private String targetAppeartime;
	/**
	 * 
	 */
	private String fileDescription;
	/**
	 * 
	 */
	private String createTime;
	/**
	 * 
	 */
	private String createUserid;
	/**
	 * 在上传一个文件后，对应的应有一个缩略图命名规则为：上传后文件名+thumbnail例如：上传后文件名为123456，则缩略图命名为：123456thumbnail
	 */
	private String thumbNail;
	/**
	 * 文件大小
	 */
	private String fileSize;
	/**
	 * 提交ocx转码后id
	 */
	private String useruploadvideoid;
	/**
	 * 提交ocx转码后视频路径
	 */
	private String useruploadPath;
	/**
	 * 提交ocx转码后的状态
	 */
	private String useruploadStatus;
	/**
	 * 素材关联案件id
	 */
	private String c1;
	/**
	 * clue：素材，source:原始视频 【该字段 区分 原始视频和 手动上传的 图片或短视频素材】
	 */
	private String c2;
	/**
	 * 设备名称
	 */
	private String c3;
	/**
	 * 任务ID
	 */
	private String c4;
	/**
	 * 图片类型objType(1:人,2:车辆,4:人骑车 )
	 */
	private String c5;
	/**
	 * 线索采集分类 1：中心现场，2：清晰图像，3：活动轨迹，4：落脚点信息，5：破案信息
	 */
	private String clueType;
	/**
	 * 
	 */
	private String c6;
	/**
	 * 
	 */
	private String c7;
	/**
	 * 
	 */
	private String c8;
	/**
	 * 
	 */
	private String verifyingStatus;
	/**
	 * 
	 */
	private String syncStatus;
	/**
	 * 
	 */
	private String lawcaseid;
	/**
	 * 图片的标签数据
	 */
	private String picInfo;

	/**
	 * 经度
	 */
	private String longitude;
	/**
	 * 纬度
	 */
	private String latitude;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
