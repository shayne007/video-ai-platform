package com.keensense.admin.request;

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
public class PageRequest {
    @NotNull(message = "页码不能为空")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    @Range(min = 1)
    private int page;
    @NotNull(message = "条数不能为空")
    @ApiModelProperty(value = "条数", required = true, example = "10")
    @Range(min = 1)
    private int rows;
}
