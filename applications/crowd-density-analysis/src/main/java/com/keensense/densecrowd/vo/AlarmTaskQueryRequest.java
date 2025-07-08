package com.keensense.densecrowd.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 17:05 2019/9/29
 * @Version v0.1
 */
@Data
public class AlarmTaskQueryRequest extends AlarmPageRequest {
    @ApiModelProperty(value = "设备ID", required = false, example = "")
    @Length(max = 20)
    private String deviceId;//	string	1-20	否 	相机编号，为空时查询全部
}
