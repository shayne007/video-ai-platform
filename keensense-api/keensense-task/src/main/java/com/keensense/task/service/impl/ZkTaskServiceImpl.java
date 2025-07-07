package com.keensense.task.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.task.config.NacosConfig;
import com.keensense.task.constants.BitCommonConst;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.constants.ZkConstants;
import com.keensense.task.entity.VsdSlave;
import com.keensense.task.entity.VsdTask;
import com.keensense.task.entity.zkTaskEntity.ExectaskEndProgress;
import com.keensense.task.entity.zkTaskEntity.ExectaskNode;
import com.keensense.task.entity.zkTaskEntity.NodeInfo;
import com.keensense.task.entity.zkTaskEntity.SlaveStatus;
import com.keensense.task.entity.zkTaskEntity.VsdSlaveTask;
import com.keensense.task.mapper.VsdTaskMapper;
import com.keensense.task.service.IDeleteTaskService;
import com.keensense.task.service.IVsdSlaveService;
import com.keensense.task.service.IZkTaskService;
import com.keensense.task.util.DateUtil;
import com.keensense.task.util.zookeeper.ZkClient;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: vsdTask service 实现类
 * @Author: wujw
 * @CreateDate: 2019/5/17 11:23
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Service
@Slf4j
public class ZkTaskServiceImpl implements IZkTaskService {

    @Autowired
    private VsdTaskMapper vsdTaskMapper;

    @Autowired
    private NacosConfig nacosConfig;
    @Autowired
    private IVsdSlaveService vsdSlaveService;
    @Autowired
    private IDeleteTaskService deleteTaskService;

    private static Long resetTime = 0L;

    private static Map<String, VsdSlave> vsdSlaveMap = new HashMap<>();

    /**
     * 内存使用率阈值
     */
    private static final Integer RAM_LIMIT = 85;

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
    public void addZkTask() {
        // 按任务优先级升序获取
        String taskTypes = nacosConfig.getTaskTypes();
        if (taskTypes == null) {
            taskTypes = TaskConstants.TASK_DEFAULT_TASK_TYPE;
        }
        log.info(">>>>>> searching taskType in {} for vsd_task....", taskTypes);
        IPage<VsdTask> vsdTaskPage = vsdTaskMapper.selectPage(new Page<>(1, nacosConfig.getCapability()),
                getQueryWrapper(TaskConstants.TASK_STATUS_WAIT).eq("isvalid", TaskConstants.TASK_ISVALID_ON)
                        .notIn("type", TaskConstants.ANALY_TYPE_OTHER, TaskConstants.ANALY_TYPE_DESITTY)
                        .inSql("task_type", taskTypes).orderByAsc("priority").orderByAsc("retrycount"));
        log.info(">>>>>>>>>task list size " + vsdTaskPage.getTotal());
        if (vsdTaskPage.getTotal() > 0) {
            addTask(vsdTaskPage.getRecords());
        }
    }

    /***
     * @description: 添加任务至节点
     * @param vsdTaskList
     *            任务列表
     * @return: void
     */
    private void addTask(List<VsdTask> vsdTaskList) {
        List<NodeInfo> nodeInfos = getNodeInfoList();
        if (nodeInfos.isEmpty()) {
            log.info("can not get node = " + ZkConstants.FILE_NODES + " for start task");
            return;
        }
        for (VsdTask vsdTask : vsdTaskList) {
            // 假如任务已经存在，则不重复下发zk，同时把vsd_task表的状态修改
            String repeatPath = checkRepeatTask(vsdTask.getId().toString());
            if (repeatPath != null) {
                log.info(" >>>>>task:{} already exist in {} ", vsdTask.getId(), repeatPath);
                VsdTask updateTask = new VsdTask();
                updateTask.setId(vsdTask.getId());
                updateTask.setStatus(TaskConstants.TASK_STATUS_RUNNING);
                int rslt = vsdTaskMapper.updateById(updateTask);
                continue;
            }
            // 任务结束时间和当前时间差距在1分钟内，则不重发该任务
            // 通知清理模块清理此任务数据
            Long endTime = Optional.ofNullable(vsdTask.getEndtime()).map(Timestamp::getTime).orElse(0L);
            if (System.currentTimeMillis() - endTime < 60000) {
                continue;
            }
            NodeInfo slaveNode = findMostSuitableNode(nodeInfos);
            // 没有分析资源的时候，需要看执行的任务中是否有较低优先级的任务，
            // 如果有，则假如分析zk中，将当前任务优先分析
            if (slaveNode == null) {
                log.info(">>>>>no suitable node to add task...");
                slaveNode = getNodeByPriorityLower(vsdTask.getPriority(), nodeInfos);
            }
            /*
            因录像流/离线视频都已经支持断点续分析，所以无需删除
            */
            boolean flag = true;
            // JSONObject jsonObject = JSON.parseObject(vsdTask.getParam());
            // if(TaskConstants.TASK_TYPE_OFFLINE == jsonObject.getIntValue("taskType")){
            // flag = deleteTask(vsdTask);
            // }
            if (slaveNode != null && flag) {
                log.info(">>>>>>>>get suitable node " + slaveNode.getIp());
                VsdTask updateTask = new VsdTask();
                updateTask.setId(vsdTask.getId());
                updateTask.setStatus(TaskConstants.TASK_STATUS_RUNNING);
                updateTask.setSlaveip(slaveNode.getIp());
                int rslt = vsdTaskMapper.updateById(updateTask);
                if (rslt > 0) {
                    log.info(">>>>>>>>>>>>status changed because of addTask.... taskId:{},old_status:{},new_status:{}",
                            vsdTask.getId(), 0, 1);
                    String taskJson = initExectaskTask(vsdTask);
                    String path = ZkConstants.FILE_TASK + ZkConstants.FILE_SEPARATOR + slaveNode.getSlaveId()
                            + ZkConstants.FILE_SEPARATOR + vsdTask.getId();
                    if (ZkClient.getZkClient().isExistNode(path)) {
                        log.info(" >>>>> node:{}   already exist", path);
                    } else {
                        ZkClient.getZkClient().createNode(path, taskJson);
                        log.info("addZkTask suit task json:" + taskJson);
                    }
                }
            } else {
                // 获取不到分析节点，可能是分析节点已满，直接中断循环
                break;
            }
        }
    }

