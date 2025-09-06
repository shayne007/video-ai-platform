package com.keensense.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * @ClassName: CameraAnalysisTrack
 * @Description: camera_analysis_track映射类
 * @Author: cuiss
 * @CreateDate: 2020/4/13 10:27
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
@Accessors(chain = true)
@TableName("camera_analysis_track")
@ToString
@EqualsAndHashCode
public class CameraAnalysisTrack extends CameraAnalysisTrackBase{

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 点位编号
     */
    @TableField("camera_id")
    private String cameraId;

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

    public CameraAnalysisTrack(){}
    public CameraAnalysisTrack(String cameraId,Integer analysisType,Integer trackDay,Integer trackHour,String trackValue,Timestamp createTime,Timestamp lastUpdateTime){
        this.cameraId = cameraId;
        this.analysisType = analysisType;
        this.trackDay = trackDay;
        this.trackHour = trackHour;
        this.trackValue = trackValue;
        this.createTime = createTime;
        this.lastUpdateTime = lastUpdateTime;
    }
}
