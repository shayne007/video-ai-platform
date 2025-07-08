package com.keensense.densecrowd.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 17:30 2019/9/29
 * @Version v0.1
 */
@Data
public class AlarmTaskDelRequest {
    /**
     * 布控编号
     */
    @ApiModelProperty(value = "布控编号", required = true, example = "1")
    @NotBlank(message = "布控编号")
    @Length(min = 1, max = 20)
    private String id;
}
