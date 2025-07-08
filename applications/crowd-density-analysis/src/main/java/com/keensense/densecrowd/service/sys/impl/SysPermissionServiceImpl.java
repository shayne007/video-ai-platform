package com.keensense.densecrowd.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.densecrowd.entity.sys.SysPermission;
import com.keensense.densecrowd.mapper.sys.SysPermissionMapper;
import com.keensense.densecrowd.service.sys.ISysPermissionService;
import org.springframework.stereotype.Service;


@Service("sysPermissionService")
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {
}
