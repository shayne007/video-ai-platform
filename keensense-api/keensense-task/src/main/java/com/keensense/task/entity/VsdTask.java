package com.keensense.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author jobob
 * @since 2019-05-07
 */
@Data
@Accessors(chain = true)
@TableName("vsd_task")
public class VsdTask {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 序列号，由外部定义
     */
    private String serialnumber;

    /**
     * 任务类型,objext:目标分类，vlpr:车牌识别,face:人脸提取
     */
    private String type;

    /**
     * 任务的处理进度,0~100
     */
    private Integer progress;

    /**
     * 任务是否有效,0:无效，1:有效
     */
    @TableField("isvalid")
    private Integer isValid;

    /**
     * 任务参数
     */
    private String param;

    /**
     * 任务状态,0：等待处理 1：正在处理 2：处理成功 3：处理失败
     */
    @TableField("STATUS")
    private Integer status;

    /**
     * 保留参数
     */
    private String reserve;

    /**
     * 任务异常之后重试的次数
     */
    @TableField("retrycount")
    private Integer retryCount;

    /**
     * 处理服务器ip
     */
    private String slaveip;

    /**
     * 任务结束时间，针对实时任务
     */
    private Timestamp endtime;

    /**
     * 任务创建时间
     */
    private Timestamp createtime;

    /**
     * 序列号，由外部定义
     */
    private String userserialnumber;

    /**
     * 视频录入时间
     */
    private Timestamp entrytime;

    /**
     * 每秒帧数
     */
    private Integer framerate;

    /**
     * 错误码
     */
    private Integer errcode;

    /**
     * 错误码对应的错误信息
     */
    private String errmsg;

    /**
     * 乐观锁
     */
    private Integer version;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 任务类型:1/实时任务;2/离线视频;3/联网录像;4/浓缩
     */
    @TableField("task_type")
    private Integer taskType;

    public VsdTask(Long id){
        this.id = id;
    }
    public VsdTask(){}
}
