package com.alperen.kitapsatissistemi.dto;

import java.math.BigDecimal;

/**
 * KitapSiparisIstatistik - .NET C# projesindeki KitapSiparisIstatistik'ten dönüştürülmüştür
 */
public class KitapSiparisIstatistik {
    private Long kitapId;
    private String kitapAd;
    private String yazar;
    private int toplamSatilanAdet;
    private BigDecimal toplamSatisTutari;
    private int siparisAdedi;

    // Constructors
    public KitapSiparisIstatistik() {}

    public KitapSiparisIstatistik(Long kitapId, String kitapAd, String yazar, 
                                 int toplamSatilanAdet, BigDecimal toplamSatisTutari, int siparisAdedi) {
        this.kitapId = kitapId;
        this.kitapAd = kitapAd;
        this.yazar = yazar;
        this.toplamSatilanAdet = toplamSatilanAdet;
        this.toplamSatisTutari = toplamSatisTutari;
        this.siparisAdedi = siparisAdedi;
    }

    // Getters and Setters
    public Long getKitapId() {
        return kitapId;
    }

    public void setKitapId(Long kitapId) {
        this.kitapId = kitapId;
    }

    public String getKitapAd() {
        return kitapAd;
    }

    public void setKitapAd(String kitapAd) {
        this.kitapAd = kitapAd;
    }

    public String getYazar() {
        return yazar;
    }

    public void setYazar(String yazar) {
        this.yazar = yazar;
    }

    public int getToplamSatilanAdet() {
        return toplamSatilanAdet;
    }

    public void setToplamSatilanAdet(int toplamSatilanAdet) {
        this.toplamSatilanAdet = toplamSatilanAdet;
    }

    public BigDecimal getToplamSatisTutari() {
        return toplamSatisTutari;
    }

    public void setToplamSatisTutari(BigDecimal toplamSatisTutari) {
        this.toplamSatisTutari = toplamSatisTutari;
    }

    public int getSiparisAdedi() {
        return siparisAdedi;
    }

    public void setSiparisAdedi(int siparisAdedi) {
        this.siparisAdedi = siparisAdedi;
    }
}