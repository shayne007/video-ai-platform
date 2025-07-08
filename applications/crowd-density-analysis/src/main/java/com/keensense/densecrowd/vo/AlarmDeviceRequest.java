package com.keensense.densecrowd.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @Author: zengyc
 * @Description: 1.9    像机分页查询参数
 * @Date: Created in 10:38 2019/9/29
 * @Version v0.1
 */
@Data
public class AlarmDeviceRequest extends AlarmPageRequest {
    /**
     * 像机作用类型(排队计数3,人群密度4)
     */
    @ApiModelProperty(value = "像机作用类型(排队计数3,人群密度4)", required = false, example = "1")
    @NotBlank
    @Length(min = 1, max = 2)
    private String cameraServerType;//	string	1-2	是
    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID", required = false, example = "2")
    @Length(max = 32)
    private String deviceId;//	string	1-32	否
    /**
     * 区域ID
     */
    @ApiModelProperty(value = "区域ID", required = false, example = "3")
    @Length(max = 32)
    private String areaId;//	string	1-32	否

}
