package com.alperen.kitapsatissistemi.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Kitap Entity - .NET C# Kitap sınıfından dönüştürülmüştür
 */
@Entity
@Table(name = "kitaplar")
public class Kitap {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Kitap adı zorunludur.")
    @Size(max = 200, message = "Kitap adı en fazla 200 karakter olabilir.")
    @Column(name = "ad", nullable = false, length = 200)
    private String ad;
    
    @NotBlank(message = "Yazar adı zorunludur.")
    @Size(max = 100, message = "Yazar adı en fazla 100 karakter olabilir.")
    @Column(name = "yazar", nullable = false, length = 100)
    private String yazar;
    
    @NotNull(message = "Fiyat zorunludur.")
    @DecimalMin(value = "0.01", message = "Fiyat 0.01'den büyük olmalıdır.")
    @DecimalMax(value = "999999.99", message = "Fiyat 999999.99'dan küçük olmalıdır.")
    @Column(name = "fiyat", nullable = false, precision = 18, scale = 2)
    private BigDecimal fiyat;
    
    @Size(max = 1000, message = "Açıklama en fazla 1000 karakter olabilir.")
    @Column(name = "aciklama", length = 1000)
    private String aciklama;
    
    @Size(max = 500, message = "Resim URL'si en fazla 500 karakter olabilir.")
    @Column(name = "resim_url", length = 500)
    private String resimUrl;
    
    @NotNull(message = "Kategori zorunludur.")
    @Column(name = "kategori_id", nullable = false)
    private Long kategoriId;
    
    @Min(value = 0, message = "Stok miktarı 0'dan küçük olamaz.")
    @Column(name = "stok_miktari", nullable = false)
    private Integer stokMiktari = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kategori_id", insertable = false, updatable = false)
    private Kategori kategori;
    
    // Constructors
    public Kitap() {}
    
    public Kitap(String ad, String yazar, BigDecimal fiyat, Long kategoriId) {
        this.ad = ad;
        this.yazar = yazar;
        this.fiyat = fiyat;
        this.kategoriId = kategoriId;
    }
    
    public Kitap(String ad, String yazar, BigDecimal fiyat, String aciklama, Long kategoriId, String resimUrl) {
        this.ad = ad;
        this.yazar = yazar;
        this.fiyat = fiyat;
        this.aciklama = aciklama;
        this.kategoriId = kategoriId;
        this.resimUrl = resimUrl;
        this.stokMiktari = 10; // Varsayılan stok
    }
    
    public Kitap(String ad, String yazar, BigDecimal fiyat, String aciklama, Long kategoriId, String resimUrl, Integer stokMiktari) {
        this.ad = ad;
        this.yazar = yazar;
        this.fiyat = fiyat;
        this.aciklama = aciklama;
        this.kategoriId = kategoriId;
        this.resimUrl = resimUrl;
        this.stokMiktari = stokMiktari;
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
    
    public String getYazar() {
        return yazar;
    }
    
    public void setYazar(String yazar) {
        this.yazar = yazar;
    }
    
    public BigDecimal getFiyat() {
        return fiyat;
    }
    
    public void setFiyat(BigDecimal fiyat) {
        this.fiyat = fiyat;
    }
    
    public String getAciklama() {
        return aciklama;
    }
    
    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }
    
    public String getResimUrl() {
        return resimUrl;
    }
    
    public void setResimUrl(String resimUrl) {
        this.resimUrl = resimUrl;
    }
    
    public Long getKategoriId() {
        return kategoriId;
    }
    
    public void setKategoriId(Long kategoriId) {
        this.kategoriId = kategoriId;
    }
    
    public Kategori getKategori() {
        return kategori;
    }
    
    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }
    
    public Integer getStokMiktari() {
        return stokMiktari;
    }
    
    public void setStokMiktari(Integer stokMiktari) {
        this.stokMiktari = stokMiktari;
    }
    
    @Override
    public String toString() {
        return "Kitap{" +
                "id=" + id +
                ", ad='" + ad + '\'' +
                ", yazar='" + yazar + '\'' +
                ", fiyat=" + fiyat +
                ", kategoriId=" + kategoriId +
                '}';
    }
}