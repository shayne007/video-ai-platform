package com.keensense.task.entity.zkTaskEntity;/**
 * Created by zhanx xiaohui on 2019/6/28.
 */

import java.util.List;
import lombok.Data;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/6/28 13:32
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
public class SlaveStatus {
    private Integer gpuIndex;
    private Integer processId;
    private Integer isDead;
    private Integer numberOfFreeSlots;
    private List<String> taskIds;
}

