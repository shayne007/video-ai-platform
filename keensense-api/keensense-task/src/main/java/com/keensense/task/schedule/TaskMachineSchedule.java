package com.keensense.task.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.task.config.NacosConfig;
import com.keensense.task.constants.BitCommonConst;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.VsdBitMachine;
import com.keensense.task.entity.VsdSlave;
import com.keensense.task.entity.VsdTask;
import com.keensense.task.entity.VsdTaskBit;
import com.keensense.task.entity.zkTaskEntity.VsdSlaveTask;
import com.keensense.task.listener.StartListener;
import com.keensense.task.service.IBitTaskService;
import com.keensense.task.service.IDeleteTaskService;
import com.keensense.task.service.IVsdBitMachineService;
import com.keensense.task.service.IVsdSlaveService;
import com.keensense.task.service.IVsdTaskBitService;
import com.keensense.task.service.IVsdTaskService;
import com.keensense.task.util.DateUtil;
import com.keensense.task.util.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: bit盒子任务
 * @Author: wujw
 * @CreateDate: 2019/5/23 15:46
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
@Component
public class TaskMachineSchedule {

    private long cleanTimeStamp = 0;

    @Autowired
    private IBitTaskService bitTaskService;
    @Autowired
    private IVsdTaskBitService vsdTaskBitService;
    @Autowired
    private IVsdTaskService vsdTaskService;
    @Autowired
    private IVsdBitMachineService vsdBitMachineService;
    @Autowired
    private IVsdSlaveService vsdSlaveService;
    @Autowired
    private IDeleteTaskService deleteTaskService;
    @Autowired
    private NacosConfig nacosConfig;

    @Scheduled(cron = "5/15 * * * * ?")
    public void executeTask() {
        if (StartListener.zkDistributeLock.hasCurrentLock() && nacosConfig.getBitSwitch()) {
            try {
                doProcessTask();         //处理正在执行的任务
                doWaitTask();           //处理正准备执行的新任务
                //清理盒子异常任务，重置盒子状态
                if (System.currentTimeMillis() - cleanTimeStamp > nacosConfig.getBitTimeStep()) {
                    cleanTask();
                    cleanTimeStamp = System.currentTimeMillis();
                }
                sleep(2000);
            } catch (InterruptedException e) {
                log.error("sleep error!");
                Thread.currentThread().interrupt();
            }

        }
    }

    private void doProcessTask() throws InterruptedException {
        Map<String, VsdTask> taskMap = getTaskByStatus(TaskConstants.TASK_STATUS_RUNNING);

        Map<Long, VsdBitMachine> bitMap = getProcessMachine();

        List<VsdTaskBit> taskBitList = getTaskBit(VsdTaskBit.STATUS_PROCESS);
        VsdTask vsdTask;
        for (VsdTaskBit vsdTaskBit : taskBitList) {
            if (taskMap.get(vsdTaskBit.getSerialnumber()) != null) {
                vsdTask = taskMap.get(vsdTaskBit.getSerialnumber());
                //判断是否是离线文件
                boolean isRealTimeFile = BitCommonConst.isRealTimeTask(vsdTask.getParam());
                if (vsdTask.getIsValid() == TaskConstants.TASK_ISVALID_OFF) {
                    stopSuccessTask(vsdTask, bitMap, vsdTaskBit);
                    taskMap.remove(vsdTaskBit.getSerialnumber());
                } else if(bitMap.get(vsdTaskBit.getBitMachineId()) != null){
                    updateProgress(vsdTask, bitMap.get(vsdTaskBit.getBitMachineId()), vsdTaskBit, !isRealTimeFile);
                    taskMap.remove(vsdTaskBit.getSerialnumber());
                }
            } else {
                stopTask(vsdTaskBit, bitMap.get(vsdTaskBit.getBitMachineId()));
            }
            sleep(100);
        }
        //多余数据进行重置
        if (taskMap.size() > 0) {
            restProcessData(taskMap);
        }
    }