    /**
     * 遍历任务节点，如有优先级较低的任务，返回该节点可用 由APP选择优先级较高的任务先分析，优先级较低的任务以错误码-34退出，待资源足够时，由reload机制再加载进去
     *
     * @param priorityThreshold 优先级阈值
     * @param nodeList          节点列表
     * @return
     */
    private NodeInfo getNodeByPriorityLower(Integer priorityThreshold, List<NodeInfo> nodeList) {
        NodeInfo node = null;
        for (NodeInfo nodeInfo : nodeList) {
            String path = ZkConstants.FILE_TASK + ZkConstants.FILE_SEPARATOR + nodeInfo.getSlaveId();
            List<String> tasks = ZkClient.getZkClient().getChildren(path);
            log.info(">>>>> node {} tasks:{} ", path, tasks);
            for (String task : tasks) {
                String taskPath = path + ZkConstants.FILE_SEPARATOR + task;
                if (ZkClient.getZkClient().isExistNode(taskPath)) {
                    JSONObject taskData = JSON.parseObject(ZkClient.getZkClient().getData(taskPath));
                    Integer priorityCurrentNode = taskData.getJSONObject("taskParamJson").getInteger("priority");
                    // 假如当前节点的优先级大于阈值，则返回当前节点
                    if (priorityCurrentNode != null && priorityCurrentNode.intValue() > priorityThreshold.intValue()) {
                        String serialnumber = taskData.getString("serialNumber");
                        log.info(
                                ">>>>find lower priority task in node {},serialnumber:{},priority:{},new task priority:{}",
                                nodeInfo.getIp(), serialnumber, priorityCurrentNode, priorityThreshold);
                        node = nodeInfo;
                    }
                }
                if (node != null) {
                    break;
                }
            }
            if (node != null) {
                break;
            }
        }
        return node;
    }

