package com.keensense.task.entity;

import lombok.Data;

/**
 * @Description: 解析vas url获取到的数据对象
 * @Author: wujw
 * @CreateDate: 2019/5/14 14:41
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
public class VasUrlEntity {
    /**
     * 开始时间
     */
    private Long startTime;
    /**
     * 结束时间
     */
    private Long endTime;
    /**
     * 录像地址
     */
    private String vasUrl;

    public VasUrlEntity(Long startTime, Long endTime, String vasUrl) {
        this.endTime = endTime;
        this.startTime = startTime;
        this.vasUrl = vasUrl;
    }

}
