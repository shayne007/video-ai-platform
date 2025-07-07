package com.keensense.task.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Description: 录像列表展示对象
 * @Author: wujw
 * @CreateDate: 2019/5/21 16:47
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
public class VideoTaskVO {

    private String serialnumber;

    private String name;

    private String type;

    private String videoUrl;

    private Integer status;

    private Integer progress;

    private Timestamp createTime;

    private String slaveip;

    private String summaryVideoUrl;

    private String summaryDatHttp;

    private List<VideoSlice> subTasks;

    /**
     * 错误码
     */
    private String errcode;

    /**
     * 错误msg
     */
    private String errmsg;
}
