package com.keensense.admin.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.sys.SysLog;
import com.keensense.admin.mapper.sys.SysLogMapper;
import com.keensense.admin.service.sys.ISysLogService;
import com.keensense.admin.vo.SysOperateLogVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Override
    public Page<SysLog> selectPageByParams(Page<SysLog> pages, Map<String, Object> params) {
        List<SysLog> records =  baseMapper.selectPageByParams(pages, params);
        pages.setRecords(records);
        return pages;
    }

    @Override
    public Page<SysOperateLogVo> queryUserPageByParams(Page<SysOperateLogVo> pages, Map<String, Object> params) {
        List<SysOperateLogVo> records =  baseMapper.queryUserPageByParams(pages, params);
        pages.setRecords(records);
        return pages;
    }

    @Override
    public Page<SysOperateLogVo> queryDeptPageByParams(Page<SysOperateLogVo> pages, Map<String, Object> params) {
        List<SysOperateLogVo> records =  baseMapper.queryDeptPageByParams(pages, params);
        pages.setRecords(records);
        return pages;
    }
}
