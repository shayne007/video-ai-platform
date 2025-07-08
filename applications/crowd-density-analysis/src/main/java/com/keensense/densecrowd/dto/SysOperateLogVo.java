package com.keensense.densecrowd.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统操作日志类
 */
@Data
public class SysOperateLogVo implements Serializable {
    private Long id;

    private Long userId;

    private Long deptId;

    private Integer visitNum;

    private String operateIp;

    private String operateModule;

    private Date createTime;

    private Date updateTime;

    private String userName;

    private String deptName;

    private String latestTime;

}