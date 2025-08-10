package com.alperen.kitapsatissistemi.dto;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * KitapViewModel - .NET C# projesindeki KitapViewModel'dan dönüştürülmüştür
 */
public class KitapViewModel {
    private Long id;
    
    @NotBlank(message = "Kitap adı zorunludur.")
    private String ad = "";
    
    @NotBlank(message = "Yazar adı zorunludur.")
    private String yazar = "";
    
    @NotNull(message = "Fiyat zorunludur.")
    @DecimalMin(value = "0.01", message = "Fiyat 0'dan büyük olmalıdır.")
    private BigDecimal fiyat;
    
    @NotBlank(message = "Açıklama zorunludur.")
    private String aciklama = "";
    
    @NotNull(message = "Kategori seçimi zorunludur.")
    private Long kategoriId;
    
    private String kategoriAd = "";
    private String resimUrl = "";

    // Constructors
    public KitapViewModel() {}

    public KitapViewModel(Long id, String ad, String yazar, BigDecimal fiyat, String aciklama, Long kategoriId) {
        this.id = id;
        this.ad = ad;
        this.yazar = yazar;
        this.fiyat = fiyat;
        this.aciklama = aciklama;
        this.kategoriId = kategoriId;
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

    public Long getKategoriId() {
        return kategoriId;
    }

    public void setKategoriId(Long kategoriId) {
        this.kategoriId = kategoriId;
    }

    public String getKategoriAd() {
        return kategoriAd;
    }

    public void setKategoriAd(String kategoriAd) {
        this.kategoriAd = kategoriAd;
    }

    public String getResimUrl() {
        return resimUrl;
    }

    public void setResimUrl(String resimUrl) {
        this.resimUrl = resimUrl;
    }
}