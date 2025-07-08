package com.keensense.admin.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 18:16 2019/6/19
 * @Version v0.1
 */
@Data
@ApiModel
public class InterestSettingsRequest {
    @NotBlank(message = "文件id不能为空")
    @ApiModelProperty("离线视频id")
    Long fileId;

    @NotBlank(message = "不能为空")
    @ApiModelProperty("感兴趣区域/不感兴趣标志:0为不感兴趣区域,1为感兴趣区域")
    Integer interestFlag;

    @NotBlank(message = "不能为空")
    @ApiModelProperty("感兴趣区域/不感兴趣区域")
    String interestParam;

    @ApiModelProperty("选择类型 ： 1 ：感兴趣区域 ; 2 跨线")
    Integer chooseType;

    @ApiModelProperty("感（不感）兴趣区域参数")
    String tripwires;

    @ApiModelProperty("异常行为检测标志")
    Integer overlineType;

    @ApiModelProperty("默认场景")
    Integer scene;

    @ApiModelProperty("独立人脸输出")
    String enableIndependentFaceSnap;

    @ApiModelProperty("室内室外")
    String enableBikeToHuman;
}