    /***
     * @description: 重试时先清理非实时流数据 若清理失败，则重试一次；假如重试继续失败，则加入重试列表，待服务好了再清理，直至清理完成
     * @param vsdTask
     *            任务对象
     * @return: boolean
     */
    private boolean deleteTask(VsdTask vsdTask) {
        if (!BitCommonConst.isRealTimeTask(vsdTask.getParam())) {
            return true;
        }
        boolean flag = deleteTaskService.deleteEs(vsdTask.getSerialnumber(), null, vsdTask.getType());
        if (!flag) {
            log.info(">>>>>>offline data first deleted failed,try again 3s later.....");
            try {
                Thread.sleep(3000L);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            flag = deleteTaskService.deleteEs(vsdTask.getSerialnumber(), null, vsdTask.getType());
            // 假如再次删除失败，则记录到重试列表中，不影响后续业务
            // if(!flag){
            // log.info(">>>>>>offline data second deleted failed,add to retry list.....");
            // Timestamp now = new Timestamp(System.currentTimeMillis());
            // TaskCleanRetryLog taskCleanRetryLog = new
            // TaskCleanRetryLog(vsdTask.getSerialnumber(),vsdTask.getUserserialnumber(),
            // vsdTask.getCreatetime(),now,now);
            // taskCleanRetryLogMapper.insert(taskCleanRetryLog);
            // }
        }
        return true;
        // return ZkConstants.isStreamTask(vsdTask.getParam())
        // || deleteTaskService.deleteEs(vsdTask.getSerialnumber(), null, vsdTask.getType());
    }

    /***
     * @description: 获取节点信息使用路数列表
     * @return: java.util.List<com.keensense.task.entity.zkTaskEntity.NodeInfo>
     */
    private List<NodeInfo> getNodeInfoList() {
        List<ExectaskNode> exectaskNodeList = getNodes();
        List<NodeInfo> nodeInfos = new ArrayList<>();
        String taskPath = ZkConstants.FILE_TASK + ZkConstants.FILE_SEPARATOR;
        exectaskNodeList.forEach(node -> {
            if (ZkClient.getZkClient().isExistNode(taskPath + node.getSlaveId())) {
                List<String> tasks = ZkClient.getZkClient().getChildren(taskPath + node.getSlaveId());
                nodeInfos.add(buildNodeInfo(node, tasks));
            }

        });
        return nodeInfos;
    }

    private NodeInfo buildNodeInfo(ExectaskNode node, List<String> task) {
        return new NodeInfo(node.getSlaveId(), node.getIp(), node.getCapability(), task.size(),
                node.getTotalNumberOfFreeSlots(), node.getRam(), node.getNumberOfTasksPerChildProcess().intValue(),
                node.getTotalNumberOfChildProcesses().intValue(), node.getTotalTasks().intValue());
    }

    /**
     * 获取/exectask/nodes/下最合适的node
     *
     * @param nodeList 节点信息
     * @return NodeInfo
     */
    private NodeInfo findMostSuitableNode(List<NodeInfo> nodeList) {
        NodeInfo nodeMostSuitable = null;
        int useRateLowest = 100;
        for (NodeInfo node : nodeList) {
            int using = node.getCapability().getObjext() - node.getTotalNumberOfFreeSlots();
            // 当内存使用率超过阈值，则不向该节点发送任务
            if (isCpuOverload(node)) {
                continue;
            }
            // 哪个节点剩余路数较多，则选取该节点发送任务
            if (node.getTotalNumberOfFreeSlots() != null) {
                int remain = node.getTotalNumberOfFreeSlots().intValue();
                log.info(">>>>>>>>{}分析空闲路数：{}", node.getIp(), remain);
                int runningTasks = node.getTotalTasks(); // zk的任务数
                int taskCapacity = node.getTotalNumberOfChildProcesses() * node.getNumberOfTasksPerChildProcess(); // 显卡支持路数

                if (checkSuitable(runningTasks, taskCapacity, remain)) {
                    // 获取slave节点的使用率
                    int useRateTemp = getUseRateTemp(node);
                    log.info(node.getIp() + "分析使用率：" + useRateTemp);
                    log.info(node.getIp() + "分析总路数：" + node.getCapability().getObjext() + ",分析使用路数:"
                            + using);
                    if (useRateTemp < useRateLowest) {
                        useRateLowest = useRateTemp;
                        nodeMostSuitable = node;
                    }
                } else {
                    log.info(">>>>>>>>>>>>>>node is full, zk total tasks:{},gpu total capacity:{}", runningTasks,
                            taskCapacity);
                }

            }
        }
        String nodes =
                nodeList.stream().map(NodeInfo::getIp).collect(Collectors.joining(","));
        log.info(">>>>current objext nodes:" + nodes);
        // 剩余总路数
        int leftTotal = nodeList.stream().mapToInt(node -> node.getTotalNumberOfFreeSlots()).sum();
        // 最大总路数
        int capacityTotal = nodeList.stream().mapToInt(node -> node.getCapability().getObjext()).sum();
        log.info(">>>>current analysis task total:{}, left :{},capacity:{}.....", capacityTotal - leftTotal, leftTotal, capacityTotal);

        if (nodeMostSuitable != null) {
            nodeMostSuitable.setTaskCount(nodeMostSuitable.getTaskCount() + 1);
        }
        return nodeMostSuitable;
    }

    private boolean isCpuOverload(NodeInfo node) {
        List<Long> ram = node.getRam();
        if (ram == null || ram.size() < 2) {
            return false;
        }
        log.info(">>>>>>" + node.getIp() + "内存使用率:" + ram.get(0).longValue() * 100 / ram.get(1).longValue());

        boolean overload = ram.get(0).longValue() * 100 / ram.get(1).longValue() >= RAM_LIMIT;
        if (overload) {
            log.info(">>>>>>" + node.getIp() + " 内存超限" + RAM_LIMIT + "%,总内存:" + ram.get(1).longValue() + ",已使用内存："
                    + ram.get(0).longValue());
        }
        return overload;
    }

    private int getUseRateTemp(NodeInfo node) {
        return ((node.getCapability().getObjext() - node.getTotalNumberOfFreeSlots()) * 100)
                / node.getCapability().getObjext();
    }

    private static boolean checkSuitable(int runningTasks, int taskCapacity, int remain) {
        // gpu还有剩余路数，并且当前zk的任务数小于显卡支持容量时，才下发任务
        return remain > 0 && runningTasks < taskCapacity;
    }

    /**
     * @description: 初始化/exectask/task/snode/task 数据
     * @return: com.keensense.task.entity.zkTaskEntity.ExectaskNode
     */
    private String initExectaskTask(VsdTask vsdTask) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskId", "" + vsdTask.getId());
        jsonObject.put("serialNumber", vsdTask.getSerialnumber());
        jsonObject.put("taskType", TaskConstants.getAnalyNum(vsdTask.getType()));
        jsonObject.put("status", TaskConstants.TASK_STATUS_WAIT);
        jsonObject.put("taskParamJson", vsdTask.getParam());
        jsonObject.put("priority", vsdTask.getPriority());
        jsonObject.put("createTime",
                vsdTask.getCreatetime() == null ? "" : DateUtil.formatDate(vsdTask.getCreatetime().getTime()));
        jsonObject.put("entryTime",
                vsdTask.getEntrytime() == null ? "" : DateUtil.formatDate(vsdTask.getEntrytime().getTime()));
        jsonObject.put("frameRate", vsdTask.getFramerate() == null ? 25 : vsdTask.getFramerate());
        return jsonObject.toString();
    }

