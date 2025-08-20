package com.alperen.kitapsatissistemi.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Kullanıcı kayıt işlemi için DTO
 */
public class RegisterRequest {
    
    @NotBlank(message = "Ad soyad boş olamaz")
    @Size(min = 2, max = 100, message = "Ad soyad 2-100 karakter arasında olmalıdır")
    private String adSoyad;
    
    @NotBlank(message = "Email adresi boş olamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;
    
    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 6, max = 50, message = "Şifre 6-50 karakter arasında olmalıdır")
    private String sifre;
    
    @NotBlank(message = "Şifre tekrarı boş olamaz")
    private String sifreTekrar;
    
    // Constructors
    public RegisterRequest() {}
    
    public RegisterRequest(String adSoyad, String email, String sifre, String sifreTekrar) {
        this.adSoyad = adSoyad;
        this.email = email;
        this.sifre = sifre;
        this.sifreTekrar = sifreTekrar;
    }
    
    // Getters and Setters
    public String getAdSoyad() {
        return adSoyad;
    }
    
    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
    }
    
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
    
    public String getSifreTekrar() {
        return sifreTekrar;
    }
    
    public void setSifreTekrar(String sifreTekrar) {
        this.sifreTekrar = sifreTekrar;
    }
    
    /**
     * Şifre eşleşme kontrolü
     */
    public boolean isPasswordMatching() {
        return sifre != null && sifre.equals(sifreTekrar);
    }
}
