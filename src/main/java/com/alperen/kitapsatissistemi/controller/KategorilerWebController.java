package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.service.KategoriService;
import com.alperen.kitapsatissistemi.service.KitapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * KategorilerWebController - Kategoriler web sayfaları için controller
 * .NET C# projesindeki KategorilerController'dan dönüştürülmüştür
 */
@Controller
@RequestMapping("/kategoriler")
public class KategorilerWebController {

    @Autowired
    private KategoriService kategoriService;
    
    @Autowired
    private KitapService kitapService;



    /**
     * Kategoriler ana sayfası
     * GET /kategoriler
     */
    @GetMapping
    public String index(Model model, HttpSession session) {
        // Kullanıcı oturum bilgilerini kontrol et
        Long kullaniciId = (Long) session.getAttribute("kullaniciId");
        String kullaniciAdi = (String) session.getAttribute("kullaniciAdi");
        
        if (kullaniciId != null && kullaniciAdi != null) {
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("kullaniciAdi", kullaniciAdi);
        } else {
            model.addAttribute("isLoggedIn", false);
        }
        try {
            // Tüm kategorileri getir
            List<Kategori> kategoriler = kategoriService.findAll();
            
            // Her kategori için kitap sayısını hesapla
            for (Kategori kategori : kategoriler) {
                long kitapSayisi = kitapService.countByKategoriId(kategori.getId());
                kategori.setKitapSayisi((int) kitapSayisi);
            }
            
            model.addAttribute("kategoriler", kategoriler);
            model.addAttribute("title", "Kategoriler");
            
            return "kategoriler/index";
            
        } catch (Exception e) {
            // Hata durumunda boş liste gönder
            model.addAttribute("kategoriler", Collections.emptyList());
            model.addAttribute("errorMessage", "Kategoriler yüklenirken bir hata oluştu: " + e.getMessage());
            model.addAttribute("title", "Kategoriler");
            return "kategoriler/index";
        }
    }

    /**
     * Kategori detay sayfası - kategoriye ait kitapları listele
     * GET /kategoriler/{id}
     */
    @GetMapping("/{id}")
    public String detay(@PathVariable Long id, 
                       Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "12") int size,
                       @RequestParam(defaultValue = "ad") String sortBy,
                       @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            // Kategoriyi getir
            Optional<Kategori> kategoriOpt = kategoriService.findById(id);
            if (!kategoriOpt.isPresent()) {
                model.addAttribute("errorMessage", "Kategori bulunamadı.");
                return "redirect:/kategoriler";
            }
            
            Kategori kategori = kategoriOpt.get();
            
            // Sorting direction
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by(direction, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            // Kategoriye ait kitapları getir
            Page<Kitap> kitapPage = kitapService.findByKategoriId(id, pageable);
            
            // Model'e verileri ekle
            model.addAttribute("kategori", kategori);
            model.addAttribute("kitapPage", kitapPage);
            model.addAttribute("kitaplar", kitapPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", kitapPage.getTotalPages());
            model.addAttribute("totalElements", kitapPage.getTotalElements());
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("title", kategori.getAd() + " Kategorisi");
            
            return "kategoriler/detay";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kategori detayları yüklenirken bir hata oluştu: " + e.getMessage());
            return "redirect:/kategoriler";
        }
    }

    /**
     * Kategori kitapları sayfası (alternatif endpoint)
     * GET /kategoriler/{id}/kitaplar
     */
    @GetMapping("/{id}/kitaplar")
    public String kategoriKitaplari(@PathVariable Long id, 
                                   Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "12") int size,
                                   @RequestParam(defaultValue = "ad") String sortBy,
                                   @RequestParam(defaultValue = "asc") String sortDir) {
        // Detay metodunu çağır
        return detay(id, model, page, size, sortBy, sortDir);
    }
}