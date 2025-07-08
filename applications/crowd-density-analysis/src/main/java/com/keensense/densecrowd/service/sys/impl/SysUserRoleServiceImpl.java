package com.keensense.densecrowd.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.densecrowd.entity.sys.SysUserRole;
import com.keensense.densecrowd.mapper.sys.SysUserRoleMapper;
import com.keensense.densecrowd.service.sys.ISysUserRoleService;
import org.springframework.stereotype.Service;


@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {
}
