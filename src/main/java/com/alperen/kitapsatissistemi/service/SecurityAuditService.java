package com.alperen.kitapsatissistemi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Güvenlik olaylarını loglama servisi
 */
@Service
public class SecurityAuditService {
    
    private static final Logger securityLogger = LoggerFactory.getLogger("SECURITY");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Başarılı login logla
     */
    public void logSuccessfulLogin(String email, String ipAddress, String userAgent) {
        String message = String.format(
            "[%s] SUCCESSFUL_LOGIN - Email: %s, IP: %s, UserAgent: %s",
            LocalDateTime.now().format(formatter),
            email,
            ipAddress,
            userAgent
        );
        securityLogger.info(message);
    }
    
    /**
     * Başarısız login logla
     */
    public void logFailedLogin(String email, String ipAddress, String userAgent, String reason) {
        String message = String.format(
            "[%s] FAILED_LOGIN - Email: %s, IP: %s, UserAgent: %s, Reason: %s",
            LocalDateTime.now().format(formatter),
            email,
            ipAddress,
            userAgent,
            reason
        );
        securityLogger.warn(message);
    }
    
    /**
     * Rate limiting ihlali logla
     */
    public void logRateLimitViolation(String ipAddress, String endpoint, String userAgent) {
        String message = String.format(
            "[%s] RATE_LIMIT_VIOLATION - IP: %s, Endpoint: %s, UserAgent: %s",
            LocalDateTime.now().format(formatter),
            ipAddress,
            endpoint,
            userAgent
        );
        securityLogger.warn(message);
    }
    
    /**
     * Şüpheli input logla
     */
    public void logSuspiciousInput(String ipAddress, String input, String endpoint, String userAgent) {
        String message = String.format(
            "[%s] SUSPICIOUS_INPUT - IP: %s, Input: %s, Endpoint: %s, UserAgent: %s",
            LocalDateTime.now().format(formatter),
            ipAddress,
            sanitizeForLog(input),
            endpoint,
            userAgent
        );
        securityLogger.warn(message);
    }
    
    /**
     * Yetkisiz erişim denemesi logla
     */
    public void logUnauthorizedAccess(String email, String ipAddress, String endpoint, String userAgent) {
        String message = String.format(
            "[%s] UNAUTHORIZED_ACCESS - Email: %s, IP: %s, Endpoint: %s, UserAgent: %s",
            LocalDateTime.now().format(formatter),
            email,
            ipAddress,
            endpoint,
            userAgent
        );
        securityLogger.warn(message);
    }
    
    /**
     * Kullanıcı kaydı logla
     */
    public void logUserRegistration(String email, String ipAddress, String userAgent) {
        String message = String.format(
            "[%s] USER_REGISTRATION - Email: %s, IP: %s, UserAgent: %s",
            LocalDateTime.now().format(formatter),
            email,
            ipAddress,
            userAgent
        );
        securityLogger.info(message);
    }
    
    /**
     * Şifre değişikliği logla
     */
    public void logPasswordChange(String email, String ipAddress, String userAgent) {
        String message = String.format(
            "[%s] PASSWORD_CHANGE - Email: %s, IP: %s, UserAgent: %s",
            LocalDateTime.now().format(formatter),
            email,
            ipAddress,
            userAgent
        );
        securityLogger.info(message);
    }
    
    /**
     * Logout logla
     */
    public void logLogout(String email, String ipAddress, String userAgent) {
        String message = String.format(
            "[%s] LOGOUT - Email: %s, IP: %s, UserAgent: %s",
            LocalDateTime.now().format(formatter),
            email,
            ipAddress,
            userAgent
        );
        securityLogger.info(message);
    }
    
    /**
     * HttpServletRequest'ten IP adresini al
     */
    public String getClientIpAddress(HttpServletRequest request) {
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
    
    /**
     * HttpServletRequest'ten User-Agent al
     */
    public String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null ? sanitizeForLog(userAgent) : "Unknown";
    }
    
    /**
     * Log için güvenli hale getir
     */
    private String sanitizeForLog(String input) {
        if (input == null) {
            return "null";
        }
        
        // Log injection saldırılarını önlemek için
        return input.replaceAll("[\r\n\t]", "_")
                   .replaceAll("[<>\"']", "")
                   .substring(0, Math.min(input.length(), 200)); // Maksimum 200 karakter
    }
}
