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
    
    @NotNull(message = "Kullanıcı ID zorunludur.")
    @Column(name = "kullanici_id", nullable = false)
    private Long kullaniciId;
    
    @NotNull(message = "Kitap ID zorunludur.")
    @Column(name = "kitap_id", nullable = false)
    private Long kitapId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_id", insertable = false, updatable = false)
    private Kullanici kullanici;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kitap_id", insertable = false, updatable = false)
    private Kitap kitap;
    
    // Constructors
    public Favori() {}
    
    public Favori(Long kullaniciId, Long kitapId) {
        this.kullaniciId = kullaniciId;
        this.kitapId = kitapId;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getKullaniciId() {
        return kullaniciId;
    }
    
    public void setKullaniciId(Long kullaniciId) {
        this.kullaniciId = kullaniciId;
    }
    
    public Long getKitapId() {
        return kitapId;
    }
    
    public void setKitapId(Long kitapId) {
        this.kitapId = kitapId;
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
                ", kullaniciId=" + kullaniciId +
                ", kitapId=" + kitapId +
                '}';
    }
}