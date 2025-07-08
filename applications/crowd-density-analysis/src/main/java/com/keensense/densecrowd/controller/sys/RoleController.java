package com.keensense.densecrowd.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.keensense.common.util.RandomUtils;
import com.keensense.densecrowd.base.BaseController;
import com.keensense.densecrowd.entity.sys.SysModule;
import com.keensense.densecrowd.entity.sys.SysRole;
import com.keensense.densecrowd.entity.sys.SysRolePermission;
import com.keensense.densecrowd.entity.sys.SysUser;
import com.keensense.densecrowd.service.sys.ISysModuleService;
import com.keensense.densecrowd.service.sys.ISysRoleService;
import com.keensense.densecrowd.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(tags = "角色管理")
@Slf4j
@RestController
@RequestMapping("/role")
@ApiIgnore
public class RoleController extends BaseController {

    @Resource
    private ISysRoleService roleService;

    @Resource
    private ISysModuleService sysModuleService;

    @ApiOperation("查询角色列表")
    @PostMapping("queryRoleList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码"),
            @ApiImplicitParam(name = "rows", value = "每页显示条数")
    })
    public R queryRoleList(int page, int rows) {
        R result = R.ok();
        Page<SysRole> pages = new Page<>(page, rows);
        try {
            roleService.page(pages);
        } catch (Exception e) {
            log.error(e.getMessage());
            return R.error(e.getMessage());
        }
        return result.put("page", new PageUtils(pages));
    }

    @ApiOperation("获取角色菜单列表")
    @PostMapping(value = "/getRoleMenuList")
    @ApiImplicitParam(name = "roleId", value = "角色Id")
    public R openRoleUpdate(Long roleId) {
        R result = R.ok();
        try {
            List<SysModule> menuList = sysModuleService.getUserMenuList(roleId);
            result.put("menuList", menuList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @ApiOperation("更新角色")
    @PostMapping(value = "/updateRole")
    public R updateRole(@RequestBody SysRole role) {
        R result = R.ok();
        String permissionIds = role.getPermissionIds();
        List<SysRolePermission> rolePermissionList = new ArrayList<>();
        if (StringUtils.isNotEmptyString(permissionIds)) {
            String[] permissionIdArr = permissionIds.split(",");
            for (String str : permissionIdArr) {
                SysRolePermission rolePerm = new SysRolePermission();
                Long rolePermissionId = Long.valueOf(RandomUtils.get8RandomValiteCode(8));
                rolePerm.setRoleId(role.getRoleId());
                rolePerm.setRolePermissionId(rolePermissionId);
                rolePerm.setPermissionId(Long.parseLong(str));
                rolePermissionList.add(rolePerm);
            }
        }
        SysRole sysRole = roleService.getOne(new QueryWrapper<SysRole>().eq("role_name", role.getRoleName()).notIn("role_id", role.getRoleId()));
        if (sysRole != null) {
            return R.error("角色名称已存在请重新输入");
        }
        roleService.update(role, rolePermissionList);
        return result;
    }

    @ApiOperation("新增角色")
    @PostMapping(value = "/addRole")
    public R addRole(@RequestBody SysRole role) {
        R result = R.ok();
        SysRole sysRole = roleService.getOne(new QueryWrapper<SysRole>().eq("role_name", role.getRoleName()));
        if (sysRole != null) {
            return R.error("角色名称已存在请重新输入");
        }
        SysUser user = getUser();
        role.setCreateTime(new Date());
        role.setCreateUserId(user.getUserId());
        role.setCreateUserName(user.getUsername());
        roleService.insertRolePermission(role);
        return result;
    }

    @ApiOperation(value = "删除角色")
    @PostMapping(value = "/deleteRole")
    public R delete(Long userId) {
        roleService.deleteRolePermission(userId);
        return R.ok();
    }
}
