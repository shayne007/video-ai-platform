package com.keensense.admin.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.sys.SysRolePermission;
import com.keensense.admin.mapper.sys.SysRolePermissionMapper;
import com.keensense.admin.service.sys.ISysRolePermissionService;
import org.springframework.stereotype.Service;



@Service("sysRolePermissionService")
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {
}
