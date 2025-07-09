package com.keensense.common.ratelimiter;

// Key generator interface
public interface RateLimitKeyGenerator {
    String generateKey(RateLimitContext context);
}
