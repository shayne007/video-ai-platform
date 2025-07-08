package com.keensense.densecrowd.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.densecrowd.entity.sys.SysRolePermission;
import com.keensense.densecrowd.mapper.sys.SysRolePermissionMapper;
import com.keensense.densecrowd.service.sys.ISysRolePermissionService;
import org.springframework.stereotype.Service;


@Service("sysRolePermissionService")
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {
}
