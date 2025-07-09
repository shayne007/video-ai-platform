package com.keensense.common.ratelimiter;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * TODO
 *
 * @since 2025/7/9
 */
// User-based rate limiting
@Component("userBasedKeyGenerator")
public class UserBasedRateLimitKeyGenerator implements RateLimitKeyGenerator {

    @Override
    public String generateKey(RateLimitContext context) {
        HttpServletRequest request = context.getRequest();
        HandlerMethod method = context.getHandlerMethod();

        // Extract user ID from JWT token or session
        String userId = extractUserId(request);

        return String.format("user:%s:endpoint:%s.%s",
                userId,
                method.getBeanType().getSimpleName(),
                method.getMethod().getName());
    }

    private String extractUserId(HttpServletRequest request) {
        // Extract from JWT token
        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            // Parse JWT and extract user ID
//            return parseJwtUserId(authHeader.substring(7));
//        }

        // Fallback to session or IP
        return request.getRemoteAddr();
    }
}

