package com.keensense.densecrowd.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.keensense.densecrowd.util.RequestUtil;
import com.keensense.densecrowd.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

@Slf4j
@Component
@WebFilter(urlPatterns = "/*", filterName = "authFilter")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String method = req.getMethod();
        String path = req.getContextPath();
        String path1 = req.getServletPath();
        String ip = RequestUtil.getIpAddress(req);
        //后续可在此屏蔽ip地址
        log.info("method:" + method + path1);
        Enumeration headerNames = ((HttpServletRequest) request).getHeaderNames();
        while(headerNames.hasMoreElements()){
            String value = (String) headerNames.nextElement();
            log.info(value + ":" + req.getHeader(value));
        }
        Map map = req.getParameterMap();
        if (method.equals("GET")) {
            chain.doFilter(request, response);
            log.info(String.format("ip:%s,请求路径:%s,请求方法%s,请求参数：%s-%s", ip, path, method + path1, "", ""));
        } else {

            String msg = "";
            RequestWrapper wrapper = new RequestWrapper(req);
            if (map != null && StringUtils.isEmpty(wrapper.getBody())) {
                try {
                    msg = JSONObject.toJSONString(map);
                } catch (Exception e) {
                    log.error(e.getCause().toString());
                }
            }
            log.info(String.format("ip:%s,请求路径:%s,请求方法%s,请求参数：%s-%s", ip, path, method + path1, msg, wrapper.getBody()));
            chain.doFilter(wrapper, response);
        }

    }

    @Override
    public void destroy() {

    }
}

