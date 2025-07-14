package com.keensense.task.schedule;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.common.exception.VideoException;
import com.keensense.task.config.NacosConfig;
import com.keensense.task.constants.DeleteTaskConstants;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.TaskCleanInterim;
import com.keensense.task.entity.TaskCleanLog;
import com.keensense.task.entity.TaskCleanYmdData;
import com.keensense.task.entity.TbAnalysisTask;
import com.keensense.task.listener.StartListener;
import com.keensense.task.service.IDeleteTaskService;
import com.keensense.task.service.ITaskCleanInterimService;
import com.keensense.task.service.ITaskCleanLogService;
import com.keensense.task.service.ITaskCleanYmdDataService;
import com.keensense.task.service.ITbAnalysisTaskService;
import com.keensense.task.util.DateUtil;
import com.keensense.task.util.DeleteTaskUtil;
import com.keensense.task.util.oldclean.MysqlUtil;
import com.keensense.task.util.oldclean.ShellUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * @Description: 数据清理定时任务
 * @Author: wujw
 * @CreateDate: 2019/6/24 10:09
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Component
@Slf4j
public class CleanDataSchedule {

    @Autowired
    private IDeleteTaskService deleteTaskService;
    @Autowired
    private ITbAnalysisTaskService tbAnalysisTaskService;
    @Autowired
    private ITaskCleanYmdDataService taskCleanYmdDataService;
    @Autowired
    private ITaskCleanInterimService taskCleanInterimService;
    @Autowired
    private ITaskCleanLogService taskCleanLogService;
    @Autowired
    private NacosConfig nacosConfig;

    private static long startTime = getStartTime();
    private static long endTime = getEndTime();

    /***
     * @description: 每天晚上1点半重置数据
     */
    @Scheduled(cron = "0 30 21 * * ?")
    public void initData(){
        startTime = getStartTime();
        endTime = getEndTime();
        log.info("init startTime = " + startTime);
        log.info("init endTime = " + endTime);
        //重置状态
        taskCleanInterimService.resetRunningStatus();
    }

    /***
     * @description: 根据磁盘容量检索删除数据
     */
    @Scheduled(cron = "0 1/10 * * * ?")
    public void getDeleteTaskBySpaceSchedule() {
        if(isExecuteTime()){
            getDeleteTaskBySpace();
        }
    }

    /***
     * @description: 根据时间删除检索数据
     */
    @Scheduled(cron = "0 40 1 * * ?")
    public void getDeleteTaskByDateSchedule() {
        if (!StartListener.zkDistributeLock.hasCurrentLock()) {
            return;
        }
        int cleanOffLineTaskDate = nacosConfig.getCleanOfflineSaveDay();
        if (cleanOffLineTaskDate > 0) {
            LocalDateTime endLocalTime = DeleteTaskUtil.plusEndTime(LocalDateTime.now(), 0L - cleanOffLineTaskDate);
            deleteOfficeBySpace(endLocalTime);
        }
        deleteOnlineByTime();
    }

    @Scheduled(cron = "1/59 * * * * ?")
    public void deleteByTask() {
        if (!StartListener.zkDistributeLock.hasCurrentLock()) {
            return;
        }
        List<TaskCleanLog> list = taskCleanLogService.getListForDelete(DeleteTaskConstants.CLEAN_STATUS_WAIT);
        for (TaskCleanLog taskCleanLog : list) {
            if (deleteTaskService.deleteEs(taskCleanLog.getSerialnumber(), null, taskCleanLog.getAnalyType())) {
                taskCleanLog.setStatus(DeleteTaskConstants.CLEAN_STATUS_RUNNING);
            } else {
                taskCleanLog.setRetryCount(taskCleanLog.getRetryCount() + 1);
            }
            taskCleanLog.setUpdateTime(DateUtil.now());
        }
        if(!list.isEmpty()){
            taskCleanLogService.updateBatch(list);
        }
    }

