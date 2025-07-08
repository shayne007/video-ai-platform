package com.keensense.admin.service.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.entity.sys.SysLog;
import com.keensense.admin.vo.SysOperateLogVo;

import java.util.Map;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:12
 */
public interface ISysLogService extends IService<SysLog> {

    Page<SysLog> selectPageByParams(Page<SysLog> pages, Map<String,Object> params);

    Page<SysOperateLogVo> queryUserPageByParams(Page<SysOperateLogVo> page, Map<String,Object> params);

    Page<SysOperateLogVo> queryDeptPageByParams(Page<SysOperateLogVo> page, Map<String,Object> params);
}

