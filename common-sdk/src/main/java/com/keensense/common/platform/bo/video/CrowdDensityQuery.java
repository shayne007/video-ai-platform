package com.keensense.common.platform.bo.video;

import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 10:36 2019/9/25
 * @Version v0.1
 */
@Data
public class CrowdDensityQuery {
    /**
     * 支持多任务查询，例如：serialnumber1,serialnumber2，.....，以英文状态下“,”逗号分隔。    最大支持100个任务的检索。
     */
    private String serialnumber;

    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 人群密度最大值
     */
    private Integer countMax;

    /**
     * 人群密度最小值
     */
    private Integer countMin;
    /**
     * 页码
     */
    private int pageNo = 1;

    /**
     * 每页数据
     */
    private int pageSize = 10;

    /**
     * 设备号
     */
    private String deviceIds;

    /**
     * 创建时间排序 desc|asc
     */
    private String createTimeOrder;

    /**
     * 排序字段
     */
    private String orderField;

    /**
     * 排序方式 desc|asc
     */
    private String orderMethod;

    /**
     * 记录类型：1为历史记录
     */
    private Integer recordType;
}
