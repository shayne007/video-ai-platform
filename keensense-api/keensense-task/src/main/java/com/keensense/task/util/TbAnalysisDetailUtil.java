package com.keensense.task.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.common.config.SpringContext;
import com.keensense.task.config.NacosConfig;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.TbAnalysisDetail;
import com.keensense.task.entity.TbAnalysisTask;

/**
 * @Description: detail定时任务工具类
 * @Author: wujw
 * @CreateDate: 2019/5/16 17:50
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class TbAnalysisDetailUtil {

    private TbAnalysisDetailUtil(){}

    private static NacosConfig nacosConfig = SpringContext.getBean(NacosConfig.class);

    /**
     * 下载
     */
    public static final int DOWNLOAD = 1;
    /**
     * 转码
     */
    public static final int TRANSCODE = 2;
    /**
     * 分析
     */
    public static final int ANALYSIS = 3;

    /***
     * @description: 更新进度
     * @param detail 任务对象
     * @param type   操作类型
     * @param progress 进度
     * @return: com.keensense.task.entity.TbAnalysisDetail
     */
    public static TbAnalysisDetail updateProgress(TbAnalysisDetail detail, int type, int progress, TbAnalysisTask analysisTask) {
        if (DOWNLOAD == type) {
            // 下载
            detail.setDownloadProgress(progress);
            if (TaskConstants.PROGRESS_FAILED == progress) {
                detail.setProgress(progress);
            } else {
                detail.setProgress(33 * progress / 100);
            }
        } else if (TRANSCODE == type) {
            // 转码
            detail.setTranscodeProgress(progress);
            if (TaskConstants.PROGRESS_FAILED == progress) {
                detail.setProgress(progress);
            } else {
                detail.setProgress(34 + 16 * progress / 100);
            }
        } else {
            detail.setAnalysisProgress(progress);
            // 分析
            if (TaskConstants.PROGRESS_FAILED == progress) {
                detail.setProgress(progress);
                return detail;
            }else if (analysisTask != null && analysisTask.getTaskType() == TaskConstants.TASK_TYPE_VIDEO
                    && 2 == nacosConfig.getAnalysisMethod()) {
                detail.setProgress(50 + progress / 2);
            }else{
                detail.setProgress(progress);
            }
            return detail;
        }
        return detail;
    }

    /***
     * @description: 查询等待或者的任务
     * @param column 数据库列明
     * @return: com.baomidou.mybatisplus.core.conditions.Wrapper
     */
    public static QueryWrapper<TbAnalysisDetail> queryAnalysisDetailByStatus(String column) {
        return new QueryWrapper<TbAnalysisDetail>()
                .in(column, TaskConstants.OPERAT_TYPE_WATI, TaskConstants.OPERAT_TYPE_RUNNING);
    }

}