    /***
     * @description: 进行第二次补充删除,同时准备进行删除图片
     */
    @Scheduled(cron = "0 1/10 * * * ?")
    public void deleteTwice() {
        if (!StartListener.zkDistributeLock.hasCurrentLock()) {
            return;
        }
        if(isExecuteTime()){
            List<TaskCleanLog> list = taskCleanLogService.getListForDelete(DeleteTaskConstants.CLEAN_STATUS_RUNNING);
            list.forEach(taskCleanLog -> {
                if (deleteTaskService.deleteEs(taskCleanLog.getSerialnumber(), null, taskCleanLog.getAnalyType())) {
                    if(deleteTaskService.addDeleteImageRecord(taskCleanLog.getSerialnumber(), taskCleanLog.getSerialnumber(),
                            taskCleanLog.getAnalyType(), null, taskCleanLog.getTaskType(), taskCleanLog.getOptSource())){
                        taskCleanLog.setStatus(DeleteTaskConstants.CLEAN_STATUS_SUCCESS);
                    } else {
                        taskCleanLog.setRetryCount(taskCleanLog.getRetryCount() + 1);
                    }
                } else {
                    taskCleanLog.setRetryCount(taskCleanLog.getRetryCount() + 1);
                }
                taskCleanLog.setUpdateTime(DateUtil.now());
            });
            if(!list.isEmpty()){
                taskCleanLogService.updateBatch(list);
            }
        }
    }

    /***
     * @description: 根据时间以及任务号删除数据
     */
    @Scheduled(cron = "0 1/10 * * * ?")
    public void deleteByDateSchedule() {
        if (!StartListener.zkDistributeLock.hasCurrentLock()) {
            return;
        }
        if(isExecuteTime()){
            deleteByDate();
        }
    }

    /***
     * @description: 删除快照定时任务
     * @return: void
     */
    @Scheduled(cron = "0 2/10 * * * ?")
    public void deletePictureSchedule() {
        if(isExecuteTime()){
            deletePicture();
        }
    }

    /***
     * @description: 根据磁盘容量检索删除数据
     */
    public void getDeleteTaskBySpace() {
        //旧的清理策略所有机器都需要执行容量扫描
        if (MysqlUtil.isMysql()) {
            if (getOldDataSpace()) {
                log.info("准备按空间进行磁盘清理!");
                //实时任务清理并获取清理时间节点，时间节点可能为Null
                LocalDateTime localDateTime = deleteOnlineBySpace();
                //获取删除离线任务
                deleteOfficeBySpace(localDateTime);
            }
        } else {
            //新的清理策略只需要主机执行即可
            if (StartListener.zkDistributeLock.hasCurrentLock()) {
                int exceCount = taskCleanInterimService.selectListForExce();
                if(exceCount <= 50){
                    if(deleteTaskService.getDataSpace()){
                        log.info("准备按空间进行磁盘清理!");
                        //实时任务清理并获取清理时间节点，时间节点可能为Null
                        LocalDateTime localDateTime = deleteOnlineBySpace();
                        //获取删除离线任务
                        deleteOfficeBySpace(localDateTime);
                    }
                }else{
                    log.info("delete image records more than 100, count = " + exceCount);
                }
            }
        }
    }

    /***
     * @description: 根据时间以及任务号删除数据
     */
    private void deleteByDate() {
        List<TaskCleanYmdData> list = taskCleanYmdDataService.getListForDelete();
        log.info("delete es data by ymd get list size = " + list.size());
        for (TaskCleanYmdData taskCleanYmdData : list) {
            if (deleteTaskService.deleteEs(taskCleanYmdData.getSerialnumber(), taskCleanYmdData.getYmd(), taskCleanYmdData.getAnalyType())) {
                if(deleteTaskService.addDeleteImageRecord(taskCleanYmdData.getSerialnumber(),
                        taskCleanYmdData.getSerialnumber(), taskCleanYmdData.getAnalyType(),taskCleanYmdData.getYmd(),
                        TaskConstants.TASK_TYPE_ONLINE, DeleteTaskConstants.CLEAN_DATA_SOURCE_AUTO)){
                    taskCleanYmdData.setStatus(DeleteTaskConstants.CLEAN_STATUS_SUCCESS);
                    taskCleanYmdData.setUpdateTime(DateUtil.now());
                }
            } else {
                taskCleanYmdData.setRetryCount(taskCleanYmdData.getRetryCount() + 1);
            }
        }
        if(!list.isEmpty()){
            taskCleanYmdDataService.updateBatch(list);
        }
    }

