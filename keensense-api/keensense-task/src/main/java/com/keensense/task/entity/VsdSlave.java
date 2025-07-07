package com.keensense.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/7/31 11:00
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
@TableName("vsd_slavestatus")
public class VsdSlave {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String slaveIp;

    private Integer valid;

    private Integer objextCapability;

    private Integer vlprCapability;

    private Integer faceCapability;

    private String payload;

    private String reserve;

    private Timestamp lastupdateTime;

    private String slaveId;
}
