package com.keensense.alarm.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.keensense.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author ycl
 * @since 2019-05-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("alarm_disposition_notification")
public class DispositionNotificationEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 告警对象 ID
     */
    @TableId(value = "notification_id")
    @JSONField(name = "NotificationID")
    private String notificationId;

    /**
     * 布控对象ID
     */
    @JSONField(name = "DispositionID")
    private String dispositionId;

    /**
     * QST-busi--检索的人脸ID
     */
    @JSONField(name = "FaceID")
    private String faceId;

    /**
     * QST-busi--检索的相似度
     */
    @JSONField(name = "Score")
    private Float score;

    /**
     * QST-busi--车牌号
     */
    private String licensePlate;


    /**
     * QST-busi--布控类别
     */
    private Integer dispositionCategory;

    /**
     * QST-busi--使用布控类型，1:白名单 2:黑名单 3:陌生人 4:闯入布控
     */
    private Integer controlType;

    /**
     * 1 已查看, 0 未查看告警信息
     */
    private Integer viewState;

    /**
     * 描述布控的主题和目标
     */
    @JSONField(name = "Title")
    private String title;

    /**
     * 触发时间
     */
    @JSONField(name = "TriggerTime", format = "yyyyMMddHHmmss")
    private LocalDateTime triggerTime;

    /**
     * GA/T 1400.1 图像信息内容要素 ID，   人、人脸、机动车、非机动车、物品、场景 等
     * 自动采集过车或过人记录 ID
     */
    @JSONField(name = "CntObjectID")
    private String cntObjectId;


    @JSONField(name = "CntObject")
    private String cntObject;

    /**
     * 自动采集的人员数据，结构
     */
    @JSONField(name = "PersonObject")
    private JSONObject personObject;
    /**
     * 自动采集的车辆数据，结构，过车信息必选
     */
    @JSONField(name = "MotorVehicleObject")
    private JSONObject motorVehicleObject;


}
