package com.keensense.densecrowd.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.common.util.RandomUtils;
import com.keensense.densecrowd.entity.sys.SysModule;
import com.keensense.densecrowd.entity.sys.SysRole;
import com.keensense.densecrowd.entity.sys.SysRolePermission;
import com.keensense.densecrowd.entity.sys.SysUserRole;
import com.keensense.densecrowd.mapper.sys.SysRoleMapper;
import com.keensense.densecrowd.mapper.sys.SysRolePermissionMapper;
import com.keensense.densecrowd.mapper.sys.SysUserRoleMapper;
import com.keensense.densecrowd.service.sys.ISysModuleService;
import com.keensense.densecrowd.service.sys.ISysRoleService;
import com.keensense.densecrowd.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Autowired
    private ISysModuleService sysModuleService;

    @Resource
    private SysRolePermissionMapper rolePermissionMapper;

    @Resource
    private SysUserRoleMapper userRoleMapper;

    @Override
    public Set<Long> getPermissionByRoleId(Long roleId) {
        Set<Long> currentp = new HashSet<>();
        List<SysModule> sysPermissions = sysModuleService.selectMenuByRoleId(roleId);
        for (SysModule perm : sysPermissions) {
            currentp.add(perm.getModuleId());
        }
        return currentp;
    }

    @Override
    public int update(SysRole model, List<SysRolePermission> rolePermissionList) {
        //角色权限关系，先删再插
        if (null != rolePermissionList && !rolePermissionList.isEmpty()) {
            rolePermissionMapper.delete(new QueryWrapper<SysRolePermission>().eq("role_id", model.getRoleId()));
            for (SysRolePermission rolePer : rolePermissionList) {
                rolePermissionMapper.insert(rolePer);
            }
        }
        return baseMapper.updateById(model);
    }

    @Override
    public void insertRolePermission(SysRole role) {
        baseMapper.insert(role);
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
        for (SysRolePermission rolePer : rolePermissionList) {
            rolePermissionMapper.insert(rolePer);
        }
    }

    @Override
    public void deleteRolePermission(Long roleId) {
        rolePermissionMapper.delete(new QueryWrapper<SysRolePermission>().eq("role_id", roleId));
        userRoleMapper.delete(new QueryWrapper<SysUserRole>().eq("role_id", roleId));
        baseMapper.deleteById(roleId);
    }
}
