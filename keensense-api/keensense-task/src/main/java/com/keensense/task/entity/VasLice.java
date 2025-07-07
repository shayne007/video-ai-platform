package com.keensense.task.entity;

import lombok.Data;

/**
 * @Description: 分片时间对象
 * @Author: wujw
 * @CreateDate: 2019/5/15 14:16
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
public class VasLice {

    private Long startSecond;

    private Long endSecond;

    /**
     * 分析类型
     */
    private String analysisTypes;

    public VasLice(Long startSecond, Long endSecond) {
        this.startSecond = startSecond;
        this.endSecond = endSecond;
    }

    public VasLice(Long startSecond, Long endSecond,String analysisTypes) {
        this.startSecond = startSecond;
        this.endSecond = endSecond;
        this.analysisTypes = analysisTypes;
    }
}