    /**
     * @description: 获取/exectask/progress/ & 更新vsdtask进度
     * @return: com.keensense.task.entity.zkTaskEntity.ExectaskNode
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updExectaskProgress() {
        if (!ZkClient.getZkClient().isExistNode(ZkConstants.FILE_PROGRESS)) {
            log.info("can not get node = " + ZkConstants.FILE_PROGRESS + " for update progress");
            return;
        }
        String taskProgressData;
        List<String> taskList = ZkClient.getZkClient().getChildren(ZkConstants.FILE_PROGRESS);
        for (String taskId : taskList) {
            taskProgressData = ZkClient.getZkClient().getData(ZkConstants.FILE_PROGRESS + ZkConstants.FILE_SEPARATOR + taskId);
            if (taskProgressData != null && StringUtils.isNotBlank(taskProgressData.trim())) {
                try {
                    ExectaskEndProgress exectaskProgress = JSONObject.parseObject(taskProgressData, ExectaskEndProgress.class);
                    if (exectaskProgress != null) {
                        // 若end节点上出现由于分析资源不足而失败的任务，需要把这些任务重新装载。当前流程需要忽略这些数据
                        if (isReloadable(exectaskProgress)) {
                            continue;
                        }
                        List<VsdTask> vsdTasks = vsdTaskMapper.selectList(
                                new QueryWrapper<VsdTask>().eq("serialnumber", exectaskProgress.getSerialNumber()));
                        String param = "";
                        if (vsdTasks != null && vsdTasks.size() > 0) {
                            VsdTask vsdTask = vsdTasks.get(0);
                            param = vsdTask.getParam();
                            // 每次分析会将当前分析的位置传到zk，task将currentPos写入到分析参数中，下次重试时，APP以这个点作为起始位置来支持断点续分析
                            if (exectaskProgress.getCurrentPos() != null
                                    && exectaskProgress.getCurrentPos().longValue() > 0) {
                                JSONObject jsonObject = JSONObject.parseObject(param);
                                jsonObject.put("currentPos", exectaskProgress.getCurrentPos().longValue());
                                param = jsonObject.toString();
                            }
                        }
                        vsdTaskMapper.updateStatusProgressBySer(exectaskProgress.getSerialNumber(),
                                exectaskProgress.getProgress(), param, exectaskProgress.getErrMsgNumber(),
                                exectaskProgress.getErrMsg());
                    } else {
                        log.info(">>>>>>>> error exectaskProgress:{}", taskProgressData);
                    }
                } catch (Exception e) {
                    log.error("procress error data=" + taskProgressData, e);
                }
            }
        }
    }

    /**
     * @description: 获取/exectask/end/ & 更新vsdtask end进度
     * @return: com.keensense.task.entity.zkTaskEntity.ExectaskNode
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updExectaskEnd() {
        if (ZkClient.getZkClient().isExistNode(ZkConstants.FILE_END)) {
            List<String> slaveNodes = ZkClient.getZkClient().getChildren(ZkConstants.FILE_END);
            List<String> taskNodes = ZkClient.getZkClient().getChildren(ZkConstants.FILE_TASK);
            String endRootPath = ZkConstants.FILE_END + ZkConstants.FILE_SEPARATOR;
            slaveNodes.forEach(snode -> {
                String endPath = endRootPath + snode;
                List<String> deleteNodes = ZkClient.getZkClient().getChildren(endPath);
                deleteNodes.forEach(node -> endTask(endPath, node, taskNodes));
            });
        } else {
            log.info("can not get node = " + ZkConstants.FILE_END + " for end task!");
        }
    }

    /***
     * @description: 结束任务
     * @param endRootPath
     *            删除任务节点根目录
     * @param node
     *            任务节点
     * @param taskNodes
     *            任务节点根目录
     * @return: void
     */
    private boolean endTask(String endRootPath, String node, List<String> taskNodes) {
        String endPath = endRootPath + ZkConstants.FILE_SEPARATOR + node;
        log.info("get end node = " + endPath);
        String data = ZkClient.getZkClient().getData(endPath);
        VsdTask vsdTask = new VsdTask(Long.parseLong(node));
        boolean isStream = false;
        int oldStatus = 1;
        try {
            ExectaskEndProgress exectaskProgress = JSONObject.parseObject(data, ExectaskEndProgress.class);
            if (exectaskProgress != null) {
                // 若end节点上出现由于分析资源不足而失败的任务，需要把这些任务重新装载。当前流程需要忽略这些数据
                boolean reloadable = isReloadable(exectaskProgress);
                if (reloadable) {
                    return true;
                }
                vsdTask = vsdTaskMapper.selectById(node);
                if (vsdTask != null) {
                    oldStatus = vsdTask.getStatus().intValue();
                    isStream = !BitCommonConst.isRealTimeTask(vsdTask.getParam());
                    int status = exectaskProgress.getStatus();
                    log.error(">>>>>>>>>end progress of taskId:{},status:{}", node, status);
                    if (status == TaskConstants.TASK_STATUS_FAILED) {

                        int retryCount =
                                vsdTask.getRetryCount() >= TaskConstants.MAX_RETRYCOUNT ? 0 : vsdTask.getRetryCount();
                        log.error(">>>>>>>>>taskId:{},retrycount:{},isStream:{}, add by 1", node, retryCount, isStream);

                        vsdTask.setRetryCount(retryCount + 1);
                        if (isStream || vsdTask.getRetryCount() < nacosConfig.getRetryCount()) {
                            log.error(">>>>>>>>>>> retrycount in task:{},max retrycount in nacos:{}",
                                    vsdTask.getRetryCount(), nacosConfig.getRetryCount());
                            status = TaskConstants.TASK_STATUS_WAIT;
                        }
                        vsdTask.setSlaveip("");
                    }
                    vsdTask.setStatus(status);
                    vsdTask.setProgress(exectaskProgress.getProgress());
                    vsdTask.setErrmsg(exectaskProgress.getErrMsg());
                    vsdTask.setErrcode(exectaskProgress.getErrMsgNumber());
                    vsdTask.setEndtime(DateUtil.now());
                    vsdTaskMapper.updateById(vsdTask);
                    log.info(
                            ">>>>>>>>>>>>status changed because of updExectaskEnd.... taskId:{},old_status:{},new_status:{}",
                            node, oldStatus, status);
                }
            } else {
                log.info(">>>>>>>> error exectaskProgress:{}", data);
            }
        } catch (JSONException e) {
            int status = TaskConstants.TASK_STATUS_FAILED;
            log.error(">>>>>>>>>taskId:{},retrycount:{},add by 1", node, vsdTask.getRetryCount());
            vsdTask.setRetryCount(vsdTask.getRetryCount() + 1);
            if (isStream || vsdTask.getRetryCount() < nacosConfig.getRetryCount()) {
                status = TaskConstants.TASK_STATUS_WAIT;
            }
            vsdTask.setStatus(status);
            vsdTaskMapper.updateById(vsdTask);
            log.info(">>>>>>>>>>>>status changed because of updExectaskEnd.... taskId:{},old_status:{},new_status:{}",
                    node, oldStatus, status);
        }

        log.info(" >>>>>delete end node: " + endPath);
        // 删除end节点
        deleteNode(endPath);
        // 删除所有task节点下的该任务节点
        taskNodes.forEach(taskNode -> deleteNode(
                ZkConstants.FILE_TASK + ZkConstants.FILE_SEPARATOR + taskNode + ZkConstants.FILE_SEPARATOR + node));
        return true;
    }

