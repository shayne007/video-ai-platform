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
@TableName("tb_relaytrack_detail")
public class TbRelaytrackDetail extends Model<TbRelaytrackDetail> {
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @TableId
    private String id;
    /**
     * 关联tb_relaytrack_task表任务ID
     */
    private String taskId;
    /**
     * 所属监控点id
     */
    private String cameraId;
    /**
     * 所属监控点名称
     */
    private String cameraName;
    /**
     * 任务分析serialnumber
     */
    private String analysisId;
    /**
     * 任务状态： 1 分析中 2 分析完成 3 分析失败
     */
    private Integer analysisStatus;
    /**
     * 分析进度
     */
    private Integer analysisProgress;
    /**
     * 提交时间
     */
    private Date createTime;
    /**
     * 完成时间
     */
    private Date finishTime;
    /**
     * 任务状态 0 运行中 1 成功 2 失败
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

    public TbRelaytrackDetail() {

    }

    public TbRelaytrackDetail(String id, String taskId, String cameraId, String cameraName, String analysisId) {
        super();
        this.id = id;
        this.taskId = taskId;
        this.cameraId = cameraId;
        this.cameraName = cameraName;
        this.analysisId = analysisId;
        this.analysisProgress = 0;
        this.analysisStatus = 0;
        this.createTime = new Date();
        this.status = 0;
        this.remark = "";
    }

    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
