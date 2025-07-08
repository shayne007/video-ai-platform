package com.keensense.admin.base;

import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.entity.sys.SysUser;
import com.keensense.admin.interceptor.AuthorizationInterceptor;
import com.keensense.admin.service.sys.ISysUserService;
import com.keensense.common.util.HttpContextUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: zengyc
 * @Description: Controller公共组件
 * @Date: Created in 17:00 2019/6/10
 * @Version v0.1
 */
public abstract class BaseController {
    @Autowired
    private ISysUserService sysUserService;

    protected SysUser getUser() {
        return sysUserService.getById(getUserId());
    }

    protected Long getUserId() {
        return (Long) HttpContextUtils.getHttpServletRequest().getAttribute(AuthorizationInterceptor.USER_KEY);
    }

    protected String getToken() {
        return (String) HttpContextUtils.getHttpServletRequest().getAttribute(CommonConstants.TOKEN);
    }
}
