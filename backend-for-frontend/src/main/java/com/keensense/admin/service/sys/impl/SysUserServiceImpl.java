package com.keensense.admin.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.sys.SysLog;
import com.keensense.admin.entity.sys.SysOperateLog;
import com.keensense.admin.entity.sys.SysRole;
import com.keensense.admin.entity.sys.SysUser;
import com.keensense.admin.entity.sys.SysUserRole;
import com.keensense.admin.entity.sys.TokenEntity;
import com.keensense.admin.mapper.sys.SysLogMapper;
import com.keensense.admin.mapper.sys.SysOperateLogMapper;
import com.keensense.admin.mapper.sys.SysRoleMapper;
import com.keensense.admin.mapper.sys.SysUserMapper;
import com.keensense.admin.mapper.sys.SysUserRoleMapper;
import com.keensense.admin.request.LoginRequest;
import com.keensense.admin.service.sys.ISysUserService;
import com.keensense.admin.service.sys.ITokenService;
import com.keensense.admin.util.DateTimeUtils;
import com.keensense.admin.util.DbPropUtil;
import com.keensense.admin.util.IpUtils;
import com.keensense.admin.util.RandomUtils;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.util.ValidateHelper;
import com.keensense.admin.vo.UserVo;
import com.keensense.common.exception.VideoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private ITokenService tokenService;

    @Resource
    private SysUserRoleMapper userRoleMapper;

    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysLogMapper sysLogMapper;

    @Resource
    private SysOperateLogMapper sysOperateLogMapper;

    @Override
    @Transactional
    public Map<String, Object> login(LoginRequest form) {
        SysUser user = queryByUsername(form.getUsername());
        //密码错误
        if (user == null || !user.getPassword().equals(form.getPassword())) {
            throw new VideoException("用户名或密码错误");
        }
        //获取登录token
        TokenEntity tokenEntity = tokenService.queryByUserId(user.getUserId());
        String sysLogin = DbPropUtil.getString("sys_login", "0");
        if ("1".equals(sysLogin) || tokenEntity == null) {
            tokenEntity = tokenService.createToken(user.getUserId());
        }
        if (tokenEntity != null) {
            //超时重登陆
            if (tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
                tokenEntity = tokenService.createToken(user.getUserId());
            }
        }

        insertLog(user.getUsername(), user.getRealName(), "1");
        try {
            recordUserOperateLog(user);
        } catch (Exception e) {
            log.info("重复登录");
        }
        Map<String, Object> map = new HashMap<>(3);
        Map<String, Object> userinfo = new HashMap<>();
        map.put("token", tokenEntity.getToken());
        map.put("expire", tokenEntity.getExpireTime().getTime() - System.currentTimeMillis());
        userinfo.put("userId", tokenEntity.getUserId());
        userinfo.put("realName", user.getRealName());
        userinfo.put("userName", user.getUsername());
        userinfo.put("asdtest", user.getPassword());
        map.put("userinfo", userinfo);
        return map;
    }

    public void recordUserOperateLog(SysUser user) {
        if (user == null) {
            return;
        }
        String ipAddr = IpUtils.getRealIpAddr();
        Long userId = user.getUserId();
        String date = DateTimeUtils.formatDate(new Date(), DateTimeUtils.DEFAULT_FORMAT_DATE_WITHOUT_TIME);
        SysOperateLog sysOperateLog = sysOperateLogMapper.selectOne(new QueryWrapper<SysOperateLog>().eq("user_id", userId).likeRight("create_time", date));

        if (sysOperateLog != null) {
            sysOperateLog.setOperateIp(ipAddr);
            sysOperateLog.setVisitNum(sysOperateLog.getVisitNum() + 1);
            sysOperateLogMapper.updateById(sysOperateLog);
        } else {
            SysOperateLog operateLog = new SysOperateLog();
            String deptId = user.getDeptId();
            if (StringUtils.isEmpty(deptId)) {
                deptId = "0";
            }
            operateLog.setUserId(user.getUserId());
            operateLog.setDeptId(Long.parseLong(deptId));
            Date createTime = DateTimeUtils.now();
            operateLog.setCreateTime(createTime);
            operateLog.setUpdateTime(createTime);
            operateLog.setVisitNum(1);
            operateLog.setOperateIp(ipAddr);
            operateLog.setOperateModule("/u2s/rest/user/login");
            sysOperateLogMapper.insert(operateLog);
        }
    }

    @Override
    public void insertLog(String userName, String realName, String type) {
        SysLog sysLog = new SysLog();
        if ("1".equals(type)) {
            sysLog.setModuleUrl("/u2s/rest/user/login");
            sysLog.setModuleName("用户登录");
            sysLog.setActionType(5);
        } else {
            sysLog.setModuleUrl("/u2s/rest/user/logout");
            sysLog.setModuleName("用户退出");
            sysLog.setActionType(6);
        }
        sysLog.setUserName(userName);
        sysLog.setRealName(realName);
        sysLog.setCreateTime(DateTimeUtils.now());
        sysLogMapper.insert(sysLog);
    }

    public SysUser queryByUsername(String username) {
        return baseMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    @Override
    public List<String> getAllDeptId() {
        return baseMapper.getAllDeptId();
    }

    /**
     * 保存用户
     *
     * @param user
     * @return
     */
    @Override
    @Transactional
    public void saveUser(SysUser user) {
        baseMapper.insert(user);
        List<SysUserRole> userRoleList = setSysUserRoles(user);
        // 增加多条角色
        if (ValidateHelper.isNotEmptyList(userRoleList)) {
            for (SysUserRole sysUserRole : userRoleList) {
                userRoleMapper.insert(sysUserRole);
            }
        }
    }

    private List<SysUserRole> setSysUserRoles(SysUser sysuser) {
        List<SysUserRole> userRoleList = new ArrayList<>();
        if (StringUtils.isNotEmptyString(sysuser.getRoleId())) {
            String[] roles = sysuser.getRoleId().split(",");
            for (int i = 0; i < roles.length; i++) {
                SysUserRole userRole = new SysUserRole();
                Long userRoleId = Long.valueOf(RandomUtils.get8RandomValiteCode(8));
                userRole.setUserRoleId(userRoleId);
                userRole.setUserId(sysuser.getUserId());
                userRole.setRoleId(Long.parseLong(roles[i]));
                userRoleList.add(userRole);
            }
        }
        return userRoleList;
    }

    @Override
    public Page<UserVo> selectSysUserList(Page<UserVo> page, SysUser user) {
        int count = 0;
        //查询个数
        count = baseMapper.selectCount(new QueryWrapper<>());
        //个数大于0
        if (count > 0) {
            List<UserVo> sysUserList = baseMapper.selectUsersByPage(page, user);
            // 根据uerID查询角色信息
            setRoleInfo(sysUserList);
            page.setRecords(sysUserList);
            return page;
        } else {
            return null;
        }

    }

    private void setRoleInfo(List<UserVo> sysUserList) {
        if (ValidateHelper.isNotEmptyList(sysUserList)) {
            for (UserVo userVo : sysUserList) {
                List<SysUserRole> sysUserRoleList = userRoleMapper.selectList(new QueryWrapper<SysUserRole>().eq("user_id", userVo.getUserId()));
                if (ValidateHelper.isNotEmptyList(sysUserRoleList)) {
                    StringBuilder roleStrbuf = new StringBuilder();
                    StringBuilder roleIdStrbuf = new StringBuilder();
                    for (SysUserRole sysUserRole : sysUserRoleList) {
                        SysRole role = roleMapper.selectById(sysUserRole.getRoleId());
                        if (null != role) {
                            roleStrbuf.append(role.getRoleName()).append(",");
                            roleIdStrbuf.append(role.getRoleId()).append(",");
                        }
                    }
                    if (roleStrbuf.toString().length() >= 1) {
                        String roleStr = roleStrbuf.toString().substring(0, roleStrbuf.toString().length() - 1);
                        userVo.setRoleName(roleStr);
                        String roleId = roleIdStrbuf.toString().substring(0, roleIdStrbuf.toString().length() - 1);
                        userVo.setRoleId(roleId);
                    }
                }
            }
        }
    }

    @Override
    public SysUser selectUserById(long userId) {
        return sysUserMapper.selectUserById(userId);
    }

    @Override
    public void updateUserRole(SysUser user) {
        userRoleMapper.delete(new QueryWrapper<SysUserRole>().eq("user_id", user.getUserId()));
        // 增加新的sys_user_role记录
        List<SysUserRole> userRoleList = new ArrayList<>();
        if (StringUtils.isNotEmptyString(user.getRoleId())) {
            String[] roles = user.getRoleId().split(",");

            for (int i = 0; i < roles.length; i++) {
                SysUserRole userRole = new SysUserRole();
                Long userRoleId = Long.valueOf(RandomUtils.get8RandomValiteCode(8));
                userRole.setUserRoleId(userRoleId);
                userRole.setUserId(user.getUserId());
                userRole.setRoleId(Long.parseLong(roles[i]));
                userRoleList.add(userRole);
            }
        }
        for (SysUserRole sysUserRole : userRoleList) {
            userRoleMapper.insert(sysUserRole);
        }
        baseMapper.updateById(user);
    }

    @Override
    public void deleteUserRole(Long userId) {
        userRoleMapper.delete(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        baseMapper.deleteById(userId);
    }
}
