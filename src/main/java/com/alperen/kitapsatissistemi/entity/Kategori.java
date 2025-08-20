package com.alperen.kitapsatissistemi.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Kategori Entity - .NET C# Kategori sınıfından dönüştürülmüştür
 */
@Entity
@Table(name = "kategoriler")
public class Kategori {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Kategori adı zorunludur.")
    @Size(max = 100, message = "Kategori adı en fazla 100 karakter olabilir.")
    @Column(name = "ad", nullable = false, length = 100)
    private String ad;
    
    @Size(max = 500, message = "Açıklama en fazla 500 karakter olabilir.")
    @Column(name = "aciklama", length = 500)
    private String aciklama;
    
    // Transient field - veritabanında saklanmaz, sadece UI için kullanılır
    @Transient
    private Integer kitapSayisi;
    
    // Constructors
    public Kategori() {}
    
    public Kategori(String ad) {
        this.ad = ad;
    }
    
    public Kategori(String ad, String aciklama) {
        this.ad = ad;
        this.aciklama = aciklama;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAd() {
        return ad;
    }
    
    public void setAd(String ad) {
        this.ad = ad;
    }
    
    public String getAciklama() {
        return aciklama;
    }
    
    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }
    
    public Integer getKitapSayisi() {
        return kitapSayisi;
    }
    
    public void setKitapSayisi(Integer kitapSayisi) {
        this.kitapSayisi = kitapSayisi;
    }
    
    @Override
    public String toString() {
        return "Kategori{" +
                "id=" + id +
                ", ad='" + ad + '\'' +
                ", aciklama='" + aciklama + '\'' +
                '}';
    }
}
