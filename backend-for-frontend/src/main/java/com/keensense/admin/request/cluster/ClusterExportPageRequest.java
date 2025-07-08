package com.keensense.admin.request.cluster;

import com.keensense.admin.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 13:57 2019/11/22
 * @Version v0.1
 */
@Data
@ApiModel(value = "聚类结果导出")
public class ClusterExportPageRequest extends PageRequest {
    @ApiModelProperty(value = "结束页面", example = "1")
    @NotNull
    @Range(min = 1, max = 999)
    private Integer endPage;

    @ApiModelProperty("聚类任务id")
    @NotBlank
    private String clusterId;
}
