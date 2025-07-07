package com.keensense.task.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @Description: 录像查询接口返回的实体类对象
 * @Author: wujw
 * @CreateDate: 2019/7/9 16:53
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
public class VideoTaskMap {

    private String id;

    private String name;

    private Integer progress;

    private String slaveIp;

    private Integer status;

    private String analyType;

    private String analyParam;

    private String detailId;

    private String analysisUrl;

    private String downloadUrl;

    private Integer taskType;

    private Timestamp createTime;

    private Timestamp lastUpdateTime;

    private Timestamp detailCreateTime;

    private Integer detailProgress;

    private String analysisId;

    private Integer vsdProgress;

    private String remark;

    private String userserialnumber;

    /**
     * 错误码
     */
    private String errcode;

    /**
     * 错误msg
     */
    private String errmsg;

    /**
     * 录像主进度,根据分析打点获取
     */
    private Integer mainProgress;
}
