package com.keensense.admin.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.entity.sys.SysUser;
import com.keensense.admin.vo.UserVo;
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
public interface SysUserMapper extends BaseMapper<SysUser> {


    /**
     * 获取所有的部门的Id
     * @return
     */
    List<String> getAllDeptId();

    /**
     * 查询用户列表
     */
    public List<UserVo> selectUsersByPage(Page<UserVo> page, @Param("record") SysUser record);

    /**
     * 通过id查询用户
     * @param userId
     * @return
     */
    SysUser selectUserById(long userId);


}
