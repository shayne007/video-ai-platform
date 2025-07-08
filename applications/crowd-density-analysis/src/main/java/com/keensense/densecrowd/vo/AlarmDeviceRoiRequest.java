package com.keensense.densecrowd.vo;

import com.keensense.densecrowd.vo.roi.Param;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 10:50 2019/10/8
 * @Version v0.1
 */
@Data
@ApiModel("相机ROI设置")
public class AlarmDeviceRoiRequest {

    /**
     * 相机编号
     */
    @ApiModelProperty(required = true, value = "相机编号", example = "50011204001310000100")
    @NotBlank
    @Length(min = 1, max = 20)
    String deviceId;


    /**
     * JSON对象
     */
    @ApiModelProperty(required = true, value = "JSON对象")
    @Valid
    @NotNull
    Param param;
}




