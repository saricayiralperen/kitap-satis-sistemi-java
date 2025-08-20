package com.alperen.kitapsatissistemi.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Siparis Entity - .NET C# Siparis sınıfından dönüştürülmüştür
 */
@Entity
@Table(name = "siparisler")
public class Siparis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "siparis_tarihi", nullable = false)
    private LocalDateTime siparisTarihi;
    
    @NotNull(message = "Toplam tutar zorunludur.")
    @Column(name = "toplam_tutar", nullable = false, precision = 18, scale = 2)
    private BigDecimal toplamTutar;
    
    @NotBlank(message = "Durum zorunludur.")
    @Size(max = 50, message = "Durum en fazla 50 karakter olabilir.")
    @Column(name = "durum", length = 50)
    private String durum = "Beklemede";
    
    @NotNull(message = "Kullanıcı zorunludur.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_id", nullable = false)
    private Kullanici kullanici;
    
    @OneToMany(mappedBy = "siparis", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<SiparisDetay> siparisDetaylari = new ArrayList<>();
    
    // Constructors
    public Siparis() {
        this.siparisTarihi = LocalDateTime.now();
        this.durum = "Beklemede";
    }
    
    public Siparis(Kullanici kullanici, BigDecimal toplamTutar) {
        this();
        this.kullanici = kullanici;
        this.toplamTutar = toplamTutar;
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
    
    public LocalDateTime getSiparisTarihi() {
        return siparisTarihi;
    }
    
    public void setSiparisTarihi(LocalDateTime siparisTarihi) {
        this.siparisTarihi = siparisTarihi;
    }
    
    public BigDecimal getToplamTutar() {
        return toplamTutar;
    }
    
    public void setToplamTutar(BigDecimal toplamTutar) {
        this.toplamTutar = toplamTutar;
    }
    
    public String getDurum() {
        return durum;
    }
    
    public void setDurum(String durum) {
        this.durum = durum;
    }
    
    public Kullanici getKullanici() {
        return kullanici;
    }
    
    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }
    
    public List<SiparisDetay> getSiparisDetaylari() {
        return siparisDetaylari;
    }
    
    public void setSiparisDetaylari(List<SiparisDetay> siparisDetaylari) {
        this.siparisDetaylari = siparisDetaylari;
    }
    
    // Utility methods
    public void addSiparisDetay(SiparisDetay siparisDetay) {
        siparisDetaylari.add(siparisDetay);
        siparisDetay.setSiparis(this);
    }
    
    public void removeSiparisDetay(SiparisDetay siparisDetay) {
        siparisDetaylari.remove(siparisDetay);
        siparisDetay.setSiparis(null);
    }
    
    public boolean isBeklemede() {
        return "Beklemede".equals(this.durum);
    }
    
    public boolean isOnaylandi() {
        return "Onaylandı".equals(this.durum);
    }
    
    public boolean isIptalEdildi() {
        return "İptal Edildi".equals(this.durum);
    }
    
    @PrePersist
    protected void onCreate() {
        if (siparisTarihi == null) {
            siparisTarihi = LocalDateTime.now();
        }
        if (durum == null || durum.trim().isEmpty()) {
            durum = "Beklemede";
        }
    }
    
    @Override
    public String toString() {
        return "Siparis{" +
                "id=" + id +
                ", kullaniciId=" + getKullaniciId() +
                ", siparisTarihi=" + siparisTarihi +
                ", toplamTutar=" + toplamTutar +
                ", durum='" + durum + '\'' +
                '}';
    }
}
