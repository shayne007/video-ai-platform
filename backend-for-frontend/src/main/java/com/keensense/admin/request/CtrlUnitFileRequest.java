package com.keensense.admin.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel(value = "添加离线分析任务")
public class CtrlUnitFileRequest {
	@ApiModelProperty("录像时间校正")
	private Date entryTime;
	@ApiModelProperty("是否自动分析 [1自动分析, 0不自动]")
	private Integer autoAnalysisFlag;
	@ApiModelProperty("监控点id")
	@NotBlank(message="监控点不能为空")
	private String cameraId;

	@ApiModelProperty("文件id")
	@NotNull(message = "文件号不能为空")
	private Long fileId;

	private String videoType;

	@ApiModelProperty("选择点位最近的感兴趣区域: 0不勾选, 1勾选")
	@NotNull(message = "最近的感兴趣区域勾选字段不能为空")
	private Integer chooseInterest;

	@ApiModelProperty("白天夜晚场景")
	Integer sensitivity;
}
