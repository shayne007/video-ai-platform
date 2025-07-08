package com.keensense.admin.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.admin.annotation.Login;
import com.keensense.admin.base.BaseController;
import com.keensense.admin.entity.sys.SysModule;
import com.keensense.admin.entity.sys.SysUserRole;
import com.keensense.admin.service.sys.ISysModuleService;
import com.keensense.admin.service.sys.ISysUserRoleService;
import com.keensense.admin.util.StringUtils;
import com.keensense.common.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zengyc
 * @Description: 菜单
 * @Date: Created in 15:21 2019/6/11
 * @Version v0.1
 */
@RestController
@RequestMapping(value = "/user")
@Api(tags = "常规-用户菜单")
public class ModuleController extends BaseController {
    @Resource
    private ISysModuleService sysModuleService;

    @Resource
    private ISysUserRoleService sysUserRoleService;

    /**
     * 导航菜单
     */
    @Login
    @ApiOperation("导航菜单")
    @PostMapping("/nav")
    public R nav() {
        SysUserRole sysUserRole = sysUserRoleService.getOne(new QueryWrapper<SysUserRole>().eq("user_id", getUserId()));
        List<SysModule> menuList = sysModuleService.getMenuListByRoleId(sysUserRole.getRoleId());
        return R.ok().put("menuList", menuList);
    }

    @Login
    @ApiOperation("导航按钮")
    @PostMapping("/navButton")
    public R navButton() {
        SysUserRole sysUserRole = sysUserRoleService.getOne(new QueryWrapper<SysUserRole>().eq("user_id", getUserId()));
        List<SysModule> buttonList = sysModuleService.selectButtonByRoleId(sysUserRole.getRoleId());
        return R.ok().put("menuList", buttonList);
    }


    /**
     * 获取菜单
     */
    @ApiOperation("获取菜单管理列表")
    @PostMapping(value = "/getModuleList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "菜单父Id"),
            @ApiImplicitParam(name = "moduleName", value = "模块名")
    })
    public R getModuleList(Long parentId, String moduleName) {
        if (StringUtils.isNotEmptyString(moduleName)) {
            List<SysModule> moduleList = sysModuleService.queryListModuleName(moduleName);
            if (moduleList != null && !moduleList.isEmpty()) {
                for (SysModule sysModule : moduleList) {
                    sysModule.setClosed(false);
                    sysModule.setModuleLevel(1L);
                }
            }
            return R.ok().put("list", moduleList);
        }
        List<SysModule> mList = sysModuleService.queryListParentId(parentId, null);
        return R.ok().put("list", mList);
    }


    /**
     * 删除模块
     */
    @ApiOperation("删除菜单")
    @PostMapping(value = "/deleteModule")
    @ApiImplicitParam(name = "moduleId", value = "菜单Id")
    public R delModule(Long moduleId) {
        //判断是否有子菜单或按钮
        List<SysModule> menuList = sysModuleService.queryListParentId(moduleId, null);
        if (menuList != null && !menuList.isEmpty()) {
            return R.error("请先删除子菜单或按钮");
        }
        sysModuleService.deleteModule(moduleId);
        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation("修改菜单")
    @PostMapping("/updateModule")
    public R update(@RequestBody SysModule menu) {
        //数据校验
        sysModuleService.updateById(menu);
        return R.ok();
    }

    /**
     * 保存
     */
    @ApiOperation("保存菜单")
    @PostMapping("/save")
    public R save(@RequestBody SysModule menu) {
        //数据校验
        sysModuleService.save(menu);
        return R.ok();
    }
}
