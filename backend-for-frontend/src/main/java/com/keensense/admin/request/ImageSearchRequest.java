package com.keensense.admin.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 20:38 2019/7/3
 * @Version v0.1
 */
@Data
public class ImageSearchRequest extends PageRequest {
    @NotBlank(message = "流水号不能为空")
    String serialnumber;
    String picture;
    String analysisTaskId;

    /**
     * 子任务号
     */
    String analysisId;
    /**
     * 图片uuid
     */
    String uuid;
}
