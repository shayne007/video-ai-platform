package com.keensense.common.ratelimiter;

import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

/**
 * TODO
 *
 * @since 2025/7/9
 */
// Rate limit result wrapper
public class RateLimitResult {
    private final boolean allowed;
    private final int remainingRequests;
    private final Instant resetTime;
    private final long retryAfterSeconds;

    public RateLimitResult(boolean allowed, int remainingRequests,
                           Instant resetTime, long retryAfterSeconds) {
        this.allowed = allowed;
        this.remainingRequests = remainingRequests;
        this.resetTime = resetTime;
        this.retryAfterSeconds = retryAfterSeconds;
    }

    // Getters...
    public boolean isAllowed() { return allowed; }
    public int getRemainingRequests() { return remainingRequests; }
    public Instant getResetTime() { return resetTime; }
    public long getRetryAfterSeconds() { return retryAfterSeconds; }
}