    /***
     * @description: 暂停zk任务
     * @return: boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stopZkTask() {
        try {
            List<VsdTask> vsdTaskList = vsdTaskMapper.selectList(getQueryWrapper(TaskConstants.TASK_STATUS_RUNNING)
                    .notIn("type", TaskConstants.ANALY_TYPE_OTHER, TaskConstants.ANALY_TYPE_DESITTY)
                    .eq("isvalid", TaskConstants.TASK_ISVALID_OFF));
            if (!vsdTaskList.isEmpty()) {
                String stopRoot = ZkConstants.FILE_STOP + ZkConstants.FILE_SEPARATOR;
                Map<Long, String> taskToSlaveMap = getTaskToSlaveMap();
                vsdTaskList.forEach(vsdTask -> stopTask(taskToSlaveMap, vsdTask, stopRoot));
            }
        } catch (Exception e) {
            log.error("stopZkTask error", e);
        }
    }

    /***
     * @description: 停止任务
     * @param taskToSlaveMap
     *            任务ID与slaveId的映射关联Map
     * @param vsdTask
     *            任务对象
     * @param stopRoot
     *            sotp根目录
     * @return: void
     */
    private void stopTask(Map<Long, String> taskToSlaveMap, VsdTask vsdTask, String stopRoot) {
        if (taskToSlaveMap.containsKey(vsdTask.getId())) {
            String filePath =
                    stopRoot + taskToSlaveMap.get(vsdTask.getId()) + ZkConstants.FILE_SEPARATOR + vsdTask.getId();
            if (!ZkClient.getZkClient().isExistNode(filePath)) {
                log.info(">>>>>>>>stop task serialnumber = " + vsdTask.getSerialnumber() + " and time = "
                        + System.currentTimeMillis());
                ZkClient.getZkClient().createNode(filePath, "stop");
            }
        } else {
            log.info("task no find stop node:" + vsdTask.getId());
            VsdTask task = new VsdTask();
            task.setId(vsdTask.getId());
            task.setStatus(2);
            task.setEndtime(DateUtil.now());
            vsdTaskMapper.updateById(task);
        }
    }

    /***
     * @description: 获取所有正在运行中的任务ID与slaveId的映射关联Map
     * @return: java.util.Map<java.lang.Long, java.lang.String>
     */
    private Map<Long, String> getTaskToSlaveMap() {
        Map<Long, String> taskToSlaveMap = new HashMap<>();
        if (ZkClient.getZkClient().isExistNode(ZkConstants.FILE_TASK)) {
            List<String> taskNode = ZkClient.getZkClient().getChildren(ZkConstants.FILE_TASK);
            String taskRoot = ZkConstants.FILE_TASK + ZkConstants.FILE_SEPARATOR;
            taskNode.forEach(nodeId -> {
                List<String> taskIdList = ZkClient.getZkClient().getChildren(taskRoot + nodeId);
                taskIdList.forEach(taskId -> taskToSlaveMap.put(Long.parseLong(taskId), nodeId));
            });
        }
        return taskToSlaveMap;
    }

