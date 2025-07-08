package com.keensense.densecrowd.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 16:11 2019/9/25
 * @Version v0.1
 */
@Data
@ApiModel(value = "人群密度布控任务下发")
public class AlarmTaskAddRequest {
    String id;
    /**
     * 设备编号
     */
    @ApiModelProperty(required = true, value = "设备编号", example = "112233445566")
    @NotBlank
    @Length(min = 1, max = 20)
    String deviceId;//是

    /**
     * 布控名称
     */
    @ApiModelProperty(required = true, value = "布控名称", example = "test")
    @NotBlank
    @Length(min = 1, max = 20)
    String monitorName;//是

    /**
     * 布控阈值
     */
    @ApiModelProperty(required = true, value = "布控阈值(1-3)", example = "1")
    @NotNull
    @Range(min = 0, max = 999)
    Integer threshold;//	int	1-3	是

    /**
     * 布控开始时间（为空则一直布控）
     */
    @ApiModelProperty(required = false, value = "布控开始时间（为空则一直布控）", example = "0")
    @Range(min = 0, max = 9999999999999L)
    Long startTime = 0L;//	long	13	否

    /**
     * 布控结束时间（为空则一直布控）
     */
    @ApiModelProperty(required = false, value = "布控结束时间（为空则一直布控）", example = "0")
    @Range(min = 0, max = 9999999999999L)
    Long endTime = 0L;    //long	13	否

    /**
     * 报警间隔时间（分钟）
     */
    @ApiModelProperty(required = true, value = "报警间隔时间（分钟）", example = "0")
    @NotNull
    @Range(min = 0, max = 999)
    Integer interval;//	int	3	是	报警间隔时间（分钟）

    /**
     * 布控状态 0： 未布控 1：布控中
     */
    Integer status;
}
