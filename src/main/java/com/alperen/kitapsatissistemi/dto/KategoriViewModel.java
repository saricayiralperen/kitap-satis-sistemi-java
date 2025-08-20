package com.alperen.kitapsatissistemi.dto;

import com.alperen.kitapsatissistemi.entity.Kitap;
import java.util.List;
import java.util.ArrayList;

/**
 * KategoriViewModel - .NET C# projesindeki KategoriViewModel'dan dönüştürülmüştür
 */
public class KategoriViewModel {
    private Long id;
    private String ad = "";
    private String aciklama = "";
    private List<Kitap> kitaplar = new ArrayList<>();

    // Constructors
    public KategoriViewModel() {}

    public KategoriViewModel(Long id, String ad, String aciklama) {
        this.id = id;
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

    public List<Kitap> getKitaplar() {
        return kitaplar;
    }

    public void setKitaplar(List<Kitap> kitaplar) {
        this.kitaplar = kitaplar;
    }
}
