package com.keensense.search.interceptor;

import com.alibaba.fastjson.JSONObject;
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

    @Value("${filter.insert.limit}")
    private int permitsPerSecond;
    private static RateLimiter rateLimiter;

    @PostConstruct
    private void init() {
        rateLimiter = RateLimiter.create(permitsPerSecond);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        if (!isInsertRequest(url, method)) {
            return true;
        }
        if (!rateLimiter.tryAcquire()) {
            setLimitResponse(request, response);
            return false;
        }
        return true;
    }

    /**
     * 这里应该写在配置文件中
     *
     * @param url
     * @param method
     * @return
     */
    private boolean isInsertRequest(String url, String method) {
        boolean result =
                method.equalsIgnoreCase("POST") && url.contains("/VIID/MotorVehicles") || url
                        .contains("/VIID/NonMotorVehicles")
                        || url.contains("/VIID/Persons") || url.contains("/VIID/Faces") || url
                        .contains("/VIID/Summary")
                        || url.contains("/VIID/convert") || url.contains("/VIID/Violation") || url
                        .contains("/VIID/Vehicleflowrate") || url.contains("/VIID/CrowdDensity")
                        || url.contains("/VIID/Event");

        return result;
    }

    private void setLimitResponse(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(429);
        JSONObject result = ResponseUtil
                .createFailedResponse("", request.getRequestURL().toString(), 8,
                        "the request over the limit " + permitsPerSecond);
        log.error("request over the limit");
        try (PrintWriter out = response.getWriter()) {
            out.append(result.toJSONString());
        } catch (IOException e) {
            log.error("request over the limit");
        }
    }
}