package com.keensense.task.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @Description: 录像分片对象
 * @Author: wujw
 * @CreateDate: 2019/5/21 18:37
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
public class VideoSlice {

    private String serialnumber;

    private Timestamp createTime;

    private String userserialnumber;

    private Integer progress;

    private Integer status;

    private String url;

    private String taskId;

    private String remark;

    private Timestamp lastUpdateTime;


    /**
     * 错误码
     */
    private String errcode;

    /**
     * 错误msg
     */
    private String errmsg;


}
