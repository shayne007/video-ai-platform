package com.keensense.task.entity.zkTaskEntity;

import java.util.Date;
import lombok.Data;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/5/23 11:23
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
public class ExectaskEndProgress {
    private String slaveId;
    private String taskId;
    private String serialNumber;
    private String url;
    private String errMsg;
    private Integer errMsgNumber;
    /**任务类型，0-objext目标分类，1-vlpr车牌识别，2-face人脸提取*/
    private Integer progress;
    private Integer taskType;
    private Integer status;
    private Integer retryCount;
    private Integer streamType;
    private Integer screenType;
    private Integer videoFrameWidth;
    private Integer videoFrameHeight;
    private Integer videoFps;
    private Date startTime;
    private Date endTime;

    /**
     * add by cuiss
     * 录像当前分析的位置
     */
    private Long currentPos;

}
