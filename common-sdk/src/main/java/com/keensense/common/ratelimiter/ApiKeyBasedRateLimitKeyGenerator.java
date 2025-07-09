package com.keensense.common.ratelimiter;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

// API Key based rate limiting
@Component("apiKeyBasedKeyGenerator")
public class ApiKeyBasedRateLimitKeyGenerator implements RateLimitKeyGenerator {

    @Override
    public String generateKey(RateLimitContext context) {
        HttpServletRequest request = context.getRequest();
        HandlerMethod method = context.getHandlerMethod();

        String apiKey = request.getHeader("X-API-Key");
        if (apiKey == null) {
            apiKey = request.getParameter("api_key");
        }

        return String.format("apikey:%s:endpoint:%s.%s",
                apiKey != null ? apiKey : "anonymous",
                method.getBeanType().getSimpleName(),
                method.getMethod().getName());
    }
}
