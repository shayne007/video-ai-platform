package com.keensense.job.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
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
@TableName("vsd_task")
public class VsdTask extends Model<VsdTask> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Setter
    @Getter
    private Long id;
    /**
     * 序列号，由外部定义
     */
    @Setter
    @Getter
    private String serialnumber;
    /**
     * 任务类型,objext:目标分类，vlpr:车牌识别,face:人脸提取
     */
    @Setter
    @Getter
    private String type;
    /**
     * 任务的处理进度,0~100
     */
    @Setter
    @Getter
    private Integer progress;
    /**
     * 任务是否有效,0:无效，1:有效
     */
    @Setter
    @Getter
    private Integer isvalid;
    /**
     * 任务参数
     */
    @Setter
    @Getter
    private String param;
    /**
     * 任务状态,0：等待处理 1：正在处理 2：处理成功 3：处理失败
     */
    @Setter
    @Getter
    private Integer status;
    /**
     * 保留参数
     */
    @Setter
    @Getter
    private String reserve;
    /**
     * 任务异常之后重试的次数
     */
    @Setter
    @Getter
    private Integer retrycount;
    /**
     * 处理服务器ip
     */
    @Setter
    @Getter
    private String slaveip;
    /**
     * 任务结束时间，针对实时任务
     */
    @Setter
    @Getter
    private Date endtime;
    /**
     * 任务创建时间
     */
    @Setter
    @Getter
    private Date createtime;
    /**
     * 序列号，由外部定义
     */
    @Setter
    @Getter
    private String userserialnumber;
    /**
     * 视频录入时间
     */
    @Setter
    @Getter
    private Date entrytime;
    /**
     * 每秒帧数
     */
    @Setter
    @Getter
    private Integer framerate;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "VsdTask{" +
        ", id=" + id +
        ", serialnumber=" + serialnumber +
        ", type=" + type +
        ", progress=" + progress +
        ", isvalid=" + isvalid +
        ", param=" + param +
        ", status=" + status +
        ", reserve=" + reserve +
        ", retrycount=" + retrycount +
        ", slaveip=" + slaveip +
        ", endtime=" + endtime +
        ", createtime=" + createtime +
        ", userserialnumber=" + userserialnumber +
        ", entrytime=" + entrytime +
        ", framerate=" + framerate +
        "}";
    }
}
