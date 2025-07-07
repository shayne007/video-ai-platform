package com.keensense.task.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/5/23 11:23
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
public class SearchTaskEntity {

    private String id;

    private Integer taskType;

    private String serialnumber;

    private Integer progress;

    private String remark;

    private Timestamp entryTime;

}