    /**
     * @description: 监控slave是否崩溃
     * @return: boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void monitorZkNode() {
        try {
            if (!ZkClient.getZkClient().isExistNode(ZkConstants.FILE_TASK)) {
                log.info("can not get node = " + ZkConstants.FILE_TASK + " for monitor");
                return;
            }
            List<String> taskNodeList = ZkClient.getZkClient().getChildren(ZkConstants.FILE_TASK);
            List<ExectaskNode> nodeList = getNodes();
            if (nodeList.isEmpty()) {
                log.info("can not get node = " + ZkConstants.FILE_NODES + " for monitor");
            }
            Map<String, ExectaskNode> nodeMap =
                    nodeList.stream().collect(Collectors.toMap(ExectaskNode::getSlaveId, a -> a, (k1, k2) -> k1));
            taskNodeList.forEach(taskNode -> {
                if (nodeMap.containsKey(taskNode)) {
                    updateSlaveSuccess(nodeMap.get(taskNode));
                } else {
                    List<String> taskIdList = ZkClient.getZkClient()
                            .getChildren(ZkConstants.FILE_TASK + ZkConstants.FILE_SEPARATOR + taskNode);
                    if (taskIdList.isEmpty()) {
                        deleteNodeBySlave(taskNode);
                    } else {
                        taskIdList.forEach(taskId -> dealMonitorNode(Long.parseLong(taskId), taskNode));
                    }
                    updateSlaveError(taskNode);
                }
            });
        } catch (Exception e) {
            log.error("monitorZkNode error", e);
        }
    }

    @Override
    public void resetErrorTask() {
        Long now = System.currentTimeMillis();
        if (now - resetTime > 1000 * 60 * 2) {
            List<ExectaskNode> exectaskNodeList = getNodes();
            List<String> idList = new ArrayList<>();
            String taskPath = ZkConstants.FILE_TASK + ZkConstants.FILE_SEPARATOR;
            String endPath = ZkConstants.FILE_END + ZkConstants.FILE_SEPARATOR;
            exectaskNodeList.forEach(p -> {
                String parentPath = taskPath + p.getSlaveId();
                if (ZkClient.getZkClient().isExistNode(parentPath)) {
                    idList.addAll(ZkClient.getZkClient().getChildren(parentPath));
                }
                parentPath = endPath + p.getSlaveId();
                if (ZkClient.getZkClient().isExistNode(parentPath)) {
                    idList.addAll(ZkClient.getZkClient().getChildren(parentPath));
                }
            });
            Set<Long> idSet = new HashSet<>();
            idList.forEach(p -> idSet.add(Long.parseLong(p)));
            List<VsdTask> vsdTasks = vsdTaskMapper.selectList(
                    getQueryWrapper(TaskConstants.TASK_STATUS_RUNNING).eq("isValid", TaskConstants.TASK_ISVALID_ON));
            List<Long> ids = new ArrayList<>();
            vsdTasks.forEach(vsdTask -> {
                if (!idSet.contains(vsdTask.getId())) {
                    ids.add(vsdTask.getId());
                }
            });
            if (!ids.isEmpty()) {
                vsdTaskMapper.updateResetList(ids);
                log.info(">>>>>>>>>>>>status changed because of resetErrorTask.... taskId:{},old_status:1,new_status:0",
                        ids.toArray());
            }
            setResetTime(now);
        }
    }

    @Override
    public void deleteRepeat() {
        if (ZkClient.getZkClient().isExistNode(ZkConstants.FILE_TASK)) {
            List<String> nodes = ZkClient.getZkClient().getChildren(ZkConstants.FILE_TASK);
            Set<String> taskIds = new HashSet<>();
            nodes.forEach(node -> {
                String nodePath = ZkConstants.FILE_TASK + ZkConstants.FILE_SEPARATOR + node;
                List<String> tasks = ZkClient.getZkClient().getChildren(nodePath);
                tasks.forEach(task -> {
                    if (taskIds.contains(task)) {
                        String taskPath = nodePath + ZkConstants.FILE_SEPARATOR + task;
                        log.info(" >>>>>delete repeat node: " + taskPath);
                        ZkClient.getZkClient().deleteNode(taskPath);
                    } else {
                        taskIds.add(task);
                    }
                });
            });
        }
    }

    public String checkRepeatTask(String taskId) {
        if (ZkClient.getZkClient().isExistNode(ZkConstants.FILE_TASK)) {
            List<String> nodes = ZkClient.getZkClient().getChildren(ZkConstants.FILE_TASK);
            for (String node : nodes) {
                String nodePath = ZkConstants.FILE_TASK + ZkConstants.FILE_SEPARATOR + node;
                List<String> tasks = ZkClient.getZkClient().getChildren(nodePath);
                for (String task : tasks) {
                    if (taskId.equals(task)) {
                        return nodePath + ZkConstants.FILE_SEPARATOR + task;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean isNodeExisted() {
        boolean flag = ZkClient.getZkClient().isExistNode(ZkConstants.FILE_NODES);
        if (!flag) {
            log.error("zookeeper objext node is not existed!");
        }
        return flag;
    }

    /**
     * 由于任务状态延迟，部分任务加入zk之后没有获取到分析资源， 这部分任务需要重新加载成待分析状态
     */
    @Override
    public void reloadTask() {
        if (ZkClient.getZkClient().isExistNode(ZkConstants.FILE_END)) {
            List<String> slaveNodes = ZkClient.getZkClient().getChildren(ZkConstants.FILE_END);
            List<String> taskNodes = ZkClient.getZkClient().getChildren(ZkConstants.FILE_TASK);
            String endRootPath = ZkConstants.FILE_END + ZkConstants.FILE_SEPARATOR;
            if (slaveNodes != null) {
                log.info(">>>>>>>>>>>>slaveNodes:{}", slaveNodes);
                slaveNodes.forEach(snode -> {
                    String endPath = endRootPath + snode;
                    List<String> endNodes = ZkClient.getZkClient().getChildren(endPath);
                    endNodes.forEach(node -> reloadTask(endPath, node, taskNodes));
                });
            }
        }
    }

