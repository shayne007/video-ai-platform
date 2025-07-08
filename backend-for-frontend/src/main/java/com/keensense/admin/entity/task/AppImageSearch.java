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
@TableName("app_image_search")
public class AppImageSearch extends Model<AppImageSearch> {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long searchId;
	/**
	 * 1：person 2：car 4：bike
	 */
	private Integer objextType;
	/**
	 * 图片路径
	 */
	private String imageUrl;
	/**
	 * 支持多监控点查询,例如：cameraId1,cameraId2，.....，以英文状态下“,”逗号分隔
	 */
	private String cameraAddress;
	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 
	 */
	private String streamAnsSerialnumbers;
	/**
	 * 以图搜图任务id
	 */
	private String serialnumber;
	/**
	 * 搜图进度
	 */
	private String progress;
	/**
	 * 创建人
	 */
	private Long createUserId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 处理的状态 0：成功，其他：失败
	 */
	private String ret;
	/**
	 * 返回信息
	 */
	private String responseMsg;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 以图搜图批次id
	 */
	private String batchId;
	/**
	 * 0 接力追踪以图搜图 1 视频分析以图搜图
	 */
	private String type;
	/**
	 * 图片特征
	 */
	private String c1;
	/**
	 * 图片id
	 */
	private String c2;
	/**
	 * 
	 */
	private String c3;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.searchId;
  }
}
