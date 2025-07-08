package com.keensense.admin.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.entity.sys.SysRole;
import com.keensense.admin.entity.sys.SysRolePermission;

import java.util.List;
import java.util.Set;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:12
 */
public interface ISysRoleService extends IService<SysRole> {

    Set<Long> getPermissionByRoleId(Long roleId);

    int update(SysRole model, List<SysRolePermission> rolePermissionList);

    void insertRolePermission(SysRole role);

    void deleteRolePermission(Long roleId);
}

