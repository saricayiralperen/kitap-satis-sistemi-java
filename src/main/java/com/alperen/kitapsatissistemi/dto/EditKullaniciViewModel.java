package com.alperen.kitapsatissistemi.dto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * EditKullaniciViewModel - .NET C# projesindeki EditKullaniciViewModel'dan dönüştürülmüştür
 */
public class EditKullaniciViewModel {
    private Long id;
    
    @NotBlank(message = "Ad Soyad alanı zorunludur.")
    private String adSoyad = "";
    
    @NotBlank(message = "Email alanı zorunludur.")
    @Email(message = "Geçerli bir email adresi giriniz.")
    private String email = "";
    
    @NotBlank(message = "Rol alanı zorunludur.")
    private String rol = "";
    
    private LocalDateTime kayitTarihi;

    // Constructors
    public EditKullaniciViewModel() {}

    public EditKullaniciViewModel(Long id, String adSoyad, String email, String rol, LocalDateTime kayitTarihi) {
        this.id = id;
        this.adSoyad = adSoyad;
        this.email = email;
        this.rol = rol;
        this.kayitTarihi = kayitTarihi;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public LocalDateTime getKayitTarihi() {
        return kayitTarihi;
    }

    public void setKayitTarihi(LocalDateTime kayitTarihi) {
        this.kayitTarihi = kayitTarihi;
    }
}
