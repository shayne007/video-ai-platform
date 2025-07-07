package com.keensense.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * @ClassName: CameraAnalysisTrack
 * @Description: camera_analysis_track_his映射类
 * @Author: cuiss
 * @CreateDate: 2020/4/13 10:27
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
@Accessors(chain = true)
@TableName("camera_analysis_track_his")
public class CameraAnalysisTrackHis {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 点位编号
     */
    private String cameraId;

    /**
     * 分析类型  1：人行/2：车辆/3：人脸/4：骑行
     */
    private Integer analysisType;

    /**
     * 轨迹日期，形如yyyymmdd，如20200322
     */
    private Integer trackDay;

    /**
     * 轨迹小时，1-24
     */
    private Integer trackHour;

    /**
     * 用60位二进制字符串代表60分钟是否打点
     */
    private String trackValue;

    /**
     * 版本号  default 0
     */
    private Integer version;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Timestamp createTime;

    /**
     * 最近更新时间
     */
    @TableField("last_update_time")
    private Timestamp lastUpdateTime;

    public CameraAnalysisTrackHis(){}
    public CameraAnalysisTrackHis(String cameraId,Integer analysisType,Integer trackDay,Integer trackHour,String trackValue,Timestamp createTime,Timestamp lastUpdateTime){
        this.cameraId = cameraId;
        this.analysisType = analysisType;
        this.trackDay = trackDay;
        this.trackHour = trackHour;
        this.trackValue = trackValue;
        this.createTime = createTime;
        this.lastUpdateTime = lastUpdateTime;
    }

}
