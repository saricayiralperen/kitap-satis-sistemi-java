package com.alperen.kitapsatissistemi.exception;

/**
 * İş mantığı hataları için özel exception sınıfı
 */
public class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}