    /***
     * @description: 停止任务
     * @param vsdTaskBit vsdTaskBit对象
     * @param vsdBitMachine vsdBitMachine对象
     * @return: void
     */
    private void stopTask(VsdTaskBit vsdTaskBit, VsdBitMachine vsdBitMachine){
        log.info("stop task serialnumber = " + vsdTaskBit.getSerialnumber());
        if (vsdBitMachine != null) {
            if (bitTaskService.stopTask(vsdBitMachine.getUrl(), vsdTaskBit.getSerialnumber()) == BitCommonConst.SUCCESS) {
                vsdBitMachineService.stopTask(vsdBitMachine, vsdTaskBit);
            }
        } else {
            vsdBitMachineService.stopTask(null, vsdTaskBit);
        }
    }

    /***
     * @description: 重置丢失任务的状态
     * @param taskMap 状态对象
     * @return: void
     */
    private void restProcessData(Map<String, VsdTask> taskMap) {
        List<VsdTask> taskList = new ArrayList<>(taskMap.values());
        Long[] idList = new Long[taskList.size()];
        for (int i = 0; i < taskList.size(); i++) {
            idList[i] = taskList.get(i).getId();
        }
        vsdTaskService.updateStatusByIds(idList, TaskConstants.TASK_STATUS_WAIT);
    }

    /***
     * @description: 更新任务进度
     * @param vsdTask  任务对象
     * @param vsdBitMachine 盒子对象
     * @param vsdTaskBit  任务与盒子关联关系对象
     * @param isStream   是否是实时流
     * @return: void
     */
    private void updateProgress(VsdTask vsdTask, VsdBitMachine vsdBitMachine, VsdTaskBit vsdTaskBit, boolean isStream) {
        int result = bitTaskService.queryTask(vsdBitMachine.getUrl(), vsdTask.getSerialnumber(), isStream);
        if (result == BitCommonConst.COMPLETE) {
            if (bitTaskService.stopTask(vsdBitMachine.getUrl(), vsdTaskBit.getSerialnumber()) == BitCommonConst.SUCCESS) {
                doTaskSuccess(vsdTask, vsdBitMachine, vsdTaskBit, 2, isStream);
            }
        } else if (result == BitCommonConst.FAIL) {
            //停止任务
            if(bitTaskService.stopTask(vsdBitMachine.getUrl(), vsdTaskBit.getSerialnumber())  == BitCommonConst.SUCCESS){
                //更新状态
                doTaskFail(vsdTask, vsdBitMachine, vsdTaskBit, isStream);
            }
        } else if (result == BitCommonConst.NOT_FOUND) {
            //未查询到指定的查询数据，重置数据
            doTaskReset(vsdTask, vsdTaskBit);
        } else if (result == BitCommonConst.ERROR && doErrorMachine(vsdBitMachine)) {
            vsdTaskService.updateStatusById(vsdTask.getId(), TaskConstants.TASK_STATUS_WAIT);
        }
    }

    /***
     * @description: 停止正常的任务
     * @param vsdTask 任务对象
     * @param bitMap    盒子对象Map集合
     * @param vsdTaskBit 关联对象
     * @return: void
     */
    private void stopSuccessTask(VsdTask vsdTask, Map<Long, VsdBitMachine> bitMap, VsdTaskBit vsdTaskBit) {
        if (bitMap.get(vsdTaskBit.getBitMachineId()) != null) {
            VsdBitMachine vsdBitMachine = bitMap.get(vsdTaskBit.getBitMachineId());
            int result = bitTaskService.stopTask(vsdBitMachine.getUrl(), vsdTask.getSerialnumber());
            if (result == BitCommonConst.SUCCESS) {
                doTaskSuccess(vsdTask, vsdBitMachine, vsdTaskBit, TaskConstants.TASK_STATUS_SUCCESS, true);
            } else if (result == BitCommonConst.ERROR) {
                if (doErrorMachine(vsdBitMachine)) {
                    vsdTask.setStatus(TaskConstants.TASK_STATUS_SUCCESS);
                    vsdTaskService.updateVsdTask(vsdTask);
                }
            } else {
                log.error("an unrecognized result from stopTask method with vsdBitMachine id is "
                        + vsdBitMachine.getId() + "and vsdTask id is " + vsdTask.getId());
            }
        } else {      //找不到对应的盒子数据，默认为完成
            log.error("vsdTask id = " + vsdTask.getId() + " can not find vsdBitMachine data");
            vsdTaskService.updateStatusById(vsdTask.getId(), TaskConstants.TASK_STATUS_SUCCESS);
        }
    }

