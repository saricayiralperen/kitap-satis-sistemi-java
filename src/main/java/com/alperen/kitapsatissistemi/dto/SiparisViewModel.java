package com.alperen.kitapsatissistemi.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

/**
 * SiparisViewModel - .NET C# projesindeki SiparisViewModel'dan dönüştürülmüştür
 */
public class SiparisViewModel {
    private Long id;
    private String kullaniciAd = "";
    private String kullaniciEmail = "";
    private String siparisTarihi = "";
    private BigDecimal toplamTutar;
    private String durum = "Beklemede";
    private List<SiparisDetayViewModel> siparisDetaylari = new ArrayList<>();

    // Constructors
    public SiparisViewModel() {}

    public SiparisViewModel(Long id, String kullaniciAd, String kullaniciEmail, String siparisTarihi, 
                           BigDecimal toplamTutar, String durum) {
        this.id = id;
        this.kullaniciAd = kullaniciAd;
        this.kullaniciEmail = kullaniciEmail;
        this.siparisTarihi = siparisTarihi;
        this.toplamTutar = toplamTutar;
        this.durum = durum;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKullaniciAd() {
        return kullaniciAd;
    }

    public void setKullaniciAd(String kullaniciAd) {
        this.kullaniciAd = kullaniciAd;
    }

    public String getKullaniciEmail() {
        return kullaniciEmail;
    }

    public void setKullaniciEmail(String kullaniciEmail) {
        this.kullaniciEmail = kullaniciEmail;
    }

    public String getSiparisTarihi() {
        return siparisTarihi;
    }

    public void setSiparisTarihi(String siparisTarihi) {
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

    public List<SiparisDetayViewModel> getSiparisDetaylari() {
        return siparisDetaylari;
    }

    public void setSiparisDetaylari(List<SiparisDetayViewModel> siparisDetaylari) {
        this.siparisDetaylari = siparisDetaylari;
    }
}
