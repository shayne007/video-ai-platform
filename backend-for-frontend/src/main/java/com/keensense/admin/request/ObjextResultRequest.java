package com.keensense.admin.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 14:42 2019/7/8
 * @Version v0.1
 */
@Data
public class ObjextResultRequest extends PageRequest {
    @NotBlank(message = "类型不能为空")
    String objtype;
    @NotBlank(message = "开始时间不能为空")
    String startTime;
    @NotBlank(message = "结束时间不能为空")
    String endTime;
}
