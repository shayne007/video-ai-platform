package com.keensense.admin.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "用户参数")
@Data
public class SysUserRequest implements Serializable{

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "电话")
    private String tel;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "部门id")
    private String deptId;

    @ApiModelProperty(value = "状态")
    private String isvalid;

    @ApiModelProperty(value = "部门管理员")
    private String isDeptAdmin;

    @ApiModelProperty(value = "主题")
    private String themes;

    @ApiModelProperty(value = "创建用户id")
    private Long createUserId;

    @ApiModelProperty(value = "创建用户")
    private String createUserName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    @ApiModelProperty(value = "部门名字")
    private String deptName;
    /**
     * 自己新增
     */
    @ApiModelProperty(value = "角色id")
    private String roleId;

}