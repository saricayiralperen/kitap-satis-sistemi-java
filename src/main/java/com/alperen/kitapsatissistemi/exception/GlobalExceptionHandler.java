package com.alperen.kitapsatissistemi.exception;

import com.alperen.kitapsatissistemi.service.SecurityAuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler - Tüm controller'lar için merkezi exception yönetimi
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @Autowired
    private SecurityAuditService securityAuditService;

    /**
     * Entity bulunamadığında
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(
            EntityNotFoundException ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("details", request.getDescription(false));
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Validation hataları için
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        String ipAddress = securityAuditService.getClientIpAddress(request);
        String userAgent = securityAuditService.getUserAgent(request);
        
        logger.warn("Validation error on {} from IP {}", request.getRequestURI(), ipAddress);
        
        Map<String, Object> errorDetails = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", "Validation failed");
        errorDetails.put("errors", errors);
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Constraint violation hataları için
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", "Constraint violation: " + ex.getMessage());
        errorDetails.put("details", request.getDescription(false));
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Data integrity violation hataları için
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", "Data integrity violation. This operation conflicts with existing data.");
        errorDetails.put("details", request.getDescription(false));
        errorDetails.put("status", HttpStatus.CONFLICT.value());
        
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    /**
     * Runtime exception'lar için
     */
    /**
     * Access denied hatalarını yakala
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {
        
        String ipAddress = securityAuditService.getClientIpAddress(request);
        String userAgent = securityAuditService.getUserAgent(request);
        
        securityAuditService.logUnauthorizedAccess("unknown", ipAddress, request.getRequestURI(), userAgent);
        logger.warn("Access denied on {} from IP {}", request.getRequestURI(), ipAddress);
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", "Bu sayfaya erişim yetkiniz yok");
        errorDetails.put("details", request.getRequestURI());
        errorDetails.put("status", HttpStatus.FORBIDDEN.value());
        
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }
    
    /**
     * Business exception handler
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        
        String ipAddress = securityAuditService.getClientIpAddress(request);
        String userAgent = securityAuditService.getUserAgent(request);
        
        logger.warn("Business error on {} from IP {}: {}", request.getRequestURI(), ipAddress, ex.getMessage());
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("details", request.getRequestURI());
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Entity not found business exception handler
     */
    @ExceptionHandler(EntityNotFoundBusinessException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundBusinessException(
            EntityNotFoundBusinessException ex, HttpServletRequest request) {
        
        String ipAddress = securityAuditService.getClientIpAddress(request);
        String userAgent = securityAuditService.getUserAgent(request);
        
        logger.warn("Entity not found error on {} from IP {}: {}", request.getRequestURI(), ipAddress, ex.getMessage());
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("details", request.getRequestURI());
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Duplicate entity exception handler
     */
    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEntityException(
            DuplicateEntityException ex, HttpServletRequest request) {
        
        String ipAddress = securityAuditService.getClientIpAddress(request);
        String userAgent = securityAuditService.getUserAgent(request);
        
        logger.warn("Duplicate entity error on {} from IP {}: {}", request.getRequestURI(), ipAddress, ex.getMessage());
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("details", request.getRequestURI());
        errorDetails.put("status", HttpStatus.CONFLICT.value());
        
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {
        
        String ipAddress = securityAuditService.getClientIpAddress(request);
        String userAgent = securityAuditService.getUserAgent(request);
        
        logger.error("Runtime error on {} from IP {}: {}", request.getRequestURI(), ipAddress, ex.getMessage(), ex);
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", "Bir sistem hatası oluştu");
        errorDetails.put("details", request.getRequestURI());
        errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Genel exception'lar için
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGlobalException(Exception ex, WebRequest request) {
        logger.error("Global exception occurred: ", ex);
        
        // Eğer request API endpoint'i ise JSON response döndür
        if (request.getDescription(false).contains("/api/")) {
            return null; // Diğer handler'lar devreye girecek
        }
        
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", "Bir hata oluştu: " + ex.getMessage());
        modelAndView.addObject("timestamp", LocalDateTime.now());
        modelAndView.setViewName("error/500");
        
        return modelAndView;
    }
}
