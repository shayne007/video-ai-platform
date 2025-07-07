package com.keensense.task.entity.zkTaskEntity;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/5/23 11:23
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
public class ExectaskNode {

    private String slaveId;
    private String ip;
    private Capability capability;
    private Integer numberOfGpus;
    private Integer numberOfChildProcessesPerGpu;
    private Integer numberOfTasksPerGpu;
    private Integer numberOfTasksPerChildProcess;
    private Integer totalNumberOfChildProcesses;
    private Integer totalNumberOfConcurrencyTasks;
    private Integer appStatusUpdateIntervalSec;
    private Integer appTasksProgressUpdateIntervalSec;
    private Integer isEnableAppAutoRestartCrashedChildProcess;
    private Integer totalTasks;
    private List<SlaveStatus> slaveStatus;
    private Date updateTime;

    private Integer cpu;
    private List<Long> ram;
    private List<Long> diskfreespace;

    /**
     * gpu剩余总路数
     */
    private Integer totalNumberOfFreeSlots;
}
