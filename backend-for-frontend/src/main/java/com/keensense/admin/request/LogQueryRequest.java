package com.keensense.admin.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class LogQueryRequest implements Serializable {

    @ApiModelProperty("页码")
    private int page;
    @ApiModelProperty("条数")
    private int rows;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("筛选条件,逗号分隔 [查询1,新增2,修改3,删除4,登录5,退出6,其他7")
    private String actionType;
    @ApiModelProperty("关键词")
    private String moduleName;
    @ApiModelProperty("用户名")
    private String userName;
}
