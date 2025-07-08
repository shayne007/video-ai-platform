package com.keensense.admin.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.sys.SysPermission;
import com.keensense.admin.mapper.sys.SysPermissionMapper;
import com.keensense.admin.service.sys.ISysPermissionService;
import org.springframework.stereotype.Service;



@Service("sysPermissionService")
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {
}
