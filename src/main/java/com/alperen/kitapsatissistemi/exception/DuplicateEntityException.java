package com.alperen.kitapsatissistemi.exception;

/**
 * Duplicate entity hataları için özel exception sınıfı
 */
public class DuplicateEntityException extends BusinessException {
    
    public DuplicateEntityException(String entityName, String fieldName, Object value) {
        super(String.format("%s zaten mevcut - %s: %s", entityName, fieldName, value));
    }
    
    public DuplicateEntityException(String message) {
        super(message);
    }
    
    public DuplicateEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}