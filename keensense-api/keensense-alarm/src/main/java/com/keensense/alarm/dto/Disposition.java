package com.keensense.alarm.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.keensense.alarm.entity.SubImageInfoEntity;
import com.keensense.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 布控对象
 * </p>
 *
 * @author ycl
 * @since 2019-05-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Disposition extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * GA/T 1400.1，布控ID
     */
    private String dispositionId;

    /**
     * 描述布控的主题和目标，
     * 布控时必选
     */
    private String title;


    /**
     * QST-busi--使用算法库进行布控,多个ID逗号隔开
     */
    private String regIds;

    /**
     * QST-busi--使用布控类型，1:白名单 2:黑名单 3:陌生人
     */
    private Integer controlType;

    /**
     * QST-busi--布控检索阈值
     */
    private Float scoreThreshold;

    /**
     * 车牌号
     */
    private String licensePlate;
    /**
     * 人、机动车、非机动车
     */
    private Integer dispositionCategory;

    /**
     * 对象特征键值对
     */
    private String targetFeature;

    /**
     * 图像路径，以目标图像特
     * 征进行布控时使用
     */
    private String targetImageUri;

    /**
     * 取值范围为 1～3，1 表示最高
     */
    private Integer priorityLevel;

    /**
     * 布控申请人,布控时必选
     */
    private String applicantName;

    /**
     * 申请人 个人或单位的手机号、邮
     * 箱等联系方式
     */
    private String applicantInfo;

    /**
     * 申请单位名称， 布控时必选
     */
    private String applicantOrg;

    /**
     * 布控开始时间
     */
    @JSONField(format = "yyyyMMddHHmmss")
    private LocalDateTime beginTime;

    /**
     * 布控结束时间
     */
    @JSONField(format = "yyyyMMddHHmmss")
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyyMMddHHmmss")
    private LocalDateTime createTime;

    /**
     * 0:布控；1：撤控
     */
    private Boolean operateType;

    /**
     * 卡口时使用；可带多个卡口 ID
     */
    private String tollgateList;

    /**
     * 布控范围:公安数据元内部标识符DE00913
     * 1:卡口；2：区域布控 布控时必选
     */
    private String dispositionRange;

    /**
     * 布控执行状态，0：布控中 1：已撤控
     * 2：布控到期 9：未布控;
     */
    private Integer dispositionStatus;

    /**
     * 区域布控 使用 (DispositionRange 为 2)； 实际需要执行布控的行政 区域代码，县际联动填 6 位行政区号码（GB/T2260 规定），地市联动填 4 位 行政区号码，省际联动填 2 位行政区号码，多个区域 间以”;” 级联接口根据布控行政区 代码找到下级视图
     */
    private String dispositionArea;

    /**
     * 进行该布控的理由
     */
    private String reason;

    /**
     * 多个号码间以英文半角分号”;”间隔，如 “139571xxxxx;139572xx xxx”
     */
    private String receiveMobile;

    /**
     * 告警信息接收地址 URL，级联接口时该地址控制直接 接收还是逐级转发
     */
    private String receiveAddr;

    /**
     * DE00065 撤控单位名称仅在撤销布控时使用
     */
    private String dispositionRemoveOrg;

    /**
     * 撤控人
     */
    private String dispositionRemovePerson;

    /**
     * 撤控时间
     */
    @JSONField(format = "yyyyMMddHHmmss")
    private LocalDateTime dispositionRemoveTime;

    /**
     * 撤控原因
     */
    private String dispositionRemoveReason;

    /**
     * 图片集合
     */
    private WrapSubImageInfo subImageList;

    /**
     * 告警次数
     */
    private long notificationCount;


}
