package com.alperen.kitapsatissistemi.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * Kullanici Entity - .NET C# Kullanici sınıfından dönüştürülmüştür
 */
@Entity
@Table(name = "kullanicilar")
public class Kullanici {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Ad soyad zorunludur.")
    @Size(max = 100, message = "Ad soyad en fazla 100 karakter olabilir.")
    @Column(name = "ad_soyad", nullable = false, length = 100)
    private String adSoyad;
    
    @NotBlank(message = "Email zorunludur.")
    @Email(message = "Geçerli bir email adresi giriniz.")
    @Size(max = 100, message = "Email en fazla 100 karakter olabilir.")
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;
    
    @NotBlank(message = "Şifre zorunludur.")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır.")
    @Size(max = 255, message = "Şifre en fazla 255 karakter olabilir.")
    @Column(name = "sifre_hash", nullable = false, length = 255)
    private String sifreHash;
    
    @NotBlank(message = "Rol zorunludur.")
    @Size(max = 50, message = "Rol en fazla 50 karakter olabilir.")
    @Column(name = "rol", nullable = false, length = 50)
    private String rol = "User";
    
    @Column(name = "kayit_tarihi", nullable = false)
    private LocalDateTime kayitTarihi;
    
    // Constructors
    public Kullanici() {
        this.kayitTarihi = LocalDateTime.now();
        this.rol = "User";
    }
    
    public Kullanici(String adSoyad, String email, String sifreHash) {
        this();
        this.adSoyad = adSoyad;
        this.email = email;
        this.sifreHash = sifreHash;
    }
    
    public Kullanici(String adSoyad, String email, String sifreHash, String rol) {
        this(adSoyad, email, sifreHash);
        this.rol = rol;
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
    
    public String getSifreHash() {
        return sifreHash;
    }
    
    public void setSifreHash(String sifreHash) {
        this.sifreHash = sifreHash;
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
    
    // Utility methods
    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(this.rol);
    }
    
    public boolean isUser() {
        return "User".equalsIgnoreCase(this.rol);
    }
    
    @PrePersist
    protected void onCreate() {
        if (kayitTarihi == null) {
            kayitTarihi = LocalDateTime.now();
        }
        if (rol == null || rol.trim().isEmpty()) {
            rol = "User";
        }
    }
    
    @Override
    public String toString() {
        return "Kullanici{" +
                "id=" + id +
                ", adSoyad='" + adSoyad + '\'' +
                ", email='" + email + '\'' +
                ", rol='" + rol + '\'' +
                ", kayitTarihi=" + kayitTarihi +
                '}';
    }
}
