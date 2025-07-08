package com.keensense.admin.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("排行榜查询参数")
public class RankingListRequest extends PageRequest implements Serializable {

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;
}