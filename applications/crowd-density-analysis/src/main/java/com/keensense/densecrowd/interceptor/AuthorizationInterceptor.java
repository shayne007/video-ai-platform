package com.keensense.densecrowd.interceptor;


import com.keensense.common.exception.VideoException;
import com.keensense.densecrowd.annotation.Login;
import com.keensense.densecrowd.entity.sys.TokenEntity;
import com.keensense.densecrowd.service.sys.ITokenService;
import com.keensense.densecrowd.util.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限(Token)验证
 *
 * @author zengyc
 */
@Component
@Slf4j
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private ITokenService tokenService;

    public static final String USER_KEY = "userId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求地址
        Login annotation;
//        Request.setRequest(request);
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Login.class);
        } else {
            return true;
        }


        //从header中获取token
        String token = request.getHeader(CommonConstants.TOKEN);
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(CommonConstants.TOKEN);
        }

        if (annotation == null && StringUtils.isBlank(token)) {
            return true;
        }
        //token为空
        if (StringUtils.isBlank(token)) {
            throw new VideoException(401, "用户身份验证失败");
        }

        //查询token信息
        TokenEntity tokenEntity = tokenService.queryByToken(token);

        if (tokenEntity == null) {
            throw new VideoException(401, "登陆超时，请重新登录");
        }
        //超时重登陆
        if (tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
            throw new VideoException(401, "登陆超时，请重新登录");
        }

        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(USER_KEY, tokenEntity.getUserId());
        request.setAttribute(CommonConstants.TOKEN, token);
        return true;
    }
}