    /***
     * @description: 重置没有获取到资源的任务
     * @param endRootPath
     *            删除任务节点根目录
     * @param node
     *            任务节点
     * @param taskNodes
     *            任务节点根目录
     * @return: void
     */
    private void reloadTask(String endRootPath, String node, List<String> taskNodes) {
        String endPath = endRootPath + ZkConstants.FILE_SEPARATOR + node;
        String data = ZkClient.getZkClient().getData(endPath);
        try {
            ExectaskEndProgress exectaskProgress = JSONObject.parseObject(data, ExectaskEndProgress.class);
            // 若end节点上出现由于分析资源不足而失败的任务，需要把这些任务重新装载
            if (exectaskProgress != null) {
                boolean reloadable = isReloadable(exectaskProgress);
                log.info(">>>> slaveId:" + exectaskProgress.getSlaveId() + " ,taskId:" + node + ",status:"
                        + exectaskProgress.getStatus() + ",errcode:" + exectaskProgress.getErrMsgNumber() + ",errmsg:"
                        + exectaskProgress.getErrMsg());
                if (reloadable) {
                    log.info(">>>>reloading task,taskId:" + node);
                    VsdTask vsdTask = new VsdTask(Long.parseLong(node));
                    vsdTask.setStatus(TaskConstants.TASK_STATUS_WAIT);
                    vsdTask.setSlaveip("");
                    vsdTaskMapper.updateById(vsdTask);
                    log.info(">>>>>>>>>>>>status changed because of reloadTask.... taskId:{},old_status:1,new_status:0",
                            node);
                    log.info(" >>>>>delete reload node : " + endPath);
                    // 删除end节点
                    deleteNode(endPath);
                    // 删除所有task节点下的该任务节点
                    taskNodes.forEach(taskNode -> deleteNode(ZkConstants.FILE_TASK + ZkConstants.FILE_SEPARATOR
                            + taskNode + ZkConstants.FILE_SEPARATOR + node));
                }
            } else {
                log.info(">>>>>>>> error exectaskProgress:{}", data);
            }
        } catch (JSONException e) {
            log.error("parse json exception:" + e.getMessage() + ",data:" + data);
        }
    }

    /**
     * 判断zk end节点上失败的任务是不是分析资源不够或者优先级不足
     *
     * @param exectaskProgress
     * @return
     */
    private boolean isReloadable(ExectaskEndProgress exectaskProgress) {
        if (exectaskProgress.getErrMsgNumber() != null) {
            boolean reloadable =
                    TaskConstants.TASK_ERROR_CODE_NO_RESOURCE.intValue() == exectaskProgress.getErrMsgNumber().intValue()
                            || TaskConstants.TASK_ERROR_CODE_NO_WORKER.intValue() == exectaskProgress.getErrMsgNumber()
                            .intValue()
                            || TaskConstants.TASK_ERROR_CODE_NO_PRIORITY == exectaskProgress.getErrMsgNumber().intValue();
            return TaskConstants.TASK_STATUS_FAILED == exectaskProgress.getStatus() && reloadable;
        } else {
            log.info(">>>>>>>>>>>>>>>>> getErrMsgNumber error:{}", exectaskProgress.getErrMsgNumber());
            return false;
        }

    }

    /**
     * @description: 获取/exectask/nodes/
     * @return: com.keensense.task.entity.zkTaskEntity.ExectaskNode
     */
    private List<ExectaskNode> getNodes() {
        List<ExectaskNode> exectaskNodeList = new ArrayList<>();
        try {
            ZkClient zkClient = ZkClient.getZkClient();
            if (zkClient.isExistNode(ZkConstants.FILE_NODES)) {
                List<String> nodes = zkClient.getChildren(ZkConstants.FILE_NODES);
                for (String node : nodes) {
                    String data =
                            zkClient.getData(ZkConstants.FILE_NODES + ZkConstants.FILE_SEPARATOR + node);
                    ExectaskNode exectaskNode = JSONObject.parseObject(data, ExectaskNode.class);
                    if (exectaskNode != null) {
                        exectaskNodeList.add(exectaskNode);
                    }
                }
            }
        } catch (Exception e) {
            log.error("getExectaskNodes error", e);
        }
        return exectaskNodeList;
    }

    private void dealMonitorNode(Long node, String slave) {
        VsdTask vsdTask = vsdTaskMapper.selectById(node);
        if (vsdTask != null) {
            boolean deleteEnd = ZkClient.getZkClient().isExistNode(
                    ZkConstants.FILE_END + ZkConstants.FILE_SEPARATOR + slave + ZkConstants.FILE_SEPARATOR + node);
            vsdTask.setStatus(TaskConstants.TASK_STATUS_WAIT);

            if (vsdTaskMapper.updateDeadTaskByVersion(node, vsdTask.getVersion(), vsdTask.getStatus()) > 0) {
                log.info(">>>>>>>>>>>>status changed because of monitorZkNode.... taskId:{},old_status:1,new_status:0",
                        node);
                deleteNode(
                        ZkConstants.FILE_TASK + ZkConstants.FILE_SEPARATOR + slave + ZkConstants.FILE_SEPARATOR + node);
                deleteNode(
                        ZkConstants.FILE_STOP + ZkConstants.FILE_SEPARATOR + slave + ZkConstants.FILE_SEPARATOR + node);
                // 删除progress进度下信息
                deleteNode(ZkConstants.FILE_PROGRESS + ZkConstants.FILE_SEPARATOR + node);
            }
            if (deleteEnd) {
                log.info(" >>>>>delete end node in monitor : " + node);
                ZkClient.getZkClient().deleteNode(
                        ZkConstants.FILE_END + ZkConstants.FILE_SEPARATOR + slave + ZkConstants.FILE_SEPARATOR + node);
            }
        }
    }

