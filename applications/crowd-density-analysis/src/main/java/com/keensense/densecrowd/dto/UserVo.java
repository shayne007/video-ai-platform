package com.keensense.densecrowd.dto;

import lombok.Data;

@Data
public class UserVo {

    /**
     * 登陆用户id
     */
    private Long userId;

    /**
     * 登陆用户名
     */
    private String username;

    /**
     * 登陆用户密码
     */
    private String password;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 电话
     */
    private String tel;

    /**
     * 备足
     */
    private String remark;

    /**
     * 部门id
     */
    private Long deptId;


    /**
     * 是否禁用
     */
    private Integer isvalid;

    /**
     * 是否管理员
     */
    private Integer isDeptAdmin;

    /**
     * 主题id
     */
    private Integer themes;

    /**
     * 创建用户
     */
    private Long createUserId;

    /**
     * 创建用户名称
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private String createTime;
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 角色id
     */
    private String roleId;
    
    /**
     * 角色名称
     */
    private String roleName;
    
}
