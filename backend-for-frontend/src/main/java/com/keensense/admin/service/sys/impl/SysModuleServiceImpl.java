package com.keensense.admin.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.sys.SysModule;
import com.keensense.admin.entity.sys.SysRolePermission;
import com.keensense.admin.mapper.sys.SysModuleMapper;
import com.keensense.admin.service.sys.ISysModuleService;
import com.keensense.admin.service.sys.ISysRolePermissionService;
import com.keensense.admin.service.sys.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 15:43 2019/6/11
 * @Version v0.1
 */
@Service("sysModuleService")
public class SysModuleServiceImpl extends ServiceImpl<SysModuleMapper, SysModule> implements ISysModuleService {

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysRolePermissionService sysRolePermissionService;

    @Override
    public List<SysModule> getUserMenuList(Long roleId) {
        //用户菜单列表
        Set<Long> menuIdSet = roleService.getPermissionByRoleId(roleId);
        List<Long> menuIdList = new ArrayList<>(menuIdSet);
        //不以角色过滤菜单显示
        return getAllMenuList(0, menuIdList);
    }

    @Override
    public List<SysModule> selectMenuByRoleId(Long roleId) {
        return baseMapper.selectMenuByRoleId(roleId);
    }

    /**
     * 获取所有菜单列表
     */
    private List<SysModule> getAllMenuList(Integer roleType, List<Long> menuIdList) {
        //查询根菜单列表
        List<SysModule> menuList = queryListParentId(roleType, 0L, menuIdList);
        //递归获取子菜单

        getMenuTreeList(roleType, menuList, menuIdList);
        return menuList;
    }

    /**
     * 递归
     */
    private List<SysModule> getMenuTreeList(Integer roleType, List<SysModule> menuList, List<Long> menuIdList) {
        List<SysModule> subMenuList = new ArrayList<>();

        for (SysModule entity : menuList) {
            //目录
            entity.setChildren(getMenuTreeList(roleType, queryListParentId(roleType, entity.getModuleId(), menuIdList), menuIdList));

            subMenuList.add(entity);
        }

        return subMenuList;
    }

    /**
     * 获取用户列表
     *
     * @param parentId
     * @param menuIdList
     * @return
     */
    public List<SysModule> queryListParentId(Integer roleType, Long parentId, List<Long> menuIdList) {
        List<SysModule> menuList = queryListParentId(parentId, 1);
        List<SysModule> userMenuList = new ArrayList<>();
        if (menuIdList == null) {
            return userMenuList;
        }

        if (roleType != null && roleType == 0) {
            for (SysModule menu : menuList) {
                if (menuIdList.contains(menu.getModuleId())) {
                    menu.setActions("0");
                    menu.setDisplay(true);
                }
            }
            return menuList;
        }

        for (SysModule menu : menuList) {
            if (menuIdList.contains(menu.getModuleId())) {
                menu.setActions("0");
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    /**
     * 获取子节点数据
     *
     * @param parentId
     * @return
     */
    @Override
    public List<SysModule> queryListParentId(Long parentId, Integer state) {
        List<SysModule> mList = baseMapper.selectList(new QueryWrapper<SysModule>().eq("parent_id", parentId == null ? 0 :
                parentId).eq(state != null, "state", state).orderByAsc("state").orderByAsc("display_order", "module_id"));
        for (SysModule entity : mList) {
            int num = baseMapper.selectCount(new QueryWrapper<SysModule>().eq("parent_id", entity.getModuleId()));
            if (num > 0) {
                entity.setClosed(true);
            } else {
                entity.setClosed(false);
            }
        }
        return mList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteModule(Long menuId) {
        //删除菜单
        baseMapper.deleteById(menuId);
        //删除菜单与角色关联
        // 删除菜单与角色关联
        sysRolePermissionService.remove(new QueryWrapper<SysRolePermission>().eq(
                "permission_id", menuId));
    }

    @Override
    public List<SysModule> getMenuListByRoleId(Long roleId) {
        //查询根菜单列表
        List<SysModule> menuList = baseMapper.selectMenuByRoleIdAndPid(roleId, 0L);
        //递归获取子菜单
        getChildMenuList(menuList, roleId);
        return menuList;
    }

    @Override
    public List<SysModule> queryListModuleName(String moduleName) {
        return baseMapper.selectList(new QueryWrapper<SysModule>().like("module_name", moduleName));
    }

    /**
     * 递归
     */
    private List<SysModule> getChildMenuList(List<SysModule> menuList, Long roleId) {
        if (menuList != null && !menuList.isEmpty()) {
            for (SysModule entity : menuList) {
                entity.setChildren(getChildMenuList(baseMapper.selectMenuByRoleIdAndModuleId(roleId, entity.getModuleId()), roleId));
                if (null == entity.getChildren()) {
                    entity.setClosed(false);
                } else {
                    entity.setClosed(true);
                }
            }
        }
        return menuList;
    }

    @Override
    public List<SysModule> selectButtonByRoleId(Long roleId) {
        return baseMapper.selectButtonByRoleId(roleId);
    }
}
