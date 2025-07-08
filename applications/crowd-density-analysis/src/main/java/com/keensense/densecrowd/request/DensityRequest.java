package com.keensense.densecrowd.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 17:59 2019/9/25
 * @Version v0.1
 */
@Data
public class DensityRequest extends PageRequest {
    /**
     * 支持多任务查询，例如：serialnumber1,serialnumber2，.....，以英文状态下“,”逗号分隔。    最大支持100个任务的检索。
     */
    @ApiModelProperty(value = "任务号", required = false, example = "123456")
    private String serialnumber;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", required = false, example = "2019-09-27 01:01:01")
    private String startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", required = false, example = "2019-09-27 01:01:01")
    private String endTime;

    /**
     * 人群密度最小值
     */
    @ApiModelProperty(value = "人群密度最小值", required = false, example = "1")
    private Integer countMin;
    /**
     * 人群密度最大值
     */
    @ApiModelProperty(value = "人群密度最大值", required = false, example = "10")
    private Integer countMax;

    /**
     * 时间排序
     */
    @ApiModelProperty(value = "时间排序", required = false, example = "desc")
    private String createTimeOrder;

    /**
     * 导出开始页
     */
    @ApiModelProperty(value = "导出开始页", required = false, example = "1")
    private Integer pageStart;

    /**
     * 导出结束页
     */
    @ApiModelProperty(value = "导出结束页", required = false, example = "1")
    private Integer pageEnd;

    @ApiModelProperty(value = "设备号(多个以,分隔)", example = "123456")
    private String deviceIds;

    /**
     * 记录类型：1为历史记录
     */
    @ApiModelProperty(value = "记录类型", example = "1")
    private Integer recordType;
}
