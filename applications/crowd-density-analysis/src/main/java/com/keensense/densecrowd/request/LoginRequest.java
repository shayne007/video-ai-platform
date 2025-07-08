package com.keensense.densecrowd.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录表单
 *
 * @author zengyc
 */
@Data
@ApiModel(value = "登录表单参数")
public class LoginRequest {
    @ApiModelProperty(value = "用户名")
    @NotBlank(message="用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码,MD5密文")
    @NotBlank(message="密码不能为空")
    private String password;
}
