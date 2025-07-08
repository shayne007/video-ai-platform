package com.keensense.densecrowd.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 14:17 2019/9/29
 * @Version v0.1
 */
@Data
public class AlarmDensecrowdRequest extends AlarmPageRequest {
    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID", required = false, example = "")
    private List<String> deviceId;//	list	1-32	否
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", required = false, example = "")
    @Range(min = 0, max = 9999999999999L)
    private Long startTime;//	string	13	否

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", required = false, example = "")
    @Range(min = 0, max = 9999999999999L)
    private Long endTime;//	string	1-32	否

}
