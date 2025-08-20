package com.alperen.kitapsatissistemi.util;

import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.util.regex.Pattern;

/**
 * Input Sanitizer - XSS ve SQL Injection saldırılarını önlemek için
 */
@Component
public class InputSanitizer {
    
    // SQL Injection için tehlikeli karakterler ve kelimeler
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
        "(?i).*(union|select|insert|update|delete|drop|create|alter|exec|execute|script|javascript|vbscript|onload|onerror|onclick).*"
    );
    
    // XSS için tehlikeli karakterler
    private static final Pattern XSS_PATTERN = Pattern.compile(
        "(?i).*(<script|</script|javascript:|vbscript:|onload=|onerror=|onclick=|onmouseover=|onfocus=|onblur=).*"
    );
    
    /**
     * Genel input sanitization
     */
    public String sanitizeInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        
        // HTML encode
        String sanitized = HtmlUtils.htmlEscape(input);
        
        // Tehlikeli karakterleri temizle
        sanitized = sanitized.replaceAll("[<>\"'%;()&+]", "");
        
        return sanitized.trim();
    }
    
    /**
     * SQL Injection kontrolü
     */
    public boolean containsSqlInjection(String input) {
        if (input == null) {
            return false;
        }
        return SQL_INJECTION_PATTERN.matcher(input).matches();
    }
    
    /**
     * XSS kontrolü
     */
    public boolean containsXss(String input) {
        if (input == null) {
            return false;
        }
        return XSS_PATTERN.matcher(input).matches();
    }
    
    /**
     * Güvenli input kontrolü
     */
    public boolean isSafeInput(String input) {
        return !containsSqlInjection(input) && !containsXss(input);
    }
    
    /**
     * Email sanitization
     */
    public String sanitizeEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return email;
        }
        
        // Email için sadece gerekli karakterleri bırak
        return email.replaceAll("[^a-zA-Z0-9@._\\-]", "").toLowerCase().trim();
    }
    
    /**
     * Telefon numarası sanitization
     */
    public String sanitizePhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return phone;
        }
        
        // Sadece rakam, +, -, (, ), boşluk karakterlerini bırak
        return phone.replaceAll("[^0-9+\\-() ]", "").trim();
    }
    
    /**
     * Alfanumerik sanitization
     */
    public String sanitizeAlphanumeric(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        
        // Sadece harf, rakam ve boşluk bırak
        return input.replaceAll("[^a-zA-Z0-9çğıöşüÇĞIİÖŞÜ ]", "").trim();
    }
    
    /**
     * Fiyat sanitization
     */
    public String sanitizePrice(String price) {
        if (price == null || price.trim().isEmpty()) {
            return price;
        }
        
        // Sadece rakam ve nokta bırak
        return price.replaceAll("[^0-9.]", "").trim();
    }
}
