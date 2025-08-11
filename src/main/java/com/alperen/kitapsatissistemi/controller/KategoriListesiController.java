package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.service.KategoriService;
import com.alperen.kitapsatissistemi.service.KitapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * KategoriListesiController - /kategori-listesi endpoint'i için controller
 */
@Controller
public class KategoriListesiController {

    @Autowired
    private KategoriService kategoriService;
    
    @Autowired
    private KitapService kitapService;

    /**
     * Kategori listesi sayfası
     * GET /kategori-listesi
     */
    @GetMapping("/kategori-listesi")
    public String kategoriListesi(Model model, HttpSession session) {
        try {
            // Kullanıcı oturum bilgilerini kontrol et
            Long kullaniciId = (Long) session.getAttribute("kullaniciId");
            String kullaniciAdi = (String) session.getAttribute("kullaniciAdi");
            
            if (kullaniciId != null && kullaniciAdi != null) {
                model.addAttribute("isLoggedIn", true);
                model.addAttribute("kullaniciAdi", kullaniciAdi);
            } else {
                model.addAttribute("isLoggedIn", false);
            }
            
            // Tüm kategorileri getir
            List<Kategori> kategoriler = kategoriService.getAllKategoriler();
            
            // Her kategori için kitap sayısını hesapla
            Map<Long, Long> kategoriKitapSayilari = new HashMap<>();
            for (Kategori kategori : kategoriler) {
                long kitapSayisi = kitapService.countByKategoriId(kategori.getId());
                kategoriKitapSayilari.put(kategori.getId(), kitapSayisi);
            }
            
            // Model'e verileri ekle
            model.addAttribute("kategoriler", kategoriler);
            model.addAttribute("kategoriKitapSayilari", kategoriKitapSayilari);
            model.addAttribute("title", "Kategoriler");
            
            return "kategoriler/index";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kategoriler yüklenirken bir hata oluştu: " + e.getMessage());
            return "error/500";
        }
    }
}