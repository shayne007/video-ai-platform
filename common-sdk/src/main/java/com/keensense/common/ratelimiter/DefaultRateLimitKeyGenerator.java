package com.keensense.common.ratelimiter;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

// Default key generator implementation
@Component
public class DefaultRateLimitKeyGenerator implements RateLimitKeyGenerator {

    @Override
    public String generateKey(RateLimitContext context) {
        HttpServletRequest request = context.getRequest();
        HandlerMethod method = context.getHandlerMethod();

        // Generate key based on: IP + HTTP Method + Endpoint
        StringBuilder keyBuilder = new StringBuilder();

        // Add client identifier (IP address)
        keyBuilder.append(getClientIpAddress(request));
        keyBuilder.append(":");

        // Add HTTP method
        keyBuilder.append(request.getMethod());
        keyBuilder.append(":");

        // Add controller and method name
        keyBuilder.append(method.getBeanType().getSimpleName());
        keyBuilder.append(".");
        keyBuilder.append(method.getMethod().getName());

        return keyBuilder.toString();
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
