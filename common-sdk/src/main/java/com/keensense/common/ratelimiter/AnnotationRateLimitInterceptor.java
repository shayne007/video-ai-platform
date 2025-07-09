package com.keensense.common.ratelimiter;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO
 *
 * @since 2025/7/9
 */
@Component
public class AnnotationRateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Autowired
    private ApplicationContext applicationContext;

    // Map to cache key generators to avoid repeated lookups
    private final Map<String, RateLimitKeyGenerator> keyGeneratorCache = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            RateLimit rateLimit = method.getMethodAnnotation(RateLimit.class);

            if (rateLimit != null) {
                return processRateLimit(request, response, method, rateLimit);
            }
        }

        return true;
    }

    private boolean processRateLimit(HttpServletRequest request,
                                     HttpServletResponse response,
                                     HandlerMethod handlerMethod,
                                     RateLimit rateLimit) throws Exception {

        // 1. Parse the window duration
        Duration window = parseDuration(rateLimit.window());

        // 2. Generate the rate limit key
        String rateLimitKey = generateRateLimitKey(request, handlerMethod, rateLimit);

        // 3. Check rate limit
        RateLimitResult result = rateLimiterService.tryAcquire(
                rateLimitKey,
                rateLimit.limit(),
                window
        );

        // 4. Add rate limit headers
        addRateLimitHeaders(response, result, rateLimit.limit());

        // 5. Handle rate limit exceeded
        if (!result.isAllowed()) {
            handleRateLimitExceeded(response, result, rateLimit);
            return false;
        }

        return true;
    }

    private Duration parseDuration(String windowStr) {
        try {
            return Duration.parse(windowStr);
        } catch (DateTimeParseException e) {
            // Fallback to custom parsing for common formats
            return parseCustomDuration(windowStr);
        }
    }

    private Duration parseCustomDuration(String windowStr) {
        // Support formats like "1m", "30s", "1h"
        Pattern pattern = Pattern.compile("(\\d+)([smhd])");
        Matcher matcher = pattern.matcher(windowStr.toLowerCase());

        if (matcher.matches()) {
            long value = Long.parseLong(matcher.group(1));
            String unit = matcher.group(2);

//            return switch (unit) {
//                case "s" -> Duration.ofSeconds(value);
//                case "m" -> Duration.ofMinutes(value);
//                case "h" -> Duration.ofHours(value);
//                case "d" -> Duration.ofDays(value);
//                default -> Duration.ofMinutes(1); // default
//            };
        }

        return Duration.ofMinutes(1); // default fallback
    }

    private String generateRateLimitKey(HttpServletRequest request,
                                        HandlerMethod handlerMethod,
                                        RateLimit rateLimit) {

        RateLimitKeyGenerator keyGenerator = getKeyGenerator(rateLimit.keyGenerator());

        RateLimitContext context = RateLimitContext.builder()
                .request(request)
                .handlerMethod(handlerMethod)
                .annotation(rateLimit)
                .build();

        return keyGenerator.generateKey(context);
    }

    private RateLimitKeyGenerator getKeyGenerator(String keyGeneratorName) {
        return keyGeneratorCache.computeIfAbsent(keyGeneratorName, name -> {
            if ("default".equals(name)) {
                return new DefaultRateLimitKeyGenerator();
            }

            try {
                return applicationContext.getBean(name, RateLimitKeyGenerator.class);
            } catch (NoSuchBeanDefinitionException e) {
                // Fallback to default if custom generator not found
                return new DefaultRateLimitKeyGenerator();
            }
        });
    }

    private void addRateLimitHeaders(HttpServletResponse response,
                                     RateLimitResult result,
                                     int limit) {
        response.setHeader("X-RateLimit-Limit", String.valueOf(limit));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(result.getRemainingRequests()));
        response.setHeader("X-RateLimit-Reset", String.valueOf(result.getResetTime().getEpochSecond()));

        if (!result.isAllowed()) {
            response.setHeader("Retry-After", String.valueOf(result.getRetryAfterSeconds()));
        }
    }

    private void handleRateLimitExceeded(HttpServletResponse response,
                                         RateLimitResult result,
                                         RateLimit rateLimit) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json");

        String errorResponse = createErrorResponse(result, rateLimit);
        response.getWriter().write(errorResponse);
    }

    private String createErrorResponse(RateLimitResult result, RateLimit rateLimit) {
        return String.format("{\n" +
                        "                \"error\": \"Rate limit exceeded\",\n" +
                        "                \"message\": \"Too many requests. Limit: %d per %s\",\n" +
                        "                \"retryAfter\": %d,\n" +
                        "                \"resetTime\": \"%s\"\n" +
                        "            }",
                rateLimit.limit(),
                rateLimit.window(),
                result.getRetryAfterSeconds(),
                result.getResetTime().toString()
        );
    }
}