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
@TableName("tb_case_archive_pic")
public class TbCaseArchivePic extends Model<TbCaseArchivePic> {
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
	 * 图片类型objType(1:人,2:车辆,4:人骑车 )
	 */
	private Integer objType;
	/**
	 * 图片Id
	 */
	private String picId;
	/**
	 * 文件远程路径
	 */
	private String picBigPath;
	/**
	 * 图片的标签数据
	 */
	private String picInfo;
	/**
	 * 关联案件编号
	 */
	private String caseCode;
	/**
	 * 文件本地路径
	 */
	private String picThumbPath;
	/**
	 * 发生时间段：0 事前 1事中 2事后
	 */
	private Integer happenPeriod;
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
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
