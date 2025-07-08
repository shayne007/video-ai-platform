package com.keensense.densecrowd.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 11:10 2019/6/12
 * @Version v0.1
 */
@Data
public class AlarmPageRequest {
    /**
     * 当前页码，默认1
     */
    @NotNull(message = "页码不能为空")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    @Range(min = 1, max = 99999999L)
    private int currentPage = 1;//	int	1-8	否
    /**
     * 每页记录条数，默认10
     */
    @NotNull(message = "记录条数为空")
    @ApiModelProperty(value = "记录条数", required = true, example = "10")
    @Range(min = 1, max = 99999999L)
    private int pageSize = 10;// int	1-8   否
}
