package com.alperen.kitapsatissistemi.dto;

import java.math.BigDecimal;

/**
 * SiparisDetayViewModel - .NET C# projesindeki SiparisDetayViewModel'dan dönüştürülmüştür
 */
public class SiparisDetayViewModel {
    private String kitapAd = "";
    private Integer adet;
    private BigDecimal fiyat;

    // Constructors
    public SiparisDetayViewModel() {}

    public SiparisDetayViewModel(String kitapAd, Integer adet, BigDecimal fiyat) {
        this.kitapAd = kitapAd;
        this.adet = adet;
        this.fiyat = fiyat;
    }

    // Getters and Setters
    public String getKitapAd() {
        return kitapAd;
    }

    public void setKitapAd(String kitapAd) {
        this.kitapAd = kitapAd;
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
}
