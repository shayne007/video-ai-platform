package com.keensense.image.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.keensense.common.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by zhanx xiaohui on 2019-10-31.
 */
@Component
@EnableScheduling
@Slf4j
public class QpsInterceptor extends HandlerInterceptorAdapter {

    @Value("${filter.image.limit}")
    private int permitsPerSecond;
    private RateLimiter rateLimiter;

    @PostConstruct
    private void init() {
        rateLimiter = RateLimiter.create(permitsPerSecond);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        boolean notPassed = isInsertImageRequest(url, method) && !rateLimiter.tryAcquire();
        if (notPassed) {
            setLimitResponse(request, response);
            return false;
        }
        return true;
    }

    private boolean isInsertImageRequest(String url, String method) {
        return method.equalsIgnoreCase("POST") && url.contains("/VIID/Image");
    }

    private void setLimitResponse(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(429);
        String result = ResponseUtil.createFailedResponse("", request.getRequestURL().toString(), 8,
                "the request over the limit " + permitsPerSecond).toJSONString();
        log.error("request over the limit");
        try (PrintWriter out = response.getWriter()) {
            out.append(result);
        } catch (IOException e) {
            log.error("request over the limit");
        }
    }
}