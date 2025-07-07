package com.keensense.task.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.keensense.task.constants.DeleteTaskConstants;
import com.keensense.task.util.DateUtil;
import com.keensense.task.util.DeleteTaskUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * @Description: 删除任务临时类，切换到fastdfs后可删除
 * @Author: wujw
 * @CreateDate: 2019/9/28 10:01
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
@Accessors(chain = true)
@TableName("task_clean_interim")
public class TaskCleanInterim {
    /**
     * 主键
     */
    private String id;
    /**
     * 任务号
     */
    private String serialnumber;
    /**
     * 主任务
     */
    private String userserialnumber;
    /**
     * 任务类型 1-实时 2-离线 3-录像 4-浓缩
     */
    private Integer taskType;
    /**
     * 分析类型
     */
    private String analyType;
    /**
     * 删除时间
     */
    private String ymd;
    /**
     * 清理进度 0-等待删除 1-删除数据 2-删除快照 3-删除FTP文件  100-执行成功
     */
    @TableField("`status`")
    private Integer status;
    /**
     * 操作来源 1-主动删除 2-定时任务删除
     */
    private Integer optSource;
    /**
     * 错误次数
     */
    private Integer retryCount;
    /**
     * 任务创建时间
     */
    private Timestamp createTime;
    /**
     * 更新时间
     */
    private Timestamp updateTime;
    /**
     * 节点信息
     */
    private String slaveId;

    public TaskCleanInterim(){}

    public TaskCleanInterim(String serialnumber, String userSerialnumber, String analyType, String ymd,
                            Integer taskType, Timestamp createTime, Integer optSource, String slaveId) {
        this.id = DeleteTaskUtil.getUuid();
        this.serialnumber = serialnumber;
        this.userserialnumber = userSerialnumber;
        this.analyType = analyType;
        this.taskType = taskType;
        this.ymd = ymd;
        this.slaveId = slaveId;
        this.createTime = createTime;
        this.updateTime = createTime;
        this.status = DeleteTaskConstants.CLEAN_STATUS_WAIT;
        this.optSource = optSource;
        this.retryCount = 0;
    }
}
