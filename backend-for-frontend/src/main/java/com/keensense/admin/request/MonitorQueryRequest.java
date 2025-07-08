package com.keensense.admin.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("监控组列表查询参数")
public class MonitorQueryRequest extends PageRequest implements Serializable {

    @ApiModelProperty("监控组名称")
    private String groupName;

    @ApiModelProperty("状态 [1:已启动 ；0:未启动]")
    private String status;
}