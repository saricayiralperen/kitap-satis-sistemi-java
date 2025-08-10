package com.alperen.kitapsatissistemi.dto;

/**
 * FavoriViewModel - .NET C# projesindeki FavoriViewModel'dan dönüştürülmüştür
 */
public class FavoriViewModel {
    private Long id;
    private String kullaniciAd = "";
    private String kitapAd = "";

    // Constructors
    public FavoriViewModel() {}

    public FavoriViewModel(Long id, String kullaniciAd, String kitapAd) {
        this.id = id;
        this.kullaniciAd = kullaniciAd;
        this.kitapAd = kitapAd;
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

    public String getKitapAd() {
        return kitapAd;
    }

    public void setKitapAd(String kitapAd) {
        this.kitapAd = kitapAd;
    }
}