package com.keensense.densecrowd.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.densecrowd.dto.SysOperateLogVo;
import com.keensense.densecrowd.entity.sys.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {

    List<SysLog> selectPageByParams(Page<SysLog> pages, @Param("params") Map<String, Object> params);

    List<SysOperateLogVo> queryUserPageByParams(Page<SysOperateLogVo> pages, @Param("params") Map<String, Object> params);

    List<SysOperateLogVo> queryDeptPageByParams(Page<SysOperateLogVo> pages, @Param("params") Map<String, Object> params);
}