    private void doTaskSuccess(VsdTask vsdTask, VsdBitMachine vsdBitMachine, VsdTaskBit vsdTaskBit, int vsdTaskStatus, boolean isStream) {
        int userRoad = vsdBitMachine.getUserRoad() - 1;
        vsdTask.setStatus(vsdTaskStatus);
        vsdTask.setEndtime(DateUtil.now());
        if (!isStream) {
            vsdTask.setProgress(TaskConstants.PROGRESS_MAX_VALUE);
        }
        vsdBitMachine.setUserRoad(userRoad);
        vsdBitMachine.setExecuteStatus(BitCommonConst.EXECUTE_STATUS_FALSE);
        vsdBitMachine.setRetryCount(0);
        vsdBitMachineService.taskComplete(vsdBitMachine, vsdTaskBit, vsdTask);
    }

    private void doTaskFail(VsdTask vsdTask, VsdBitMachine vsdBitMachine, VsdTaskBit vsdTaskBit, boolean isSteam) {
        vsdTask.setRetryCount(vsdTask.getRetryCount() + 1);
        //如果是离线文件,判断是否已经是最大错误次数
        if (!isSteam && vsdTask.getRetryCount() >= BitCommonConst.MAX_FAIL_COUNT) {
            vsdTask.setStatus(TaskConstants.TASK_STATUS_FAILED);
        }else{
            vsdTask.setStatus(TaskConstants.TASK_STATUS_WAIT);
        }
        vsdTask.setEndtime(DateUtil.now());
        vsdTask.setProgress(0);
        vsdBitMachine.setUserRoad(vsdBitMachine.getUserRoad() - 1);
        vsdBitMachine.setExecuteStatus(BitCommonConst.EXECUTE_STATUS_FALSE);
        vsdBitMachine.setRetryCount(0);
        vsdBitMachineService.taskComplete(vsdBitMachine, vsdTaskBit, vsdTask);
    }

    private void doTaskReset(VsdTask vsdTask, VsdTaskBit vsdTaskBit) {
        vsdTask.setStatus(TaskConstants.TASK_STATUS_WAIT);
        vsdTask.setProgress(0);
        vsdBitMachineService.taskReset(vsdTask, vsdTaskBit);
    }

    private void doWaitTask() throws InterruptedException {
        Map<String, VsdTask> taskMap = getTaskByStatus(TaskConstants.TASK_STATUS_WAIT);
        if (taskMap.isEmpty()) {
            return;
        }

        Map<Long, VsdBitMachine> bitMap = getMachineByStatus(0);
        if (!bitMap.isEmpty()) {
            doLegacy(taskMap, bitMap);
            List<VsdTask> taskList = new ArrayList<>(taskMap.values());
            List<VsdBitMachine> machineList = new ArrayList<>(bitMap.values());
            long now = System.currentTimeMillis();
            for (VsdTask vsdTask : taskList) {
                if(vsdTask.getEndtime() != null && now - vsdTask.getEndtime().getTime() < 60 * 1000){
                    continue;
                }
                VsdBitMachine vsdBitMachine = getSmallestMachine(machineList);
                VsdTaskBit vsdTaskBit = new VsdTaskBit(vsdTask.getSerialnumber(), vsdBitMachine.getId());
                if (vsdTaskBitService.insert(vsdTaskBit)) {
                    if (startTask(vsdTask, vsdBitMachine, vsdTaskBit)) {
                        machineList.remove(vsdBitMachine);
                    }
                    sleep(100);
                } else {
                    log.error("insert vsd_task_bit error! vsdTask.id=" + vsdTask.getId() + ",vsdBitMachine.id=" + vsdBitMachine.getId());
                }
            }
        }
    }