    private void deleteNode(String path) {
        if (ZkClient.getZkClient().isExistNode(path)) {
            ZkClient.getZkClient().deleteNode(path);
        }
    }

    /***
     * @description: 删除节点信息
     * @param slaveId
     *            节点ID
     * @return: void
     */
    private void deleteNodeBySlave(String slaveId) {
        String slaveIdPath = ZkConstants.FILE_SEPARATOR + slaveId;
        deleteNodeRoot(ZkConstants.FILE_TASK + slaveIdPath);
        deleteNodeRoot(ZkConstants.FILE_END + slaveIdPath);
        deleteNodeRoot(ZkConstants.FILE_STOP + slaveIdPath);
    }

    /***
     * @description: 删除节点
     * @param filePath
     *            文件路径
     * @return: void
     */
    private void deleteNodeRoot(String filePath) {
        if (ZkClient.getZkClient().isExistNode(filePath)) {
            List<String> taskIdList = ZkClient.getZkClient().getChildren(filePath);
            if (taskIdList.isEmpty()) {
                ZkClient.getZkClient().deleteNode(filePath);
            }
        }
    }

    /***
     * @description: 更新vsd_slavestatus表信息
     * @param exectaskNode
     *            节点信息
     * @return: void
     */
    private void updateSlaveSuccess(ExectaskNode exectaskNode) {
        if (vsdSlaveMap.containsKey(exectaskNode.getIp())) {
            VsdSlave vsdSlave = vsdSlaveMap.get(exectaskNode.getIp());
            getVsdSlaveByNode(vsdSlave, exectaskNode);
            vsdSlaveService.updateVsdSlave(vsdSlave);
        } else {
            VsdSlave vsdSlave =
                    vsdSlaveService.selectByWrapper(new QueryWrapper<VsdSlave>().eq("slave_ip", exectaskNode.getIp()));
            if (vsdSlave == null) {
                vsdSlave = new VsdSlave();
                getVsdSlaveByNode(vsdSlave, exectaskNode);
                vsdSlaveService.insert(vsdSlave);
            } else {
                getVsdSlaveByNode(vsdSlave, exectaskNode);
                vsdSlaveService.updateVsdSlave(vsdSlave);
                vsdSlaveMap.put(exectaskNode.getIp(), vsdSlave);
            }
        }
    }

    /***
     * @description: 更新vsd_slavestatus表错误信息
     * @param ip
     *            ip地址
     * @return: void
     */
    private void updateSlaveError(String ip) {
        VsdSlave vsdSlave = vsdSlaveService.selectByWrapper(new QueryWrapper<VsdSlave>().eq("slave_ip", ip));
        if (vsdSlave != null) {
            vsdSlave.setValid(TaskConstants.SLAVE_VALID_OFF);
            vsdSlave.setLastupdateTime(DateUtil.now());
            vsdSlaveService.updateVsdSlave(vsdSlave);
        }
    }

    /***
     * @description: 根据Node节点信息获取vsdSlave对象
     * @param vsdSlave
     *            vsdSlave对象
     * @param exectaskNode
     *            节点信息
     * @return: void
     */
    private void getVsdSlaveByNode(VsdSlave vsdSlave, ExectaskNode exectaskNode) {
        vsdSlave.setValid(TaskConstants.SLAVE_VALID_ON);
        vsdSlave.setSlaveIp(exectaskNode.getIp());
        vsdSlave.setReserve("");
        vsdSlave.setObjextCapability(exectaskNode.getCapability().getObjext());
        vsdSlave.setVlprCapability(exectaskNode.getCapability().getVlpr());
        vsdSlave.setFaceCapability(exectaskNode.getCapability().getFace());
        vsdSlave.setLastupdateTime(DateUtil.now());
        List<VsdSlaveTask> list = new ArrayList<>();
        List<String> taskIds = new ArrayList<>();
        // taskId转换成serialnumber
        for (SlaveStatus slaveStatus : exectaskNode.getSlaveStatus()) {
            for (String taskId : slaveStatus.getTaskIds()) {
                taskIds.add(taskId);
            }
        }
        if (taskIds != null && taskIds.size() > 0) {
            List<String> serialnumbers = vsdTaskMapper.getSerialnumberByTaskIds(taskIds);
            for (String serialnumber : serialnumbers) {
                list.add(new VsdSlaveTask(serialnumber));
            }
        }
        vsdSlave.setPayload(JSON.toJSONString(getPayload(list, exectaskNode)));
    }

    /***
     * @description: 获取vsd_slavestatus中payload的值
     * @param list
     *            任务列表
     * @param exectaskNode
     *            节点信息
     * @return: java.util.Map
     */
    private Map<String, Object> getPayload(List<VsdSlaveTask> list, ExectaskNode exectaskNode) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("tasks", list);
        map.put("cpu", exectaskNode.getCpu());
        map.put("ram", exectaskNode.getRam());
        map.put("diskfreespace", exectaskNode.getDiskfreespace());
        return map;
    }

    /***
     * @description: 获取查询条件对象
     * @param status
     *            状态
     * @return: com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.keensense.task.entity.VsdTask>
     */
    private QueryWrapper<VsdTask> getQueryWrapper(int status) {
        return new QueryWrapper<VsdTask>().eq("status", status);
    }

    private static void setResetTime(Long resetTime) {
        ZkTaskServiceImpl.resetTime = resetTime;
    }
}
