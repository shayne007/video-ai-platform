package com.keensense.admin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel("添加结构化任务参数")
public class TaskParamBo implements Serializable {

    @ApiModelProperty("监控点Id")
    private String cameraFileId;
    @ApiModelProperty("来源")
    private String filefromtype = "1";
    @ApiModelProperty("是否自动分析 [1自动分析, 0不自动分析]")
    private String autoAnalysis;
    @ApiModelProperty("任务类型,objext:目标分类")
    private String type = "objext";
    @ApiModelProperty("任务参数")
    private String param;
    //  0 = 一般監控視頻場景, 1 = 卡口 / 類卡口場景
    private String scene;

    private String objMinSize;

    private String objMinTimeInMs;

    private String sensitivity;

    private String outputDSFactor;

    private String startIdx;

    private String endIdx;

    // 分析模式
    private String mode;

    // 目标类型
    private String targetTypes;

    // 参数类型 默认为0 自定义为1
    private String paramType;

    // 跳帧
    private String frameSkip;

    // 人脸质量阀值
    private String qualitythreshold;

    private  String paramTypeFace;

    // 0表示是不感兴趣区域  1 为感性趣
    private String  interest;

    // 兴趣区 节点字符串
    private String  featurFollowarea;
}
