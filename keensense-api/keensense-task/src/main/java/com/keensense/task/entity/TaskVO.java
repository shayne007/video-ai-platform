package com.keensense.task.entity;

import com.keensense.task.util.NullPointUtil;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/5/20 15:07
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
public class TaskVO {

    private String serialnumber;

    private String type;

    private Integer status;

    private Integer isvalid;

    private Integer progress;

    private String createUserId;

    private Timestamp createTime;

    private Timestamp endTime;

    private String slaveip;

    private String cameraId;

    private String deviceId;

    private String summaryVideoUrl;

    private String summaryDatHttp;

    @Override
    public String toString(){
        return "TaskVO [serialnumber=" + serialnumber + ", type=" + type + ",status="+ status
                + ", isvalid=" + isvalid  + ", progress=" + progress
                + ", createTime=" + NullPointUtil.formatTimestamp(createTime)
                + ", endTime="+ NullPointUtil.formatTimestamp(endTime)
                + ", deviceId="+ deviceId
                + ", summaryVideoUrl=" + summaryVideoUrl  + ", summaryDatHttp=" + summaryDatHttp
                + ", slaveip=" + slaveip + ", cameraId=" + cameraId + "]";
    }
}
