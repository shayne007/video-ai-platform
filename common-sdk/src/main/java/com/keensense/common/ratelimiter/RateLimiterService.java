package com.keensense.common.ratelimiter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * TODO
 *
 * @since 2025/7/9
 */
@Service
public class RateLimiterService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public RateLimitResult tryAcquire(String key, int limit, Duration window) {
        long windowSeconds = window.getSeconds();
        long now = System.currentTimeMillis() / 1000;
        long windowStart = now - windowSeconds;

        String script = "local key = KEYS[1]\n" +
                "            local window = tonumber(ARGV[1])\n" +
                "            local limit = tonumber(ARGV[2])\n" +
                "            local now = tonumber(ARGV[3])\n" +
                "            local windowStart = now - window\n" +
                "\n" +
                "            -- Remove old entries\n" +
                "            redis.call('ZREMRANGEBYSCORE', key, 0, windowStart)\n" +
                "\n" +
                "            -- Count current requests\n" +
                "            local current = redis.call('ZCARD', key)\n" +
                "\n" +
                "            if current < limit then\n" +
                "                -- Add current request\n" +
                "                redis.call('ZADD', key, now, now)\n" +
                "                redis.call('EXPIRE', key, window)\n" +
                "\n" +
                "                local remaining = limit - current - 1\n" +
                "                local resetTime = windowStart + window\n" +
                "\n" +
                "                return {1, remaining, resetTime, 0}\n" +
                "            else\n" +
                "                -- Rate limit exceeded\n" +
                "                local resetTime = windowStart + window\n" +
                "                local retryAfter = resetTime - now\n" +
                "\n" +
                "                return {0, 0, resetTime, retryAfter}\n" +
                "            end";

        @SuppressWarnings("unchecked")
        List<Long> result = (List<Long>) redisTemplate.execute(
                new DefaultRedisScript<>(script, List.class),
                Collections.singletonList(key),
                String.valueOf(windowSeconds),
                String.valueOf(limit),
                String.valueOf(now)
        );

        boolean allowed = result.get(0) == 1;
        int remaining = result.get(1).intValue();
        Instant resetTime = Instant.ofEpochSecond(result.get(2));
        long retryAfter = result.get(3);

        return new RateLimitResult(allowed, remaining, resetTime, retryAfter);
    }
}