package com.keensense.admin.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:39 2019/6/12
 * @Version v0.1
 */
@Data
@ApiModel("修改密码参数")
public class ModifyPasswordRequest {
    @ApiModelProperty("旧密码")
    private String oldPassword;

    @NotBlank(message = "请输入确认密码")
    @ApiModelProperty("确认密码")
    private String confirmPassword;

    @ApiModelProperty("新密码")
    @NotBlank(message = "请输入新密码")
    private String newPassword;

    @ApiModelProperty("用户Id")
    private Long userId;
}
