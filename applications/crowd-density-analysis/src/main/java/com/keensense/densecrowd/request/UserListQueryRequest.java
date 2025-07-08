package com.keensense.densecrowd.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("用户列表查询参数")
public class UserListQueryRequest extends PageRequest implements Serializable {

    @ApiModelProperty("账号")
    private String username;

    @ApiModelProperty("姓名")
    private String realName;

}