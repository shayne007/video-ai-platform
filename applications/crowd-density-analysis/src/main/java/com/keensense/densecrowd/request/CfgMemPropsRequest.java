package com.keensense.densecrowd.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "保存服务器配置参数")
public class CfgMemPropsRequest {

    /**
     * 参数key
     */
    @ApiModelProperty(value = "参数key")
    private String propKey;
    /**
     * 参数值
     */
    @ApiModelProperty(value = "参数值")
    private String propValue;
}
