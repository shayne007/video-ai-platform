package com.keensense.task.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import com.keensense.task.constants.TaskConstants;
import com.keensense.task.util.DateUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author jobob
 * @since 2019-05-06
 */
@Data
@Accessors(chain = true)
@TableName("tb_analysis_task")
public class TbAnalysisTask implements Serializable {

    private String id;
    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务描述
     */
    @TableField("'desc'")
    private String desc;

    /**
     * 分析类型 objext vlpr
     */
    private String analyType;

    /**
     * 分析参数
     */
    private String analyParam;

    /**
     * 分片数量
     */
    private Integer sliceNumber;

    /**
     * 提交时间
     */
    private Timestamp createTime;

    /**
     * 完成时间
     */
    private Timestamp finishTime;

    /**
     * 最后更改时间
     */
    private Timestamp lastupdateTime;

    /**
     * 创建用户
     */
    private String createUserid;

    /**
     * 任务类型 1 实时流分析 2 离线文件分析 3 录像分析
     */
    private Integer taskType;

    /**
     * 所属监控点id
     */
    private String cameraId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 任务状态 0 运行中 1 已停止 2 已删除
     */
    private Integer status;

    /**
     * 任务分析进度 0-100
     */
    private Integer progress;

    /**
     * 设备ID
     */
    private String deviceId;

    @TableField(exist = false)
    private Long timeStamp;

    public TbAnalysisTask(){
        super();
    }

    /**
     * @Description: 初始化对象
     * @param id          流水号
     * @param analyType   分析类型 objext,vlpr,picture
     * @param sliceNumber 分片数量
     * @param taskType    任务类型 1 实时流分析  2 离线文件分析  3 录像分析 4图片流分析
     * @param cameraId    所属监控点id
     * @param deviceId    设备ID
     * @param analyParam  分析参数
     * @return TbAnalysisTask
     */
    public static TbAnalysisTask init(String id, String analyType, int sliceNumber, int taskType, String cameraId,
                                             String deviceId,  String analyParam) {
        TbAnalysisTask analyTask = new TbAnalysisTask();
        analyTask.setId(id);
        analyTask.setAnalyType(analyType);
        analyTask.setAnalyParam(analyParam);
        analyTask.setSliceNumber(sliceNumber);
        analyTask.setCreateTime(DateUtil.now());
        analyTask.setTaskType(taskType);
        analyTask.setCameraId(cameraId);
        analyTask.setDeviceId(deviceId);
        analyTask.setStatus(TaskConstants.TASK_DELETE_FALSE);
        return analyTask;
    }

}
