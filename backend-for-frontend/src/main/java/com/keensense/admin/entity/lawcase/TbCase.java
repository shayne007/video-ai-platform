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
@TableName("tb_case")
public class TbCase extends Model<TbCase> {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 案件编号
	 */
	private String caseCode;
	/**
	 * 案件名称
	 */
	private String caseName;
	/**
	 * 案件类别
	 */
	private String caseOptionId;
	/**
	 * 案件状态：0 已处警 1 已受理 2 已立案 3 已破案 4 已结案 5 已销案 6 已不立 7 已移交 8 已破未结 9 撤案转行政处罚 50 不处理 51 已调解 52 已终止 59 已终结 60 已处罚 61 已受理未结 62 当场处罚 20 审查中 21 已审查 99 其他
	 */
	private Integer caseStatus;
	/**
	 * 案件详情
	 */
	private String caseDetail;
	/**
	 * 案发时间
	 */
	private Date caseStartTime;
	/**
	 * 结案时间
	 */
	private Date caseEndTime;
	/**
	 * 案发地点
	 */
	private String caseLocation;
	/**
	 * 办案人员
	 */
	private String caseHandleUser;
	/**
	 * 协办侦查员
	 */
	private String caseHandleInvestor;
	/**
	 * 经度
	 */
	private String caseLongitude;
	/**
	 * 纬度
	 */
	private String caseLatitude;
	/**
	 * 所属派出所编码
	 */
	private String caseStationId;
	/**
	 * 目标轨迹图
	 */
	private String trackImageUrl;
	/**
	 * 删除标识  1:已删除  0:正常
	 */
	private Integer deleted;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 创建人编号
	 */
	private String createUser;
	/**
	 * 版本号
	 */
	private Integer version;
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
