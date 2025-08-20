package com.alperen.kitapsatissistemi.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Favori Entity - .NET C# Favori sınıfından dönüştürülmüştür
 */
@Entity
@Table(name = "favoriler")
public class Favori {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Kullanıcı zorunludur.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_id", nullable = false)
    private Kullanici kullanici;
    
    @NotNull(message = "Kitap zorunludur.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kitap_id", nullable = false)
    private Kitap kitap;
    
    // Constructors
    public Favori() {}
    
    public Favori(Kullanici kullanici, Kitap kitap) {
        this.kullanici = kullanici;
        this.kitap = kitap;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getKullaniciId() {
        return kullanici != null ? kullanici.getId() : null;
    }
    
    public Long getKitapId() {
        return kitap != null ? kitap.getId() : null;
    }
    
    public Kullanici getKullanici() {
        return kullanici;
    }
    
    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }
    
    public Kitap getKitap() {
        return kitap;
    }
    
    public void setKitap(Kitap kitap) {
        this.kitap = kitap;
    }
    
    @Override
    public String toString() {
        return "Favori{" +
                "id=" + id +
                ", kullaniciId=" + getKullaniciId() +
                ", kitapId=" + getKitapId() +
                '}';
    }
}
