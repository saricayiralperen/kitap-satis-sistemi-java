package com.alperen.kitapsatissistemi.dto;

import javax.validation.constraints.*;

/**
 * ChangePasswordViewModel - .NET C# projesindeki ChangePasswordViewModel'dan dönüştürülmüştür
 */
public class ChangePasswordViewModel {
    private Long kullaniciId;
    
    private String kullaniciAd = "";
    
    @NotBlank(message = "Yeni şifre zorunludur.")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır.")
    private String yeniSifre = "";
    
    @NotBlank(message = "Şifre tekrarı zorunludur.")
    private String yeniSifreTekrar = "";

    // Constructors
    public ChangePasswordViewModel() {}

    public ChangePasswordViewModel(Long kullaniciId, String kullaniciAd) {
        this.kullaniciId = kullaniciId;
        this.kullaniciAd = kullaniciAd;
    }

    // Getters and Setters
    public Long getKullaniciId() {
        return kullaniciId;
    }

    public void setKullaniciId(Long kullaniciId) {
        this.kullaniciId = kullaniciId;
    }

    public String getKullaniciAd() {
        return kullaniciAd;
    }

    public void setKullaniciAd(String kullaniciAd) {
        this.kullaniciAd = kullaniciAd;
    }

    public String getYeniSifre() {
        return yeniSifre;
    }

    public void setYeniSifre(String yeniSifre) {
        this.yeniSifre = yeniSifre;
    }

    public String getYeniSifreTekrar() {
        return yeniSifreTekrar;
    }

    public void setYeniSifreTekrar(String yeniSifreTekrar) {
        this.yeniSifreTekrar = yeniSifreTekrar;
    }

    // Şifre eşleşme kontrolü
    public boolean isPasswordMatching() {
        return yeniSifre != null && yeniSifre.equals(yeniSifreTekrar);
    }
}
