package com.keensense.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description: 盒子与任务的关联对象
 * @Author: wujw
 * @CreateDate: 2019/5/21 16:47
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
@TableName("vsd_task_bit")
public class VsdTaskBit implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public static final int STATUS_WAIT = 0;
    public static final int STATUS_PROCESS = 1;

    public VsdTaskBit(){}

    public VsdTaskBit(String serialnumber, Long bitMachineId) {
        this.serialnumber = serialnumber;
        this.bitMachineId = bitMachineId;
        status = STATUS_WAIT;
    }

    private Long id;

    private String serialnumber;

    private Long bitMachineId;

    private Integer status;

}
