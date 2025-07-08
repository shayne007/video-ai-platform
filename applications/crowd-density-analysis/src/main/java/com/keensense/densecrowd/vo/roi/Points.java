package com.keensense.densecrowd.vo.roi;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 11:11 2019/10/8
 * @Version v0.1
 */
@Data
public class Points {
    @NotNull
    @Range(min = 1, max = 99999)
    Integer x;

    @NotNull
    @Range(min = 1, max = 99999)
    Integer y;
}
