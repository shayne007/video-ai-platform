package com.keensense.task.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: CameraAnalysisTrackBase
 * @Description: CameraAnalysisTrack基础信息
 * @Author: cuiss
 * @CreateDate: 2020/4/17 10:10
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
public class CameraAnalysisTrackBase implements Serializable {

    protected static final long serialVersionUID = -1L;

    /**
     * 分析类型  1：人行/2：车辆/3：人脸/4：骑行
     */
    @TableField("analysis_type")
    protected Integer analysisType;

    /**
     * 轨迹日期，形如yyyymmdd，如20200322
     */
    @TableField("track_day")
    protected Integer trackDay;

    /**
     * 轨迹小时，1-24
     */
    @TableField("track_hour")
    protected Integer trackHour;

    /**
     * 用60位二进制字符串代表60分钟是否打点
     */
    @TableField("track_value")
    protected String trackValue;
}
