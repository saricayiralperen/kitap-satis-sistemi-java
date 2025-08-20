package com.alperen.kitapsatissistemi.exception;

/**
 * Entity bulunamadığında fırlatılan özel exception sınıfı
 */
public class EntityNotFoundBusinessException extends BusinessException {
    
    public EntityNotFoundBusinessException(String entityName, Object id) {
        super(String.format("%s bulunamadı, ID: %s", entityName, id));
    }
    
    public EntityNotFoundBusinessException(String message) {
        super(message);
    }
    
    public EntityNotFoundBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}