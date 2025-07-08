package com.keensense.common.platform.bo.video;

import lombok.Data;

/**
 * @Author: zengyc
 * @Description: 人群密度参数
 * @Date: Created in 11:00 2019/9/25
 * @Version v0.1
 */
@Data
public class CrowdDensity {
    private String Id;//记录标识
    private int count;

    /**
     * 任务编号
     */
    private String serialnumber;
    /**
     * 抓拍场景图片访问地址
     */
    private String picUrl;
    /**
     * 时间 YYYYMMDDHHMMSS
     */
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

    /**
     * 导出图片路径
     */
    private String pictureLocalPath;

    private Integer alarmThreshold;
    private String densityInfo;
}
