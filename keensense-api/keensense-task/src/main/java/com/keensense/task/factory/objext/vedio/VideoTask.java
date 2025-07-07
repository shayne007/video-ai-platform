package com.keensense.task.factory.objext.vedio;

import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.TaskCleanLog;
import com.keensense.task.entity.TbAnalysisDetail;
import com.keensense.task.entity.TbAnalysisTask;
import com.keensense.task.entity.VasLice;
import com.keensense.task.entity.VasUrlEntity;
import com.keensense.task.factory.objext.ObjextTask;
import com.keensense.task.util.DateUtil;
import com.keensense.task.util.VideoExceptionUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 分片任务类
 * @Author: wujw
 * @CreateDate: 2019/5/20 13:33
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class VideoTask extends ObjextTask {

    @Override
    public int deleteTask(TbAnalysisTask tbAnalysisTask, int optSource) {
        int result = deleteTask(tbAnalysisTask.getId());
        return result;
    }

    /***
     * @description: 获取视频分片时间段
     * @param vasUrlEntity 录像url对象
     * @return: java.util.List<java.util.Map   <   java.lang.String   ,   java.lang.String>>
     */
    protected List<VasLice> getSliceList(VasUrlEntity vasUrlEntity) {
        return getSliceList(vasUrlEntity,null);
    }

    /**
     * 根据分析类型及起止时间划分分片
     * @param vasUrlEntity
     * @param analysisTypes
     * @return
     */
    protected  List<VasLice> getSliceList(VasUrlEntity vasUrlEntity,String  analysisTypes){
        //获取步长
        long taskTimeInterval = nacosConfig.getAnalysisTimeStep() * 1000L;
        long startSecond = vasUrlEntity.getStartTime();
        long endSecond = vasUrlEntity.getEndTime();
        List<VasLice> sliceList = new ArrayList<>();
        do {
            long endTemp = startSecond + taskTimeInterval;
            //判断最后剩余的时长小于步长，则最后剩余时间并入最后一次完整的步长中
            if (endSecond - endTemp < taskTimeInterval) {
                sliceList.add(new VasLice(startSecond, endSecond,analysisTypes));
                startSecond = endSecond;
            } else {
                sliceList.add(new VasLice(startSecond, endTemp,analysisTypes));
                startSecond = endTemp;
            }
        } while (startSecond < endSecond);
        return sliceList;

    }

    /***
     * @description: 获取校准时间字符串
     * @param startSecond 开始时间戳
     * @return: java.lang.String
     */
    protected String getEntryTime(long startSecond) {
        return DateUtil.formatDate(startSecond, DateUtil.DEFAULT_MATTER);
    }

    /***
     * @description: 获取分片下载路径
     * @param vasUrl 请求路径头
     * @param vasLice 分片时间对象
     * @return: java.lang.String
     */
    protected String getUrl(String vasUrl, VasLice vasLice, boolean isWsVideo) {
        if (isWsVideo) {
            return getWsUrl(vasUrl, vasLice);
        } else {
            return getUrl(vasUrl, vasLice);
        }
    }

    /***
     * @description: 获取分片下载路径
     * @param vasUrl 请求路径头
     * @param vasLice 分片时间对象
     * @return: java.lang.String
     */
    private String getUrl(String vasUrl, VasLice vasLice) {
        StringBuilder sb = new StringBuilder(vasUrl);
        sb.append("starttime=");
        sb.append(DateUtil.formatDate(vasLice.getStartSecond(), DateUtil.YYYYMMDDHHMMSS));
        sb.append("&endtime=");
        sb.append(DateUtil.formatDate(vasLice.getEndSecond(), DateUtil.YYYYMMDDHHMMSS));
        sb.append("&");
        return sb.toString();
    }

    /***
     * @description: 获取分片下载路径
     * @param vasUrl 请求路径头
     * @param vasLice 分片时间对象
     * @return: java.lang.String
     */
    private String getWsUrl(String vasUrl, VasLice vasLice) {
        StringBuilder sb = new StringBuilder(vasUrl);
        sb.append("startTime=");
        sb.append(DateUtil.formatDate(vasLice.getStartSecond(), DateUtil.YYYYMMDDHHMMSS));
        sb.append("&stopTime=");
        sb.append(DateUtil.formatDate(vasLice.getEndSecond(), DateUtil.YYYYMMDDHHMMSS));
        sb.append("&");
        return sb.toString();
    }

    /***
     * @description: 获取需要清理的子任务列表
     * @param details 子任务对象
     * @param tbAnalysisTask 主任务对象
     * @param optSource 操作类型
     * @return: java.util.List<com.keensense.task.entity.TaskCleanLog>
     */
    private List<TaskCleanLog> getTaskCleanLogList(List<TbAnalysisDetail> details, TbAnalysisTask tbAnalysisTask, int optSource) {
        List<TaskCleanLog> taskLogs = new ArrayList<>(details.size());
        Timestamp timestamp = DateUtil.now();
        for (TbAnalysisDetail detail : details) {
            if (detail.getAnalysisStatus() != null) {
                String id = getCleanId(detail);
                int retryCount = tbAnalysisDetailMapper.countByRetryId(id);
                if (retryCount == 0) {
                    taskLogs.add(new TaskCleanLog(id, tbAnalysisTask.getId(),tbAnalysisTask.getAnalyType(),
                            tbAnalysisTask.getTaskType(), timestamp, optSource));
                }
            } else {
                log.info("detail " + detail.getAnalysisId() + " has not start objext!");
            }
        }
        return taskLogs;
    }

    /***
     * @description: 获取要删除的子任务analySisId
     * @param detail 子任务对象
     * @return: java.lang.String
     */
    private String getCleanId(TbAnalysisDetail detail) {
        if (TaskConstants.PROGRESS_REUSE == detail.getProgress()) {
            return detail.getRemark();
        } else {
            return detail.getAnalysisId();
        }
    }

}
