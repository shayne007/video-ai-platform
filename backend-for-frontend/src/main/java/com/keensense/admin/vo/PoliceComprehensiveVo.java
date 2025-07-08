package com.keensense.admin.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PoliceComprehensiveVo implements Serializable {

    private String lawcaseId;

    private String caseName;

    private String caseCode;

    private String categoryValue;

    private String propertyValue;

    private String caseState;

    private Date startTime;

    private Date endTime;

    private Date recieveTime;

    private Date resolveTime;

    private String engageOrg;

    private String reporterName;

    private String reporterPhone;

    private String locationId;

    private String policeStation;

    private String caseCommunity;

    private String locationType;

    private String locationName;

    private String locationDetail;

    private String lontitude;

    private String latitude;

    private String caseArea;

    private String casePlaces;

    private String placesType;

    private String caseDesc;

    private String caseMoney;

    private String moneyLose;

    private String choosePlace;

    private String choosePerson;

    private String chooseItem;

    private String chooseDate;

    private String chooseTime;

    private String chooseChance;

    private String chooseWeather;

    private String caseMethodFeaturesValue;

    private String stealMethod;

    private String disguiseMethod;

    private String prepareMethod;

    private String orgWay;

    private String colludeWay;

    private String caseWay;

    private String exploreWay;

    private String actionWay;

    private String isCross;

    private String busWay;

    private String upStation;

    private String downStation;

    private String caseTool;

    private String oprateResult;

    private String keyWords;

    private String remarkAdvice;

    private String hasSupect;

    private String hasSurvey;

    private Integer isOldData;

    private String electronicFileId;

    private String caseClass;

    private String caseThumbnail;

    private Long creatorId;

    private Date createTime;

    private Long updateUserId;

    private Date updateTime;

    private Integer isDeleted;

    private Date deleteTime;

    private String deleteReason;

    private String archiveInfo;

    private String archiveReport;

    private Long principal;

    private Integer isClassicCase;

    private Long happenPlaceLevel;

    private String reportPerson;

    private String idCard;

    private String contactPhone;

    private Integer isqualified;

    private Long sourceid;

    private String isacrossborder;

    private Integer verifyingStatus;

    private Long withwaitverifyres;

    private String orginSystem;

    private String orginCode;

    private String recieveCode;

    private String transport;

    private Integer syncStatus;
    
    /**
     * 自己新增
     */
    private String createTimeStr;
    private String userName;
    private String startTimeStr;
    
    private String category;
    
    private String endTimeStr;
    
    private String categoryValueStr;
    
    // 轨迹图片url
    private String trackImageUrl;

}