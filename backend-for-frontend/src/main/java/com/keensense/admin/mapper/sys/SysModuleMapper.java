package com.keensense.admin.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.admin.entity.sys.SysModule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Mapper
public interface SysModuleMapper extends BaseMapper<SysModule> {

    List<SysModule> selectMenuByRoleId(Long roleId);

    List<SysModule> selectMenuByRoleIdAndPid(@Param("roleId") Long roleId, @Param("parentId") Long parentId);

    List<SysModule> selectMenuByRoleIdAndModuleId(@Param("roleId") Long roleId, @Param("moduleId") Long moduleId);

    List<SysModule> selectButtonByRoleId(@Param("roleId") Long roleId);
}