    /***
     * @description: 获取使用率最低的盒子
     * @param machineList 盒子列表
     * @return: com.keensense.task.entity.VsdBitMachine
     */
    private VsdBitMachine getSmallestMachine(List<VsdBitMachine> machineList){
        int min = 100;
        VsdBitMachine vsdBitMachine = null;
        for (VsdBitMachine machine : machineList){
            int rate = machine.getUserRoad() * 100 / machine.getNumberRoad();
            if(rate < min){
                min = rate;
                vsdBitMachine = machine;
            }
        }
        return vsdBitMachine;
    }

    /***
     * @description: 处理遗留问题
     * @param taskMap  任务MAP
     * @param bitMachineMap 盒子MAP
     * @return: void
     */
    private void doLegacy(Map<String, VsdTask> taskMap, Map<Long, VsdBitMachine> bitMachineMap) throws InterruptedException {
        VsdTask vsdTask;
        VsdBitMachine vsdBitMachine;
        List<VsdTaskBit> taskBitList = getTaskBit(VsdTaskBit.STATUS_WAIT);
        for (VsdTaskBit vsdTaskBit : taskBitList) {
            //任务数据不为空
            if (taskMap.get(vsdTaskBit.getSerialnumber()) != null) {
                vsdTask = taskMap.get(vsdTaskBit.getSerialnumber());
                //盒子数据不为空时
                if (bitMachineMap.get(vsdTaskBit.getBitMachineId()) != null) {
                    vsdBitMachine = bitMachineMap.get(vsdTaskBit.getBitMachineId());
                    if (startTask(vsdTask, vsdBitMachine, vsdTaskBit)) {
                        bitMachineMap.remove(vsdBitMachine.getId());
                    }
                    sleep(100);
                } else {
                    //任务中有数据，而盒子数据没有，删除临时数据更换盒子
                    vsdTaskBitService.deleteById(vsdTaskBit.getId());
                }
                taskMap.remove(vsdTaskBit.getSerialnumber());
            } else {
                //任务中没有取到数据，则原任务出现问题，删除临时数据
                vsdTaskBitService.deleteById(vsdTaskBit.getId());
            }
        }
    }

    /***
     * @description: 启动任务
     * @param vsdTask   任务对象
     * @param vsdBitMachine 盒子对象
     * @param vsdTaskBit 任务盒子关联对象
     * @return: boolean
     */
    private boolean startTask(VsdTask vsdTask, VsdBitMachine vsdBitMachine, VsdTaskBit vsdTaskBit) {
        if(BitCommonConst.isRealTimeTask(vsdTask.getParam())){
            deleteTaskService.deleteEs(vsdTask.getSerialnumber(), null, vsdTask.getType());
        }
        int result = bitTaskService.startTask(vsdBitMachine.getUrl(), vsdTask);
        if (result == BitCommonConst.SUCCESS) {
            startTaskSuccess(vsdTask, vsdBitMachine, vsdTaskBit);
            //满负载则移除该设备
            return vsdBitMachine.getExecuteStatus() == BitCommonConst.EXECUTE_STATUS_TRUE;
        } else if (result == BitCommonConst.FAIL) {
            startTaskFail(vsdTask, vsdBitMachine, vsdTaskBit);
        } else if (result == BitCommonConst.ERROR) {
            doErrorMachine(vsdBitMachine);
        } else {
            log.error("an unrecognized result from startTask method with vsdBitMachine id is "
                    + vsdBitMachine.getId() + "and vsdTask id is " + vsdTask.getId());
        }
        return false;
    }

    private void startTaskSuccess(VsdTask vsdTask, VsdBitMachine vsdBitMachine, VsdTaskBit vsdTaskBit) {
        vsdBitMachine.setUserRoad(vsdBitMachine.getUserRoad() + 1);
        if (vsdBitMachine.getUserRoad().equals(vsdBitMachine.getNumberRoad())) {
            vsdBitMachine.setExecuteStatus(BitCommonConst.EXECUTE_STATUS_TRUE);
        }
        vsdBitMachine.setRetryCount(0);

        vsdTask.setStatus(TaskConstants.TASK_SHOW_STATUS_RUNNING);
        vsdTask.setProgress(0);
        vsdTaskBit.setStatus(VsdTaskBit.STATUS_PROCESS);
        vsdBitMachineService.startSuccess(vsdBitMachine, vsdTaskBit, vsdTask);
    }

