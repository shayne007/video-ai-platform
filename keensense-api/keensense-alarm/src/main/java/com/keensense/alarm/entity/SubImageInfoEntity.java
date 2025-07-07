package com.keensense.alarm.entity;

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
@TableName("alarm_sub_image_info")
public class SubImageInfoEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 图像标识GA/T 1400.1，图像信息基本要素 ID，视频、图像、文件
     */
    @TableId(value = "image_id")
    private String imageId;

    /**
     * 布控对象id
     */
    private String dispositionId;

    /**
     * 视频图像分析类型 / 自动分析事件类型，设备采集必选
     */
    private Integer eventSort;

    /**
     * 采集设备编码
     */
    private String deviceId;

    /**
     * 图像文件的存储路径，采用 URI 命名规则
     */
    private String storagePath;

    /**
     * 图像类型
     */
    private Integer type;

    /**
     * 图像文件格式
     */
    private Integer fileFormat;

    /**
     * 拍摄时间
     */
    @JSONField(format = "yyyyMMddHHmmss")
    private LocalDateTime shotTime;

    /**
     * 水平像素值
     */
    private Integer width;

    /**
     * 垂直像素值
     */
    private Integer height;

    /**
     * base64
     */
    private String data;


}
