package com.keensense.task.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.keensense.task.constants.DeleteTaskConstants;
import com.keensense.task.util.DeleteTaskUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * @ClassName: TaskCleanRetryLog
 * @Description: 数据清理重试记录表
 * @Author: cuiss
 * @CreateDate: 2020/2/13 11:59
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
@Accessors(chain = true)
@TableName("task_clean_retry_log")
public class TaskCleanRetryLog {

    /**
     * 主键
     */
    private Long id;
    /**
     * 任务号
     */
    private String serialnumber;
    /**
     * 主任务
     */
    private String userserialnumber;
    /**
     * 任务创建时间
     */
    private Timestamp startTime;
    /**
     * 任务创建时间
     */
    private Timestamp endTime;
    /**
     * 任务创建时间
     */
    private Timestamp createTime;
    /**
     * 清理进度 0-等待删除 1-删除数据 2-删除快照 3-删除FTP文件  100-执行成功
     */
    @TableField("`status`")
    private Integer status;
    /**
     * 更新时间
     */
    private Timestamp updateTime;
    /**
     * 错误次数
     */
    private Integer retryCount;

    /**
     * 乐观锁版本号
     */
    private Integer version;

    public TaskCleanRetryLog(){}

    public TaskCleanRetryLog(String serialnumber, String userSerialnumber,Timestamp startTime,Timestamp endTime,
                         Timestamp createTime) {
        this.serialnumber = serialnumber;
        this.userserialnumber = userSerialnumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createTime = createTime;
        this.updateTime = createTime;
        this.status = DeleteTaskConstants.CLEAN_STATUS_WAIT;
        this.retryCount = 0;
        this.version = 0;
    }

}