    /***
     * @description: 处理连接异常的机器
     * @param vsdBitMachine 盒子对象
     * @return:
     */
    private boolean doErrorMachine(VsdBitMachine vsdBitMachine) {
        log.error("An error with vsdBitMachine id = " + vsdBitMachine.getId());
        //判断是否已经是最大错误次数
        if (vsdBitMachine.getRetryCount() >= BitCommonConst.MAX_FAIL_COUNT) {
            QueryWrapper<VsdTaskBit> queryWrapper = new QueryWrapper<VsdTaskBit>()
                    .eq("bit_machine_id", vsdBitMachine.getId());
            List<VsdTaskBit> list = vsdTaskBitService.selectByWrapper(queryWrapper);
            for (VsdTaskBit vsdTaskBit : list) {
                bitTaskService.stopTask(vsdBitMachine.getUrl(), vsdTaskBit.getSerialnumber());
            }
            vsdBitMachine.setExecuteStatus(BitCommonConst.EXECUTE_STATUS_FALSE);
            vsdBitMachine.setBitStatus(BitCommonConst.MACHINE_STATUS_ERROR);
            vsdBitMachine.setUserRoad(0);
            vsdBitMachineService.doErrorMachine(vsdBitMachine, queryWrapper);
            return true;
        } else {
            //如果最后失败时间为空或者大于最小间隔时间，则失败次数加1
            if (vsdBitMachine.getLastFailTime() == null
                    || System.currentTimeMillis() - vsdBitMachine.getLastFailTime().getTime() > BitCommonConst.MIN_FAIL_TIME) {
                vsdBitMachine.setRetryCount(vsdBitMachine.getRetryCount() + 1);
                vsdBitMachine.setLastFailTime(DateUtil.now());
                vsdBitMachineService.updateVsdBitMachine(vsdBitMachine);
            }
        }
        return false;
    }

    /**
     * @param vsdTask       vsdTask对象
     * @param vsdBitMachine 盒子对象
     * @description: 处理解析异常的Task任务
     * @return: void
     */
    private void startTaskFail(VsdTask vsdTask, VsdBitMachine vsdBitMachine, VsdTaskBit vsdTaskBit) {
        log.error("vsdTask id = " + vsdTask.getSerialnumber() + " request failed");
        vsdTask.setRetryCount(vsdTask.getRetryCount() + 1);
        //如果是离线文件,判断是否已经是最大错误次数
        if (BitCommonConst.isRealTimeTask(vsdTask.getParam()) && vsdTask.getRetryCount() >= BitCommonConst.MAX_FAIL_COUNT) {
            vsdTask.setStatus(TaskConstants.TASK_STATUS_FAILED);
        }
        if (bitTaskService.stopTask(vsdBitMachine.getUrl(), vsdTask.getSerialnumber()) == BitCommonConst.SUCCESS) {
            vsdBitMachineService.taskReset(vsdTask, vsdTaskBit);
        }
    }

    /***
     * @description: 根据状态获取任务
     * @param status 状态
     * @return: java.util.Map<java.lang.String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               com.keensense.task.entity.VsdTask>
     */
    private Map<String, VsdTask> getTaskByStatus(int status) {
        List<VsdTask> list = vsdTaskService.selectByWrapper(new QueryWrapper<VsdTask>()
                .eq("type", TaskConstants.ANALY_TYPE_OBJEXT)
                .eq("isvalid", TaskConstants.TASK_ISVALID_ON).eq(TaskConstants.STATUS, status));
        return list.stream().collect(Collectors.toMap(VsdTask::getSerialnumber, vsdTask -> vsdTask));
    }
    
