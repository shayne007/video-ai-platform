package com.keensense.admin.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("案件列表查询参数")
public class CaseQueryRequest extends PageRequest implements Serializable {

    @ApiModelProperty(value = "案件名称名称",example = "0")
    private String caseName;

    @ApiModelProperty("状态 [1:进行中，2:已结案]")
    private String caseState;
}