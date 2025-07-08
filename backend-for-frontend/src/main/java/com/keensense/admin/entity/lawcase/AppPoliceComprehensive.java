package com.keensense.admin.entity.lawcase;

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
@TableName("app_police_comprehensive")
public class AppPoliceComprehensive extends Model<AppPoliceComprehensive> {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private String lawcaseId;
	/**
	 * 案件名称
	 */
	private String caseName;
	/**
	 * 案件编号
	 */
	private String caseCode;
	/**
	 * 案件类别
	 */
	private String categoryValue;
	/**
	 * 案件性质
	 */
	private String propertyValue;
	/**
	 * 
	 */
	private String caseState;
	/**
	 * 发案起始时间
	 */
	private Date startTime;
	/**
	 * 发案终止时间
	 */
	private Date endTime;
	/**
	 * 接警时间
	 */
	private Date recieveTime;
	/**
	 * 
	 */
	private Date resolveTime;
	/**
	 * 责任单位
	 */
	private String engageOrg;
	/**
	 * 报案人姓名
	 */
	private String reporterName;
	/**
	 * 报案人电话
	 */
	private String reporterPhone;
	/**
	 * 案发区划
	 */
	private String locationId;
	/**
	 * 案发派出所
	 */
	private String policeStation;
	/**
	 * 案发社区
	 */
	private String caseCommunity;
	/**
	 * 案发地点类型
	 */
	private String locationType;
	/**
	 * 案发地点名称
	 */
	private String locationName;
	/**
	 * 案发详址
	 */
	private String locationDetail;
	/**
	 * 发案经度
	 */
	private String lontitude;
	/**
	 * 发案纬度
	 */
	private String latitude;
	/**
	 * 案发区域
	 */
	private String caseArea;
	/**
	 * 案发场所
	 */
	private String casePlaces;
	/**
	 * 地点类别
	 */
	private String placesType;
	/**
	 * 案情简介
	 */
	private String caseDesc;
	/**
	 * 涉案金额
	 */
	private String caseMoney;
	/**
	 * 经济损失
	 */
	private String moneyLose;
	/**
	 * 选择处所
	 */
	private String choosePlace;
	/**
	 * 选择对象
	 */
	private String choosePerson;
	/**
	 * 选择物品
	 */
	private String chooseItem;
	/**
	 * 选择日期
	 */
	private String chooseDate;
	/**
	 * 选择时间
	 */
	private String chooseTime;
	/**
	 * 选择时机
	 */
	private String chooseChance;
	/**
	 * 选择天气 
	 */
	private String chooseWeather;
	/**
	 * 侵入手段
	 */
	private String caseMethodFeaturesValue;
	/**
	 * 窃入手段
	 */
	private String stealMethod;
	/**
	 * 伪装灭迹
	 */
	private String disguiseMethod;
	/**
	 * 预备手段
	 */
	private String prepareMethod;
	/**
	 * 组织形式
	 */
	private String orgWay;
	/**
	 * 勾结形式
	 */
	private String colludeWay;
	/**
	 * 作案范围
	 */
	private String caseWay;
	/**
	 * 试探方式
	 */
	private String exploreWay;
	/**
	 * 行为特点
	 */
	private String actionWay;
	/**
	 * 是否抢渡
	 */
	private String isCross;
	/**
	 * 公交路线
	 */
	private String busWay;
	/**
	 * 上车站点
	 */
	private String upStation;
	/**
	 * 下车站点
	 */
	private String downStation;
	/**
	 * 作案工具
	 */
	private String caseTool;
	/**
	 * 处理结果
	 */
	private String oprateResult;
	/**
	 * 关键字
	 */
	private String keyWords;
	/**
	 * 备注及民警意见
	 */
	private String remarkAdvice;
	/**
	 * 是否有嫌疑人
	 */
	private String hasSupect;
	/**
	 * 是否有现场勘查
	 */
	private String hasSurvey;
	/**
	 * 是否是老数据
	 */
	private BigDecimal isOldData;
	/**
	 * 电子归档id
	 */
	private String electronicFileId;
	/**
	 * 案件分类： 案件、疑情
	 */
	private String caseClass;
	/**
	 * 案件封面
	 */
	private String caseThumbnail;
	/**
	 * 创建人
	 */
	private BigDecimal creatorId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新人
	 */
	private BigDecimal updateUserId;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 删除标识
	 */
	private BigDecimal isDeleted;
	/**
	 * 删除时间
	 */
	private Date deleteTime;
	/**
	 * 删除原因
	 */
	private String deleteReason;
	/**
	 * 归档信息
	 */
	private String archiveInfo;
	/**
	 * 归档报告
	 */
	private String archiveReport;
	/**
	 * 责任人
	 */
	private BigDecimal principal;
	/**
	 * 是否是经典案例
	 */
	private BigDecimal isClassicCase;
	/**
	 * 案发地图层级
	 */
	private BigDecimal happenPlaceLevel;
	/**
	 * 报案人
	 */
	private String reportPerson;
	/**
	 * 报案人证件号码
	 */
	private String idCard;
	/**
	 * 报案人电话
	 */
	private String contactPhone;
	/**
	 * 是否是合格案件
	 */
	private BigDecimal isqualified;
	/**
	 * 专网id
	 */
	private BigDecimal sourceid;
	/**
	 * 专网网闸
	 */
	private String isacrossborder;
	/**
	 *  审批状态
	 */
	private BigDecimal verifyingStatus;
	/**
	 * 审批关联的附件id
	 */
	private BigDecimal withwaitverifyres;
	/**
	 * 来源系统
	 */
	private String orginSystem;
	/**
	 * 系统编码
	 */
	private String orginCode;
	/**
	 * 接触警编号
	 */
	private String recieveCode;
	/**
	 * 交通工具
	 */
	private String transport;
	/**
	 * 
	 */
	private BigDecimal syncStatus;
	/**
	 * 
	 */
	private String trackImageUrl;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.lawcaseId;
  }
}
