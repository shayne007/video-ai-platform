package com.keensense.admin.entity.cluster;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 17:36 2019/11/22
 * @Version v0.1
 */
@Data
@TableName("tb_cluster_task_detail")
public class TbClusterTaskDetail {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField
    private String pid;

    @TableField
    private Integer countNum;

    @TableField
    private Date createTime;

    @TableField
    private String result;
}
