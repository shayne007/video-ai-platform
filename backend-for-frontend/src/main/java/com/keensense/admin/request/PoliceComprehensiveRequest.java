package com.keensense.admin.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "案件管理参数")
public class PoliceComprehensiveRequest implements Serializable {

    @ApiModelProperty(value = "案件id")
    private String lawcaseId;

    @ApiModelProperty(value = "案件名称")
    private String caseName;

    @ApiModelProperty(value = "发案起始时间")
    private Date startTime;

    @ApiModelProperty(value = "案件类别")
    private String categoryValue;

    @ApiModelProperty(value = "案发详址")
    private String locationDetail;

    @ApiModelProperty(value = "发案经度")
    private String lontitude;

    @ApiModelProperty(value = "发案纬度")
    private String latitude;

    @ApiModelProperty(value = "案情简介")
    private String caseDesc;



    @ApiModelProperty(value = "案件编号")
    private String caseCode;

    @ApiModelProperty(value = "案件性质")
    private String propertyValue;

    @ApiModelProperty(value = "案件状态")
    private String caseState;

    @ApiModelProperty(value = "发案终止时间")
    private Date endTime;

    @ApiModelProperty(value = "接警时间")
    private Date recieveTime;

    @ApiModelProperty(value = "解决时间")
    private Date resolveTime;

    @ApiModelProperty(value = "责任单位")
    private String engageOrg;

    @ApiModelProperty(value = "报案人姓名")
    private String reporterName;

    @ApiModelProperty(value = "报案人电话")
    private String reporterPhone;

    @ApiModelProperty(value = "案发区划")
    private String locationId;

    @ApiModelProperty(value = "案发派出所")
    private String policeStation;

    @ApiModelProperty(value = "案发社区")
    private String caseCommunity;

    @ApiModelProperty(value = "案发地点类型")
    private String locationType;

    @ApiModelProperty(value = "案发地点名称")
    private String locationName;

    @ApiModelProperty(value = "案发区域")
    private String caseArea;

    @ApiModelProperty(value = "案发场所")
    private String casePlaces;

    @ApiModelProperty(value = "地点类别")
    private String placesType;

    @ApiModelProperty(value = "涉案金额")
    private String caseMoney;

    @ApiModelProperty(value = "经济损失")
    private String moneyLose;

    @ApiModelProperty(value = "选择处所")
    private String choosePlace;

    @ApiModelProperty(value = "选择对象")
    private String choosePerson;

    @ApiModelProperty(value = "选择物品")
    private String chooseItem;

    @ApiModelProperty(value = "选择日期")
    private String chooseDate;

    @ApiModelProperty(value = "选择时间")
    private String chooseTime;

    @ApiModelProperty(value = "选择时机")
    private String chooseChance;

    @ApiModelProperty(value = "选择天气")
    private String chooseWeather;

    @ApiModelProperty(value = "侵入手段")
    private String caseMethodFeaturesValue;

    @ApiModelProperty(value = "窃入手段")
    private String stealMethod;

    @ApiModelProperty(value = "伪装灭迹")
    private String disguiseMethod;

    @ApiModelProperty(value = "预备手段")
    private String prepareMethod;

    @ApiModelProperty(value = "组织形式")
    private String orgWay;

    @ApiModelProperty(value = "勾结形式")
    private String colludeWay;

    @ApiModelProperty(value = "作案范围")
    private String caseWay;

    @ApiModelProperty(value = "试探方式")
    private String exploreWay;

    @ApiModelProperty(value = "行为特点")
    private String actionWay;

    @ApiModelProperty(value = "是否抢渡")
    private String isCross;

    @ApiModelProperty(value = "公交路线")
    private String busWay;

    @ApiModelProperty(value = "上车站点")
    private String upStation;

    @ApiModelProperty(value = "下车站点")
    private String downStation;

    @ApiModelProperty(value = "作案工具")
    private String caseTool;

    @ApiModelProperty(value = "处理结果")
    private String oprateResult;

    @ApiModelProperty(value = "关键字")
    private String keyWords;

    @ApiModelProperty(value = "备注及民警意见")
    private String remarkAdvice;

    @ApiModelProperty(value = "是否有嫌疑人")
    private String hasSupect;

    @ApiModelProperty(value = "是否有现场勘查")
    private String hasSurvey;

    @ApiModelProperty(value = "是否是老数据")
    private Integer isOldData;

    @ApiModelProperty(value = "电子归档id")
    private String electronicFileId;

    @ApiModelProperty(value = "案件分类： 案件、疑情")
    private String caseClass;

    @ApiModelProperty(value = "案件封面")
    private String caseThumbnail;

    @ApiModelProperty(value = "创建人")
    private Long creatorId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private Long updateUserId;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "删除标识")
    private Integer isDeleted;

    @ApiModelProperty(value = "删除时间")
    private Date deleteTime;

    @ApiModelProperty(value = "删除原因")
    private String deleteReason;

    @ApiModelProperty(value = "归档信息")
    private String archiveInfo;

    @ApiModelProperty(value = "归档报告")
    private String archiveReport;

    @ApiModelProperty(value = "责任人")
    private Long principal;

    @ApiModelProperty(value = "是否是经典案例")
    private Integer isClassicCase;

    @ApiModelProperty(value = "案发地图层级")
    private Long happenPlaceLevel;

    @ApiModelProperty(value = "报案人")
    private String reportPerson;

    @ApiModelProperty(value = "报案人证件号码")
    private String idCard;

    @ApiModelProperty(value = "报案人电话")
    private String contactPhone;

    @ApiModelProperty(value = "是否是合格案件")
    private Integer isqualified;

    @ApiModelProperty(value = "专网id")
    private Long sourceid;

    @ApiModelProperty(value = "专网网闸")
    private String isacrossborder;

    @ApiModelProperty(value = "审批状态")
    private Integer verifyingStatus;

    @ApiModelProperty(value = "审批关联的附件id")
    private Long withwaitverifyres;

    @ApiModelProperty(value = "来源系统")
    private String orginSystem;

    @ApiModelProperty(value = "系统编码")
    private String orginCode;

    @ApiModelProperty(value = "接触警编号")
    private String recieveCode;

    @ApiModelProperty(value = "交通工具")
    private String transport;

    @ApiModelProperty(value = "同步状态")
    private Integer syncStatus;

    @ApiModelProperty(value = "轨迹图片url")
    private String trackImageUrl;
    
    /**
     * 自己新增
     */
    private String createTimeStr;
    private String userName;
    private String startTimeStr;
    
    private String category;
    
    private String endTimeStr;
    
    private String categoryValueStr;
    


}