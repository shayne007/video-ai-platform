package com.keensense.densecrowd.base;

import com.keensense.common.util.HttpContextUtils;
import com.keensense.densecrowd.entity.sys.SysUser;
import com.keensense.densecrowd.interceptor.AuthorizationInterceptor;
import com.keensense.densecrowd.service.sys.ISysUserService;
import com.keensense.densecrowd.util.CommonConstants;
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
