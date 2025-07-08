package com.keensense.admin.entity.task;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @date 2019-06-08 20:11:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_relaytrack_task")
public class TbRelaytrackTask extends Model<TbRelaytrackTask> {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务ID //(value = "id", type = IdType.AUTO)
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
//	private String desc;
	/**
	 * 案件编号
	 */
	private String caseId;
	/**
	 * 录像起始时间
	 */
	private Date startTime;
	/**
	 * 录像结束时间
	 */
	private Date endTime;
	/**
	 * 提交时间
	 */
	private Date createTime;
	/**
	 * 最后更改时间
	 */
	private Date lastupdateTime;
	/**
	 * 目标类型：1-人；2-骑；4-车
	 */
	private Integer objType;
	/**
	 * 搜图图片
	 */
	private String picture;
	/**
	 * 任务总进度
	 */
	private Integer progress;
	/**
	 * 任务状态 0 运行中 1 已完成 2 已删除
	 */
	private Integer status;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 
	 */
	private String feature;

	@TableField(exist = false)
	private String cameraNames;

	@TableField(exist = false)
	private String cameraIds;

	private String analysisId;

	private String uuid;

	public TbRelaytrackTask() {

	}
	public TbRelaytrackTask(String id, String name, String caseId, Date startTime, Date endTime, Integer objType,
							String picture,String feature, String analysisId, String uuid) {
		super();
		this.id = id;
		this.name = name;
		this.caseId = caseId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.createTime = new Date();
		this.objType = objType;
		this.picture = picture;
		this.progress = 0;
		this.status = 0;
		this.feature = feature;
		this.analysisId = analysisId;
		this.uuid = uuid;
	}
  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
