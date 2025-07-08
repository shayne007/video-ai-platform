package com.keensense.densecrowd.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.common.util.DateTime;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.keensense.common.validator.ValidatorUtils;
import com.keensense.densecrowd.annotation.Login;
import com.keensense.densecrowd.base.BaseController;
import com.keensense.densecrowd.dto.UserVo;
import com.keensense.densecrowd.entity.sys.SysRole;
import com.keensense.densecrowd.entity.sys.SysUser;
import com.keensense.densecrowd.request.LoginRequest;
import com.keensense.densecrowd.request.ModifyPasswordRequest;
import com.keensense.densecrowd.request.SysUserRequest;
import com.keensense.densecrowd.request.UserListQueryRequest;
import com.keensense.densecrowd.service.sys.ISysRoleService;
import com.keensense.densecrowd.service.sys.ISysUserService;
import com.keensense.densecrowd.service.sys.ITokenService;
import com.keensense.densecrowd.util.EntityObjectConverter;
import com.keensense.densecrowd.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping(value = "/user")
@Api(tags = "常规-登录/注销")
public class UserController extends BaseController {
    @Resource
    private ISysUserService userService;

    @Resource
    private ISysRoleService roleService;

    @Resource
    private ITokenService tokenService;

    /**
     * 用户登录
     *
     * @return
     * @throws IOException
     * @author YangXQ
     * @since 2016/1/11/11:00
     */
    @ApiOperation("登录")
    @PostMapping(value = "/login")
    public R login(@RequestBody LoginRequest loginForm) {
        ValidatorUtils.validateEntity(loginForm);
        //用户登录
        Map<String, Object> map = userService.login(loginForm);
        return R.ok(map);
    }


    @PostMapping("logout")
    @ApiOperation("注销")
    public R logout(Long userId) {
        tokenService.expireToken(userId);
        SysUser user = userService.selectUserById(userId);
        if (user != null) {
            userService.insertLog(user.getUsername(), user.getRealName(), "2");
        }
        return R.ok();
    }

    /**
     * 保存新密码
     */
    @Login
    @PostMapping(value = "/saveNewPwd")
    @ApiOperation("修改密码")
    public R saveNewPwd(@RequestBody ModifyPasswordRequest modifyPassword) {
        SysUser sysUser = getUser();
        if (modifyPassword.getOldPassword().equals(modifyPassword.getNewPassword())) {
            return R.error("新密码不能与旧密码一致");
        }
        if (!modifyPassword.getNewPassword().equals(modifyPassword.getConfirmPassword())) {
            return R.error("新密码与确认密码不一致");
        }
        if (!sysUser.getPassword().equals(modifyPassword.getOldPassword())) {
            return R.error("旧密码不正确");
        } else {
            sysUser.setPassword(modifyPassword.getNewPassword());
            boolean save = userService.updateById(sysUser);
            if (save) {
                return R.ok("修改成功");
            } else {
                return R.error("修改失败");
            }
        }
    }

    @PostMapping(value = "/updateUserPwd")
    @ApiOperation("修改其他用户密码")
    public R updateUserPwd(@RequestBody ModifyPasswordRequest modifyPassword) {
        Long userId = modifyPassword.getUserId();
        SysUser sysUser = userService.getById(userId);
        if (!modifyPassword.getNewPassword().equals(modifyPassword.getConfirmPassword())) {
            return R.error("新密码与确认密码不一致");
        }
        sysUser.setPassword(modifyPassword.getNewPassword());
        boolean save = userService.updateById(sysUser);
        if (save) {
            return R.ok("修改成功");
        } else {
            return R.error("修改失败");
        }
    }


    @ApiOperation(value = "新增用户")
    @PostMapping(value = "/createUser")
    public R createUser(@RequestBody SysUserRequest sysUser) {
        if (StringUtils.isEmpty(sysUser.getUsername())) {
            return R.error("用户名不能为空");
        }
        // 校验用户是否存在
        SysUser hasUser = userService.getOne(new QueryWrapper<SysUser>().eq("username", sysUser.getUsername()));
        if (hasUser != null) {
            return R.error("用户已经存在");
        }
        // 设置部门管理员标识
        setDeptAdmin(sysUser);
        SysUser user = EntityObjectConverter.getObject(sysUser, SysUser.class);
        user.setCreateUserId(getUser().getUserId());
        user.setCreateUserName(getUser().getUsername());
        user.setCreateTime(DateTime.getCurrentTime());
        user.setIsvalid("1");
        userService.saveUser(user);
        return R.ok();
    }

    private void setDeptAdmin(SysUserRequest sysuser) {
        if (StringUtils.isNotEmptyString(sysuser.getRoleId())) {
            String[] roles = sysuser.getRoleId().split(",");

            StringBuilder roleType = new StringBuilder();
            for (int i = 0; i < roles.length; i++) {
                SysRole sysRole = roleService.getById(new Long(roles[i]));
                if (null != sysRole) {
                    roleType.append(sysRole.getRoleSign()).append(";");
                }
            }
            String roleTypeStr = roleType.toString();

            //系统管理员
            if (roleTypeStr.contains("admin")) {
                sysuser.setIsDeptAdmin("1");
            }
            // 设置部门管理员
            else if (roleTypeStr.contains("manger")) {
                sysuser.setIsDeptAdmin("2");
            } else {// 普通操作员
                sysuser.setIsDeptAdmin("0");
            }
        }
    }

    @ApiOperation(value = "查询用户列表")
    @PostMapping(value = "/queryUserList")
    public R queryUsers(@RequestBody UserListQueryRequest userListQuery) {
        R result = R.ok();
        try {
            int page = userListQuery.getPage();
            int rows = userListQuery.getRows();
            String username = userListQuery.getUsername();
            String realName = userListQuery.getRealName();
            SysUser user = new SysUser();
            if (StringUtils.isNotEmptyString(username)) {
                user.setUsername("%" + username + "%");
            }
            if (StringUtils.isNotEmptyString(realName)) {
                user.setRealName("%" + realName + "%");
            }
            Long currentUserId = getUserId();
            if (null != currentUserId && currentUserId != 1) {
                user.setCreateUserId(currentUserId);
            }
            Page<UserVo> pages = new Page<>(page, rows);
            Page<UserVo> pageResult = userService.selectSysUserList(pages, user);
            result.put("page", new PageUtils(pageResult));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "修改用户信息")
    @PostMapping(value = "/updateUser")
    public R updateUser(@RequestBody SysUserRequest sysUser) {
        if (StringUtils.isEmpty(sysUser.getUsername())) {
            return R.error("用户名不能为空");
        }
        String userName = sysUser.getUsername();
        SysUser hasUser = userService.getOne(new QueryWrapper<SysUser>().eq("username", userName).notIn("user_id", sysUser.getUserId()));
        if (hasUser != null) {
            return R.error("用户名已经存在");
        }
        SysUser user = EntityObjectConverter.getObject(sysUser, SysUser.class);
        userService.updateUserRole(user);
        return R.ok();
    }

    @ApiOperation(value = "删除用户信息")
    @PostMapping(value = "/deleteUser")
    public R delete(Long userId) {
        userService.deleteUserRole(userId);
        return R.ok();
    }
}