    /***
     * @description: 获取有空闲路数的盒子
     * @return: java.util.Map<java.lang.Long,com.keensense.task.entity.VsdBitMachine>
     */
    private Map<Long, VsdBitMachine> getProcessMachine() {
        List<VsdBitMachine> list = vsdBitMachineService.selectByWrapper(new QueryWrapper<VsdBitMachine>()
                .eq("bit_status", BitCommonConst.MACHINE_STATUS_NORMAL).gt("user_road", 0));
        return list.stream().collect(Collectors.toMap(VsdBitMachine::getId, vsdBitMachine -> vsdBitMachine));
    }

    /***
     * @description: 根据状态查询盒子
     * @param status 状态码
     * @return: java.util.Map<java.lang.Long,com.keensense.task.entity.VsdBitMachine>
     */
    private Map<Long, VsdBitMachine> getMachineByStatus(Integer status) {
        List<VsdBitMachine> list =  vsdBitMachineService.selectByWrapper(new QueryWrapper<VsdBitMachine>()
                .eq("bit_status", BitCommonConst.MACHINE_STATUS_NORMAL)
                .eq("execute_status", status));
        Map<Long, VsdBitMachine> result = new HashMap<>(list.size());
        for (VsdBitMachine vsdBitMachine : list) {
            result.put(vsdBitMachine.getId(), vsdBitMachine);
        }
        return result;
    }

    /***
     * @description: 根据状态查询关联关系
     * @param status 状态
     * @return: java.util.List<com.keensense.task.entity.VsdTaskBit>
     */
    private List<VsdTaskBit> getTaskBit(int status) {
        return vsdTaskBitService.selectByWrapper(new QueryWrapper<VsdTaskBit>().eq(TaskConstants.STATUS, status));
    }

    /**
     * 清理异常任务，修复盒子状态
     */
    private void cleanTask() {
        //获取所有盒子
        List<VsdBitMachine> list = vsdBitMachineService.selectByWrapper(new QueryWrapper<>());
        for (VsdBitMachine vsdBitMachine : list) {
            JSONObject result = bitTaskService.queryTaskByMachine(vsdBitMachine.getUrl());
            //只在查询成功后才进行后续处理
            if (BitCommonConst.SUCCESS == result.getInteger("status")) {
                //盒子当前使用路数
                int useRoute = result.getInteger("useRoute");
                //盒子总路数
                int allRoute = result.getInteger("allRoute");
                JSONArray serialNumberList = result.getJSONArray("serialnumberList");
                Map<String, VsdTaskBit> vsdTaskBitMap = getVsdTaskBitMap(vsdBitMachine.getId());
                //如果盒子中的任务不包含在MAP中，则表示该任务为异常数据，发起停止请求
                for (int i = 0; i < serialNumberList.size(); i++) {
                    if (!vsdTaskBitMap.containsKey(serialNumberList.getString(i))) {
                        bitTaskService.stopTask(vsdBitMachine.getUrl(), serialNumberList.getString(i));
                        useRoute--;
                    }
                }
                resetBitMachine(vsdBitMachine, allRoute, useRoute);
                updateVsdSlave(vsdBitMachine, allRoute, vsdTaskBitMap, true);
            }else{
                updateVsdSlave(vsdBitMachine, 0, null, false);
            }
        }
    }

    /***
     * @description: 获取vsdTaskBitMap
     * @param id machine的ID
     * @return: java.util.Map<java.lang.String,com.keensense.task.entity.VsdTaskBit>
     */
    private  Map<String, VsdTaskBit> getVsdTaskBitMap(Long id){
        List<VsdTaskBit> vsdTaskBits = vsdTaskBitService.selectByWrapper(new QueryWrapper<VsdTaskBit>()
                .eq("bit_machine_id", id)
                .eq(TaskConstants.STATUS,  VsdTaskBit.STATUS_PROCESS));
        Map<String, VsdTaskBit> vsdTaskBitMap = new HashMap<>(vsdTaskBits.size());
        for (VsdTaskBit vsdTaskBit : vsdTaskBits) {
            vsdTaskBitMap.put(vsdTaskBit.getSerialnumber(), vsdTaskBit);
        }
        return vsdTaskBitMap;
    }

