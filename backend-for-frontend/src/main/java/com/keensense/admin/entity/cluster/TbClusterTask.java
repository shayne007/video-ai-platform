package com.keensense.admin.entity.cluster;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.keensense.common.platform.enums.ObjTypeEnum;
import com.keensense.common.platform.enums.TaskStatusEnum;
import lombok.Data;

import java.util.Date;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 14:01 2019/11/22
 * @Version v0.1
 */
@Data
@TableName("tb_cluster_task")
public class TbClusterTask {
    @TableId
    private String id;

    @TableField
    private String name;

    @TableField
    private String startTime;

    @TableField
    private String endTime;

    @TableField
    private Date createTime;

    @TableField
    private Integer objType;

    @TableField(exist = false)
    private String objTypeDesc;

    @TableField
    private String feature;

    @TableField
    private Integer status;

    @TableField(exist = false)
    private String statusDesc;

    public String getObjTypeDesc() {
        String objTypeDesc = ObjTypeEnum.get(this.objType).getDesc();
        return objTypeDesc;
    }

    public String getStatusDesc() {
        return TaskStatusEnum.get(this.status).getDesc();
    }

    @TableField
    private String qcameraIds;

    @TableField
    private String remark;

    @TableField
    private String cameraIds;

    @TableField
    private String cameraNames;

    @TableField
    private Integer threshold;
}