    /***
     * @description: 删除快照定时任务
     */
    public void deletePicture() {
        if (MysqlUtil.isMysql()) {
            List<TaskCleanInterim> list = taskCleanInterimService.selectListBySlave(MasterSchedule.getMasterId());
            list.forEach(taskCleanInterim -> {
                if (ShellUtil.deletePicture(taskCleanInterim.getSerialnumber(), taskCleanInterim.getYmd())) {
                    taskCleanInterim.setStatus(DeleteTaskConstants.CLEAN_STATUS_SUCCESS);
                    taskCleanInterimService.updateById(taskCleanInterim);
                }
            });
        }else{
            if(StartListener.zkDistributeLock.hasCurrentLock()){
                //主机执行清理图片策略
                List<TaskCleanInterim> list = taskCleanInterimService.selectListBySlave(null);
                Timestamp now = DateUtil.now();
                list.forEach(taskCleanInterim -> {
                    if (deleteTaskService.deleteImages(taskCleanInterim.getSerialnumber(), taskCleanInterim.getYmd(), taskCleanInterim.getAnalyType())) {
                        taskCleanInterim.setStatus(DeleteTaskConstants.CLEAN_STATUS_RUNNING);
                        taskCleanInterim.setUpdateTime(now);
                        taskCleanInterimService.updateById(taskCleanInterim);
                    }
                });
            }
        }
    }

    /***
     * @description: 根据空间删除实时任务数据
     * @return: java.time.LocalDateTime
     */
    private LocalDateTime deleteOnlineBySpace() {
        if (!nacosConfig.getCleanOnlineSwitch()) {
            return null;
        }
        log.info("delete online task by space");
        List<TbAnalysisTask> taskList = tbAnalysisTaskService.selectList(getOnlineWarpper());
        if (!taskList.isEmpty()) {
            String serialnumbers = DeleteTaskUtil.getSerialnumbers(taskList);
            Map<String, Object> paramMap = DeleteTaskUtil.getEarliestParamMap(serialnumbers);
            String dataJson = deleteTaskService.getResultOrderByCreateTime(paramMap);
            JSONArray dataArray = DeleteTaskUtil.getDeleteList(dataJson);
            if (dataArray != null) {
                String createTime = DeleteTaskUtil.getCreateTime(dataArray.getJSONObject(0));
                String yyyyMMdd = DeleteTaskUtil.getDayByCreateTime(createTime);
                log.info("delete online data by space time = " + yyyyMMdd);
                LocalDateTime localDateTime = DeleteTaskUtil.getEndTime(yyyyMMdd);
                if (localDateTime == null) {
                    log.error("can not get date with createTime = " + createTime);
                } else if (isBeforeToday(localDateTime)) {
                    List<String> serialnumberList = DeleteTaskUtil.getSerialnumberList(dataArray, yyyyMMdd);
                    List<TbAnalysisTask> deleteList = tbAnalysisTaskService.selectList(getOnlineWarpper().in("id", serialnumberList));
                    List<TaskCleanYmdData> insertList = taskCleanYmdDataService.getTaskCleanYmdDataList(deleteList, yyyyMMdd);
                    taskCleanYmdDataService.insertBatch(insertList);
                    return localDateTime;
                }
            }
        }
        return null;
    }

    /***
     * @description: 判断是否在当天之前
     * @param localDateTime 当天
     * @return: boolean
     */
    private static boolean isBeforeToday(LocalDateTime localDateTime) {
        return localDateTime.isBefore(DateUtil.getTimeDateToday(0, 0, 0));
    }

