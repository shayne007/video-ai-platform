package com.keensense.common.ratelimiter;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

// Context for key generation
@Builder
@Getter
public class RateLimitContext {
    private final HttpServletRequest request;
    private final HandlerMethod handlerMethod;
    private final RateLimit annotation;

    // Getters...
}
