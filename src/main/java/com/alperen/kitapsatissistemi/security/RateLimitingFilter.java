package com.alperen.kitapsatissistemi.security;

import com.alperen.kitapsatissistemi.service.SettingsService;
import com.alperen.kitapsatissistemi.service.SecurityAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Rate Limiting Filter - Brute force saldırılarını önlemek için
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    
    @Autowired
    private SecurityAuditService securityAuditService;

    @Autowired
    private SettingsService settingsService;
    
    private static final int MAX_REQUESTS_PER_MINUTE = 1000;
    private static final int MAX_LOGIN_ATTEMPTS_PER_MINUTE = 20;
    private static final long WINDOW_SIZE_MILLIS = 60 * 1000; // 1 dakika
    
    private final ConcurrentHashMap<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, RequestCounter> loginAttempts = new ConcurrentHashMap<>();
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String clientIp = getClientIpAddress(request);
        String requestUri = request.getRequestURI();
        
        // Login endpoint'leri için özel rate limiting
        if (settingsService.isLoginRateLimitEnabled() && isLoginEndpoint(requestUri)) {
            if (!checkLoginRateLimit(clientIp)) {
                String userAgent = request.getHeader("User-Agent");
                securityAuditService.logRateLimitViolation(clientIp, requestUri, userAgent);
                response.setStatus(429); // Too Many Requests
                response.getWriter().write("{\"error\":\"Too many login attempts. Please try again later.\"}");
                return;
            }
        }

        // Register endpoint'leri için özel rate limiting
        if (settingsService.isRegisterRateLimitEnabled() && isRegisterEndpoint(requestUri)) {
            if (!checkLoginRateLimit(clientIp)) {
                String userAgent = request.getHeader("User-Agent");
                securityAuditService.logRateLimitViolation(clientIp, requestUri, userAgent);
                response.setStatus(429); // Too Many Requests
                response.getWriter().write("{\"error\":\"Too many register attempts. Please try again later.\"}");
                return;
            }
        }
        
        // Genel rate limiting
        if (!checkGeneralRateLimit(clientIp)) {
            String userAgent = request.getHeader("User-Agent");
            securityAuditService.logRateLimitViolation(clientIp, requestUri, userAgent);
            response.setStatus(429); // Too Many Requests
            response.getWriter().write("{\"error\":\"Too many requests. Please try again later.\"}");
            return;
        }
        
        filterChain.doFilter(request, response);
    }
    
    private boolean isLoginEndpoint(String uri) {
        return uri.contains("/login");
    }

    private boolean isRegisterEndpoint(String uri) {
        return uri.contains("/register");
    }
    
    private boolean checkLoginRateLimit(String clientIp) {
        return checkRateLimit(clientIp, loginAttempts, MAX_LOGIN_ATTEMPTS_PER_MINUTE);
    }
    
    private boolean checkGeneralRateLimit(String clientIp) {
        return checkRateLimit(clientIp, requestCounts, MAX_REQUESTS_PER_MINUTE);
    }
    
    private boolean checkRateLimit(String clientIp, ConcurrentHashMap<String, RequestCounter> counters, int maxRequests) {
        long currentTime = System.currentTimeMillis();
        
        RequestCounter counter = counters.computeIfAbsent(clientIp, k -> new RequestCounter());
        
        synchronized (counter) {
            // Zaman penceresi geçtiyse sayacı sıfırla
            if (currentTime - counter.windowStart.get() > WINDOW_SIZE_MILLIS) {
                counter.count.set(0);
                counter.windowStart.set(currentTime);
            }
            
            // İstek sayısını artır
            int currentCount = counter.count.incrementAndGet();
            
            return currentCount <= maxRequests;
        }
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
    
    private static class RequestCounter {
        private final AtomicInteger count = new AtomicInteger(0);
        private final AtomicLong windowStart = new AtomicLong(System.currentTimeMillis());
    }
}
