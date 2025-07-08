package com.keensense.densecrowd.vo.roi;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 11:11 2019/10/8
 * @Version v0.1
 */
@Data
public class Param {
    /**
     * 相机分辨率高
     */
    @NotNull
    @Range(min = 1, max = 99999)
    Integer height;
    /**
     * 相机分辨率宽
     */
    @NotNull
    @Range(min = 1, max = 99999)
    Integer width;

    @NotNull
    @Valid
    List<Rois> rois;
}