package com.alperen.kitapsatissistemi.entity;

import java.math.BigDecimal;

public class SepetItem {
    private int kitapId;
    private String kitapAd;
    private BigDecimal fiyat;
    private int adet;
    private String resimUrl;
    private BigDecimal toplamFiyat;

    // Constructors
    public SepetItem() {}

    public SepetItem(int kitapId, String kitapAd, BigDecimal fiyat, int adet, String resimUrl) {
        this.kitapId = kitapId;
        this.kitapAd = kitapAd;
        this.fiyat = fiyat;
        this.adet = adet;
        this.resimUrl = resimUrl;
    }

    // Getters and Setters
    public int getKitapId() {
        return kitapId;
    }

    public void setKitapId(int kitapId) {
        this.kitapId = kitapId;
    }

    public String getKitapAd() {
        return kitapAd;
    }

    public void setKitapAd(String kitapAd) {
        this.kitapAd = kitapAd;
    }

    public BigDecimal getFiyat() {
        return fiyat;
    }

    public void setFiyat(BigDecimal fiyat) {
        this.fiyat = fiyat;
    }

    public int getAdet() {
        return adet;
    }

    public void setAdet(int adet) {
        this.adet = adet;
    }

    public String getResimUrl() {
        return resimUrl;
    }

    public void setResimUrl(String resimUrl) {
        this.resimUrl = resimUrl;
    }

    public BigDecimal getToplamFiyat() {
        if (toplamFiyat != null) {
            return toplamFiyat;
        }
        return fiyat.multiply(BigDecimal.valueOf(adet));
    }

    public void setToplamFiyat(BigDecimal toplamFiyat) {
        this.toplamFiyat = toplamFiyat;
    }
}
