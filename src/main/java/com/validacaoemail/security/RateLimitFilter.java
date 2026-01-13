package com.validacaoemail.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(1)
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int RATE_LIMIT_REQUESTS = 60;
    private static final Duration RATE_LIMIT_DURATION = Duration.ofMinutes(1);
    
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/email/verify")) {
            String clientId = getClientId(request);
            Bucket bucket = resolveBucket(clientId);

            if (!bucket.tryConsume(1)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Rate limit exceeded\",\"message\":\"Too many requests\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private Bucket resolveBucket(String clientId) {
        return cache.computeIfAbsent(clientId, this::createBucket);
    }

    private Bucket createBucket(String key) {
        Refill refill = Refill.intervally(RATE_LIMIT_REQUESTS, RATE_LIMIT_DURATION);
        Bandwidth limit = Bandwidth.classic(RATE_LIMIT_REQUESTS, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    private String getClientId(HttpServletRequest request) {
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
