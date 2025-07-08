package com.keensense.densecrowd.vo.roi;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 11:12 2019/10/8
 * @Version v0.1
 */
@Data
public class Rois {
    @NotBlank
    @Length(min = 1, max = 20)
    String roiName;

    @NotNull
    @Valid
    List<Points> points;
}