    /***
     * @description: 重置盒子状态
     * @param vsdBitMachine 盒子对象
     * @param allRoute      所有路数
     * @param useRoute      使用路数
     * @return: void
     */
    private void resetBitMachine(VsdBitMachine vsdBitMachine, int allRoute, int useRoute){
        //如果路数不相等或者盒子状态为不可用，修正盒子参数
        if (allRoute != vsdBitMachine.getNumberRoad() || useRoute != vsdBitMachine.getUserRoad()
                || vsdBitMachine.getBitStatus() == 0) {
            vsdBitMachine.setUserRoad(useRoute);
            vsdBitMachine.setNumberRoad(allRoute);
            vsdBitMachine.setRetryCount(0);
            vsdBitMachine.setBitStatus(BitCommonConst.MACHINE_STATUS_NORMAL);
            if (allRoute == useRoute) {
                vsdBitMachine.setExecuteStatus(BitCommonConst.EXECUTE_STATUS_TRUE);
            } else {
                vsdBitMachine.setExecuteStatus(BitCommonConst.EXECUTE_STATUS_FALSE);
            }
            vsdBitMachineService.updateVsdBitMachine(vsdBitMachine);
        }
    }

    private void updateVsdSlave(VsdBitMachine vsdBitMachine, int allRoute, Map<String, VsdTaskBit> vsdTaskBitMap, boolean isSuccess){
        String ip = IpUtils.getIpByUrl(vsdBitMachine.getUrl());
        if(!StringUtils.isEmpty(ip)){
            VsdSlave vsdSlave = vsdSlaveService.selectByWrapper(new QueryWrapper<VsdSlave>().eq("slave_ip", ip));
            if(vsdSlave == null){
                if(isSuccess){
                    vsdSlave = new VsdSlave();
                    getVsdSlaveByNode(vsdSlave, ip, allRoute, vsdTaskBitMap);
                    vsdSlaveService.insert(vsdSlave);
                }
            }else{
                if(isSuccess){
                    getVsdSlaveByNode(vsdSlave, ip, allRoute, vsdTaskBitMap);
                }else{
                    vsdSlave.setValid(TaskConstants.SLAVE_VALID_OFF);
                    vsdSlave.setLastupdateTime(DateUtil.now());
                    vsdSlaveService.updateVsdSlave(vsdSlave);
                }
                vsdSlaveService.updateVsdSlave(vsdSlave);
            }
        }
    }

    /***
     * @description: 根据Node节点信息获取vsdSlave对象
     * @param vsdSlave 节点对象
     * @param ip ip地址
     * @param allRoute 总路数
     * @param vsdTaskBitMap 任务列表
     * @return: void
     */
    private void getVsdSlaveByNode(VsdSlave vsdSlave, String ip, int allRoute, Map<String, VsdTaskBit> vsdTaskBitMap) {
        vsdSlave.setValid(TaskConstants.SLAVE_VALID_ON);
        vsdSlave.setSlaveIp(ip);
        vsdSlave.setSlaveId(MasterSchedule.getMasterId());
        vsdSlave.setReserve("");
        vsdSlave.setObjextCapability(allRoute);
        vsdSlave.setVlprCapability(0);
        vsdSlave.setFaceCapability(0);
        vsdSlave.setLastupdateTime(DateUtil.now());
        List<VsdSlaveTask> list = new ArrayList<>();
        for (Map.Entry<String, VsdTaskBit> slaveStatus : vsdTaskBitMap.entrySet()) {
            list.add(new VsdSlaveTask(slaveStatus.getValue().getSerialnumber()));
        }
        vsdSlave.setPayload(JSON.toJSONString(getPayload(list)));
    }

    /***
     * @description: 获取vsd_slavestatus中payload的值
     * @param list 任务列表
     * @return: java.util.Map
     */
    private Map<String, Object> getPayload(List<VsdSlaveTask> list) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("tasks", list);
        map.put("cpu", 0);
        map.put("ram", Arrays.asList(0,0));
        map.put("diskfreespace", Arrays.asList(0,0,0));
        return map;
    }

    private void sleep(long time) throws InterruptedException{
        Thread.sleep(time);
    }
}
