package com.keensense.admin.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.sys.SysUserRole;
import com.keensense.admin.mapper.sys.SysUserRoleMapper;
import com.keensense.admin.service.sys.ISysUserRoleService;
import org.springframework.stereotype.Service;



@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {
}
