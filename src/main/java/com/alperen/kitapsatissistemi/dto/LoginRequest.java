package com.alperen.kitapsatissistemi.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Login işlemi için DTO
 */
public class LoginRequest {
    
    @NotBlank(message = "Email adresi boş olamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;
    
    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır")
    private String sifre;
    
    // Constructors
    public LoginRequest() {}
    
    public LoginRequest(String email, String sifre) {
        this.email = email;
        this.sifre = sifre;
    }
    
    // Getters and Setters
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSifre() {
        return sifre;
    }
    
    public void setSifre(String sifre) {
        this.sifre = sifre;
    }
}
