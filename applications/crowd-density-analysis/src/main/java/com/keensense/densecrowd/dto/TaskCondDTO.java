package com.keensense.densecrowd.dto;


import com.keensense.densecrowd.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务信息DTO
 *
 * @author admin
 */
@Data
@ApiModel(value = "分析任务参数")
public class TaskCondDTO extends PageRequest {

    @ApiModelProperty(value = "任务状态 [0：等待分析 1：分析中 2：分析完成 3：分析失败]")
    private Short status;

    @ApiModelProperty(value = "起始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "监控点名称")
    private String cameraName;

    @ApiModelProperty(value = "监控点类型")
    private Long fromType;

    @ApiModelProperty(value = "文件名")
    private String fileName;

}