    /***
     * @description: 根据时间删除实时任务数据
     * @return: java.time.LocalDateTime
     */
    private void deleteOnlineByTime() {
        if (nacosConfig.getCleanOnlineSwitch()) {
            int cleanOffLineTaskDate = nacosConfig.getCleanOnlineSaveDay();
            if (cleanOffLineTaskDate < 0) {
                return;
            }
            if (cleanOffLineTaskDate == 0) {
                cleanOffLineTaskDate = 90;
            }
            LocalDateTime localDateTime = LocalDateTime.now().plusDays(0L - cleanOffLineTaskDate);
            String yyyyMMdd = localDateTime.toLocalDate().format(DateUtil.YYYYMMDD);
            LocalDateTime endTime = DeleteTaskUtil.getEndTime(localDateTime);
            List<TbAnalysisTask> list = tbAnalysisTaskService.selectList(getOnlineWarpper().
                    le("create_time", new Timestamp(DateUtil.getTimestampOfDateTime(endTime))));
            if (!list.isEmpty()) {
                log.info("delete online task by date = " + yyyyMMdd);
                List<TaskCleanYmdData> insertList = taskCleanYmdDataService.getTaskCleanYmdDataList(list, yyyyMMdd);
                taskCleanYmdDataService.insertBatch(insertList);
            }
        }
    }

    /***
     * @description: 获取查询实时任务的基础查询调教对象
     * @return: com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.keensense.task.entity.TbAnalysisTask>
     */
    private QueryWrapper<TbAnalysisTask> getOnlineWarpper() {
        return new QueryWrapper<TbAnalysisTask>()
                .eq("task_type", TaskConstants.TASK_TYPE_ONLINE)
                .ne("status", TaskConstants.TASK_DELETE_TURE);
    }

    /***
     * @description: 根据空间删除离线任务数据
     * @param localDateTime 删除截止时间
     * @return: void
     */
    private void deleteOfficeBySpace(LocalDateTime localDateTime) {
        if (nacosConfig.getCleanOfflineSwitch()) {
            log.info("delete office task by space");
            if (localDateTime == null) {
                TbAnalysisTask tbAnalysisTask = tbAnalysisTaskService.selectEarliestOfficeTask();
                if (tbAnalysisTask != null) {
                    localDateTime = DateUtil.getDateTimeOfTimestamp(tbAnalysisTask.getCreateTime().getTime());
                } else {
                    return;
                }
            }
            log.info("delete office task by space time = " + DateUtil.formatDate(DateUtil.getTimestampOfDateTime(localDateTime)));
            if (isBeforeToday(localDateTime)) {
                localDateTime = DeleteTaskUtil.getEndTime(localDateTime);
                deleteOfficeByTime(localDateTime);
            }
        }
    }

    /***
     * @description: 根据时间删除离线任务数据
     * @param localDateTime 删除截止时间
     * @return: void
     */
    private void deleteOfficeByTime(LocalDateTime localDateTime) {
        Timestamp time = new Timestamp(DateUtil.getTimestampOfDateTime(localDateTime));
        List<TbAnalysisTask> tasks = tbAnalysisTaskService.selectOfficeForDelete(time);
        if (!tasks.isEmpty()) {
            for (TbAnalysisTask tbAnalysisTask : tasks) {
                try {
                    tbAnalysisTaskService.deleteTask(tbAnalysisTask, DeleteTaskConstants.CLEAN_DATA_SOURCE_AUTO);
                    tbAnalysisTaskService.insertTaskCleanLog(tbAnalysisTask,DeleteTaskConstants.CLEAN_DATA_SOURCE_AUTO);
                } catch (VideoException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    /***
     * @description: 老版本mysql模式检测磁盘容量
     * @return: boolean
     */
    private boolean getOldDataSpace() {
        File fileRoot = new File(ShellUtil.PICTURE_ROOT);
        if (!fileRoot.exists()) {
            log.error(ShellUtil.PICTURE_ROOT + " is not exists!");
            return false;
        }
        long idleRate = fileRoot.getUsableSpace() * 100 / fileRoot.getTotalSpace();
        log.info("空闲率 : " + idleRate + "%");
        int lessSpaceG = nacosConfig.getCleanDiskValue();
        if (0 >= lessSpaceG || lessSpaceG >= 100){
            lessSpaceG = 10;
        }
        return idleRate < lessSpaceG;
    }

    /***
     * @description: 获取结束清理时间
     * @return: long
     */
    private static long getEndTime(){
        return DateUtil.getTimestampOfDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(6, 0, 0)));
    }

    /***
     * @description: 获取开始清理时间
     * @return: long
     */
    private static long getStartTime(){
        return DateUtil.getTimestampToday(22, 0, 0);
    }

    private static boolean isExecuteTime(){
        long now = System.currentTimeMillis();
        return (now >= startTime && now <= endTime);
    }
}
