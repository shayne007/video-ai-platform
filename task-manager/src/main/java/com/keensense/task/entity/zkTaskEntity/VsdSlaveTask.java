package com.keensense.task.entity.zkTaskEntity;

import lombok.Data;

/**
 * @Description: 保存入Slave表payload字段的任务对象
 * @Author: wujw
 * @CreateDate: 2019/8/28 17:39
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
public class VsdSlaveTask {

    private String serialnumber;

    public VsdSlaveTask(String serialnumber){
        this.serialnumber = serialnumber;
    }
}
