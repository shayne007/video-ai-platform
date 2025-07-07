package com.keensense.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

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
@TableName("tb_analysis_detail")
public class TbAnalysisDetail {

    private String id;
    /**
     * 关联tb_analysis_task表任务ID
     */
    private String taskId;
    /**
     * 任务进度
     */
    private Integer progress;
    /**
     * 校准时间
     */
    private String entryTime;
    /**
     * 任务分析地址
     */
    private String analysisUrl;
    /**
     * 任务分析serialnumber
     */
    private String analysisId;
    /**
     * 任务状态： 1 待提交 2 正在处理 3已完成 4处理失败
     */
    private Integer analysisStatus;
    /**
     * 分析进度
     */
    private Integer analysisProgress;
    /**
     * 录像下载地址
     */
    private String downloadUrl;
    /**
     * 录像下载id
     */
    private String downloadId;
    /**
     * 下载状态： 1 待提交 2 正在处理 3已完成 4处理失败
     */
    private Integer downloadStatus;
    /**
     * 下载进度
     */
    private Integer downloadProgress;
    /**
     * 录像下载后文件信息
     */
    private String downloadFile;
    /**
     * 录像下载重试次数
     */
    private Integer downloadRetry;
    /**
     * 提交转码url地址
     */
    private String transcodeUrl;
    /**
     * 转码id
     */
    private String transcodeId;
    /**
     * 任务状态： 1 待提交 2 正在处理 3已完成 4处理失败
     */
    private Integer transcodeStatus;
    /**
     * 转码进度
     */
    private Integer transcodeProgress;
    /**
     * 转码后文件信息
     */
    private String transcodeFile;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 结束时间
     */
    private Timestamp finishTime;
    /**
     * 最后更新时间
     */
    private Timestamp lastupdateTime;
    /**
     * 备注或者重用主键
     */
    private String remark;
}
