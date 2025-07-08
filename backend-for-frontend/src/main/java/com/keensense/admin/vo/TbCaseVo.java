package com.keensense.admin.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TbCaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 主办侦查员
     */
    private String caseHandleUser;
    /**
     * 协办侦查员
     */
    private String caseHandleInvestor;

    /**
     * 所属派出所编码
     */
    private String caseStationId;

    /**
     * 经度
     */
    private String caseLongitude;
    /**
     * 纬度
     */
    private String caseLatitude;
    /**
     * 删除标识  1:已删除  0:正常
     */
    private Integer deleted;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
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
     * 轨迹图片
     */
    private String trackImageUrl;



    /*====临时字段====*/
    /**
     * 案件状态描述
     */
    private String caseStatusStr;
    /**
     * 创建时间
     */
    private String createTimeStr;
    /**
     * 更新时间
     */
    private String lastUpdateTimeStr;

    /**
     * 案发时间Str
     */
    private String caseStartTimeStr;
    /**
     * 结案时间Str
     */
    private String caseEndTimeStr;
    /**
     * 案件类别名称
     */
    private String caseOptionName;

}
