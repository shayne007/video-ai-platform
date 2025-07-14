package com.keensense.task.entity.zkTaskEntity;

import lombok.Data;

import java.util.List;

/**
 * @Description: 节点信息
 * @Author: wujw
 * @CreateDate: 2019/10/14 19:14
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
public class NodeInfo {

    private String slaveId;

    private String ip;

    private Capability capability;

    private int taskCount;

    /**
     * 服务器剩余路数
     */
    private Integer totalNumberOfFreeSlots;

    /**
     * 每张显卡的分析路数
     */
    private int numberOfTasksPerChildProcess;

    /**
     * 显卡数量
     */
    private int totalNumberOfChildProcesses;

    /**
     * 总任务数
     */
    private int totalTasks;

    /**
     * 服务器内存
     */
    private List<Long> ram;

    public NodeInfo(String slaveId, String ip, Capability capability, int taskCount,
                    Integer totalNumberOfFreeSlots,List<Long> ram,int numberOfTasksPerChildProcess,
                    int totalNumberOfChildProcesses,int totalTasks){
        this.slaveId = slaveId;
        this.ip = ip;
        this.capability = capability;
        this.taskCount = taskCount;
        this.totalNumberOfFreeSlots = totalNumberOfFreeSlots;
        this.ram = ram;
        this.numberOfTasksPerChildProcess = numberOfTasksPerChildProcess;
        this.totalNumberOfChildProcesses = totalNumberOfChildProcesses;
        this.totalTasks = totalTasks;
    }

}
