package com.keensense.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.keensense.task.constants.DeleteTaskConstants;
import com.keensense.task.util.DeleteTaskUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * @Description: 数据清理对象
 * @Author: wujw
 * @CreateDate: 2019/6/24 10:09
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
@Accessors(chain = true)
@TableName("task_clean_ymddata")
public class TaskCleanYmdData {

    /**
     * 主键
     */
    private String id;
    /**
     * 删除时间
     */
    private String ymd;
    /**
     * 任务号
     */
    private String serialnumber;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 清理进度 0-等待删除 1-删除数据 2-删除快照 3-删除FTP文件  100-执行成功
     */
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
     * 分析类型
     */
    private String analyType;

    public TaskCleanYmdData(){
        super();
    }

    public TaskCleanYmdData(String ymd, String serialnumber, String analyType, Timestamp createTime) {
        this.id = DeleteTaskUtil.getUuid();
        this.ymd = ymd;
        this.serialnumber = serialnumber;
        this.analyType = analyType;
        this.createTime = createTime;
        this.updateTime = createTime;
        this.status = DeleteTaskConstants.CLEAN_STATUS_WAIT;
        this.retryCount = 0;
    }
}
