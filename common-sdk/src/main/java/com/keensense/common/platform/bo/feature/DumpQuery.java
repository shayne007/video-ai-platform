package com.keensense.common.platform.bo.feature;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 18:55 2019/11/21
 * @Version v0.1
 */
@Data
public class DumpQuery {
    @NotBlank
    String task;

    @NotBlank
    @Length(min = 19, max = 19)
    String from;

    @NotBlank
    @Length(min = 19, max = 19)
    String to;

    @NotNull
    @Range(min = 1, max = 4)
    Integer objType;

    String startAt;

    String uuid;
}
