package com.keensense.admin.service.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.entity.sys.SysUser;
import com.keensense.admin.request.LoginRequest;
import com.keensense.admin.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:12
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 用户登录
     * @param form    登录表单
     * @return        返回登录信息
     */
    Map<String, Object> login(LoginRequest form);

    void insertLog(String userName,String realName,String type);

    /**
     * 获取部门的id，用于佳都案件同步
     * @return
     */
    List<String> getAllDeptId();

    void saveUser(SysUser user);

    /**
     * 查询用户列表
     */
    Page<UserVo> selectSysUserList(Page<UserVo> page, SysUser user);

    /**
     * 根据id查用户
     * @param userId
     * @return
     */
    SysUser selectUserById(long userId);

    void updateUserRole(SysUser user);

    void deleteUserRole(Long userId);
}

