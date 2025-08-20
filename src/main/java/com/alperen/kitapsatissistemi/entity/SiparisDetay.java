package com.alperen.kitapsatissistemi.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * SiparisDetay Entity - .NET C# SiparisDetay sınıfından dönüştürülmüştür
 */
@Entity
@Table(name = "siparis_detaylari")
public class SiparisDetay {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Adet zorunludur.")
    @Min(value = 1, message = "Adet en az 1 olmalıdır.")
    @Column(name = "adet", nullable = false)
    private Integer adet;
    
    @NotNull(message = "Fiyat zorunludur.")
    @Column(name = "fiyat", nullable = false, precision = 18, scale = 2)
    private BigDecimal fiyat;
    
    @NotNull(message = "Sipariş zorunludur.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "siparis_id", nullable = false)
    private Siparis siparis;
    
    @NotNull(message = "Kitap zorunludur.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kitap_id", nullable = false)
    private Kitap kitap;
    
    // Constructors
    public SiparisDetay() {}
    
    public SiparisDetay(Siparis siparis, Kitap kitap, Integer adet, BigDecimal fiyat) {
        this.siparis = siparis;
        this.kitap = kitap;
        this.adet = adet;
        this.fiyat = fiyat;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getSiparisId() {
        return siparis != null ? siparis.getId() : null;
    }
    
    public Long getKitapId() {
        return kitap != null ? kitap.getId() : null;
    }
    
    public Integer getAdet() {
        return adet;
    }
    
    public void setAdet(Integer adet) {
        this.adet = adet;
    }
    
    public BigDecimal getFiyat() {
        return fiyat;
    }
    
    public void setFiyat(BigDecimal fiyat) {
        this.fiyat = fiyat;
    }
    
    public Siparis getSiparis() {
        return siparis;
    }
    
    public void setSiparis(Siparis siparis) {
        this.siparis = siparis;
    }
    
    public Kitap getKitap() {
        return kitap;
    }
    
    public void setKitap(Kitap kitap) {
        this.kitap = kitap;
    }
    
    // Utility methods
    public BigDecimal getToplamFiyat() {
        if (adet != null && fiyat != null) {
            return fiyat.multiply(BigDecimal.valueOf(adet));
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public String toString() {
        return "SiparisDetay{" +
                "id=" + id +
                ", siparisId=" + getSiparisId() +
                ", kitapId=" + getKitapId() +
                ", adet=" + adet +
                ", fiyat=" + fiyat +
                '}';
    }
}
