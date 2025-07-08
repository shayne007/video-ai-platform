package com.keensense.admin.dto.cluster;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 15:13 2019/11/21
 * @Version v0.1
 */
@Data
public class OperateVo {
    /**
     * 点位
     */
    @ApiModelProperty(value = "点位名称", example = "123")
    @Length(max = 10000)
    private String cameraIds;
    /**
     * 阈值
     */
    @ApiModelProperty(required = true, value = "阈值", example = "50")
    @NotNull
    @Range(min = 1, max = 100)
    private Integer threshold = 50;

    /**
     * 开始时间
     */
    @ApiModelProperty(required = true, value = "开始时间", example = "2019-11-18 00:00:00")
    @NotBlank
    @Length(min = 19, max = 19)
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(required = true, value = "结束时间", example = "2019-11-20 00:00:00")
    @NotBlank
    @Length(min = 19, max = 19)
    private String endTime;

    /**
     * 分析类型
     */
    @ApiModelProperty(required = true, value = "分析类型", example = "1")
    @NotNull
    @Range(min = 1, max = 4)
    private Integer objType;
}
