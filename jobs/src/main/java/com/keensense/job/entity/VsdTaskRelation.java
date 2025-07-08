package com.keensense.job.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.name;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author cuiss
 * @since 2018-11-05
 */
@TableName("vsd_task_relation")
public class VsdTaskRelation extends Model<VsdTaskRelation> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Setter
    @Getter
    private Long id;
    /**
     * vds_task_id
     */
    @TableField("task_id")
    @Setter
    @Getter
    private Long taskId;
    /**
     * 序列号，由外部定义
     */
    @Setter
    @Getter
    private String serialnumber;
    /**
     * 点位文件id
     */
    @TableField("camera_file_id")
    @Setter
    @Getter
    private Long cameraFileId;
    /**
     * 视频来源 1 点位 2离线视频
     */
    @TableField("from_type")
    @Setter
    @Getter
    private Long fromType;
    /**
     * 任务创建时间
     */
    @Setter
    @Getter
    private Date createtime;
    @Setter
    @Getter
    private Long createuser;
    @Setter
    @Getter
    private String c1;
    @Setter
    @Getter
    private String c2;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "VsdTaskRelation{" +
        ", id=" + id +
        ", taskId=" + taskId +
        ", serialnumber=" + serialnumber +
        ", cameraFileId=" + cameraFileId +
        ", fromType=" + fromType +
        ", createtime=" + createtime +
        ", createuser=" + createuser +
        ", c1=" + c1 +
        ", c2=" + c2 +
        "}";
    }
}
