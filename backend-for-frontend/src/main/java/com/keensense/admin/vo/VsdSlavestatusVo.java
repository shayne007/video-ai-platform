package com.keensense.admin.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class VsdSlavestatusVo implements Serializable{

    private String id;

    /**
     * 机器Ip
     */
    private String slaveIp;
    
    /**
     * CPU使用率
     */
    private String cpuRate;
    
    /**
     * 内存使用量
     */
    private String memoryUsage;
 
    /**
     * 最大内存
     */
    private String memoryAll;
    
    /**
     * 内存使用量
     */
    private int  memoryUsageRate;
    
    private double diskAll;
    
    private double diskUsage;
    
    private double diskRemain;
    

    private Byte valid;

    private String validStr;
    
    private Short objextCapability;
    private Short objextNum;
    
    private Short vlprCapability;
    private Short vlprNum;
    private Short faceCapability;
    private Short faceNum;

    private String reserve;

    private Date lastupdateTime;

    private String load;
    
    private String payload;
    
    /**
     * 任务数量
     */
    private int taskNum;

    private String slaveId;
    
    
    
}
