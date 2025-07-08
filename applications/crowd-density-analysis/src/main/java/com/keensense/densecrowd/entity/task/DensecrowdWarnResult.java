package com.keensense.densecrowd.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 11:18 2020/7/20
 * @Version v0.1
 */
@Data
@TableName("densecrowd_warn_result")
public class DensecrowdWarnResult {
    @TableId(type = IdType.INPUT)
    private String id;//记录标识
    private int count;

    /**
     * 任务编号
     */
    private String serialnumber;
    /**
     * 抓拍场景图片访问地址
     */
    private String picUrl;

    private String createTime;
    /**
     * 该字段用于存储每个ROI的人群密度信息
     */
//    private DensityInfo densityInfo;

    /**
     * 人头位置信息
     */
    private String headPosition;

    /**
     * 监控点名称
     */
    private String cameraName;

    private Integer alarmThreshold;

    private String densityInfo;
}
