package com.keensense.admin.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.sys.SysOperateLog;
import com.keensense.admin.mapper.sys.SysOperateLogMapper;
import com.keensense.admin.service.sys.ISysOperateLogService;
import org.springframework.stereotype.Service;


@Service("sysOperateLogService")
public class SysOperateLogServiceImpl extends ServiceImpl<SysOperateLogMapper, SysOperateLog> implements ISysOperateLogService {

}
