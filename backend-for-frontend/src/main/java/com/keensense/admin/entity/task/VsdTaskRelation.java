package com.keensense.admin.entity.task;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vsd_task_relation")
public class VsdTaskRelation extends Model<VsdTaskRelation> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * vds_task_id
     */
    private Long taskId;
    /**
     * 序列号，由外部定义
     */
    private String serialnumber;
    /**
     * 点位文件id
     */
    private String cameraFileId;
    /**
     * 视频来源 1 点位 2离线视频
     */
    private Integer fromType;
    /**
     * 任务创建时间
     */
    private Date createtime;
    /**
     *
     */
    private Long createuser;
    /**
     *
     */
    private String c1;
    /**
     *
     */
    private String c2;

    private Integer taskStatus;

    private Integer isvalid;

    private Integer taskProgress;

    private String type;

    private String remark;

    private String taskUserId;

    private String taskName;

    private String cameraId;

    private Date endTime;

    private Date lastUpdateTime;

    private String url;

    private String slaveip;

    private Integer overlineType;


    private Date entryTime;

    private Integer enablePartial;


    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
