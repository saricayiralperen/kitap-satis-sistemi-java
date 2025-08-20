package com.alperen.kitapsatissistemi.dto;

import com.alperen.kitapsatissistemi.entity.Siparis;
import com.alperen.kitapsatissistemi.entity.Kitap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * RaporViewModel - .NET C# projesindeki RaporViewModel'dan dönüştürülmüştür
 */
public class RaporViewModel {
    private int toplamKitap;
    private int toplamKullanici;
    private int toplamKategori;
    private int buAySiparis;
    private List<Siparis> sonSiparisler = new ArrayList<>();
    private List<Kitap> populerKitaplar = new ArrayList<>();
    private List<KitapSiparisIstatistik> kitapSiparisIstatistikleri = new ArrayList<>();
    private Map<String, Integer> kategoriDagilimi = new HashMap<>();
    private Map<String, Integer> aylikSiparisler = new HashMap<>();

    // Constructors
    public RaporViewModel() {}

    public RaporViewModel(int toplamKitap, int toplamKullanici, int toplamKategori, int buAySiparis) {
        this.toplamKitap = toplamKitap;
        this.toplamKullanici = toplamKullanici;
        this.toplamKategori = toplamKategori;
        this.buAySiparis = buAySiparis;
    }

    // Getters and Setters
    public int getToplamKitap() {
        return toplamKitap;
    }

    public void setToplamKitap(int toplamKitap) {
        this.toplamKitap = toplamKitap;
    }

    public int getToplamKullanici() {
        return toplamKullanici;
    }

    public void setToplamKullanici(int toplamKullanici) {
        this.toplamKullanici = toplamKullanici;
    }

    public int getToplamKategori() {
        return toplamKategori;
    }

    public void setToplamKategori(int toplamKategori) {
        this.toplamKategori = toplamKategori;
    }

    public int getBuAySiparis() {
        return buAySiparis;
    }

    public void setBuAySiparis(int buAySiparis) {
        this.buAySiparis = buAySiparis;
    }

    public List<Siparis> getSonSiparisler() {
        return sonSiparisler;
    }

    public void setSonSiparisler(List<Siparis> sonSiparisler) {
        this.sonSiparisler = sonSiparisler;
    }

    public List<Kitap> getPopulerKitaplar() {
        return populerKitaplar;
    }

    public void setPopulerKitaplar(List<Kitap> populerKitaplar) {
        this.populerKitaplar = populerKitaplar;
    }

    public List<KitapSiparisIstatistik> getKitapSiparisIstatistikleri() {
        return kitapSiparisIstatistikleri;
    }

    public void setKitapSiparisIstatistikleri(List<KitapSiparisIstatistik> kitapSiparisIstatistikleri) {
        this.kitapSiparisIstatistikleri = kitapSiparisIstatistikleri;
    }

    public Map<String, Integer> getKategoriDagilimi() {
        return kategoriDagilimi;
    }

    public void setKategoriDagilimi(Map<String, Integer> kategoriDagilimi) {
        this.kategoriDagilimi = kategoriDagilimi;
    }

    public Map<String, Integer> getAylikSiparisler() {
        return aylikSiparisler;
    }

    public void setAylikSiparisler(Map<String, Integer> aylikSiparisler) {
        this.aylikSiparisler = aylikSiparisler;
    }
}
