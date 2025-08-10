package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.service.KitapService;
import com.alperen.kitapsatissistemi.service.KategoriService;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Collections;

/**
 * KitapWebController - Web sayfaları için kitap controller'ı
 */
@Controller
@RequestMapping("/kitaplar")
public class KitapWebController {
    
    @Autowired
    private KitapService kitapService;
    
    @Autowired
    private KategoriService kategoriService;
    
    /**
     * Kitaplar ana sayfası
     * GET /kitaplar
     */
    @GetMapping
    public String index(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "12") int size,
                       @RequestParam(defaultValue = "ad") String sortBy,
                       @RequestParam(defaultValue = "asc") String sortDir,
                       @RequestParam(required = false) String search,
                       @RequestParam(required = false) Long kategoriId) {
        try {
            // Sorting direction
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by(direction, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Kitap> kitapPage;
            
            // Filtreleme ve arama
            if (search != null && !search.trim().isEmpty()) {
                kitapPage = kitapService.findByAdContainingIgnoreCase(search.trim(), pageable);
            } else if (kategoriId != null) {
                kitapPage = kitapService.findByKategoriId(kategoriId, pageable);
            } else {
                kitapPage = kitapService.findAll(pageable);
            }
            
            // Kategorileri getir
            List<Kategori> kategoriler = kategoriService.findAll();
            
            // Model'e verileri ekle
            model.addAttribute("kitapPage", kitapPage);
            model.addAttribute("kitaplar", kitapPage.getContent());
            model.addAttribute("kategoriler", kategoriler);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", kitapPage.getTotalPages());
            model.addAttribute("totalElements", kitapPage.getTotalElements());
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("search", search);
            model.addAttribute("kategoriId", kategoriId);
            
            return "kitaplar/index";
            
        } catch (Exception e) {
            // Hata durumunda boş veriler gönder
            model.addAttribute("kitaplar", Collections.emptyList());
            model.addAttribute("kategoriler", Collections.emptyList());
            model.addAttribute("error", "Kitaplar yüklenirken bir hata oluştu: " + e.getMessage());
            return "kitaplar/index";
        }
    }
    
    /**
     * Kitap detay sayfası
     * GET /kitaplar/{id}
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        try {
            Optional<Kitap> kitapOpt = kitapService.getKitapByIdWithKategori(id);
            
            if (kitapOpt.isPresent()) {
                Kitap kitap = kitapOpt.get();
                model.addAttribute("kitap", kitap);
                
                // Benzer kitapları getir (aynı kategoriden)
                if (kitap.getKategori() != null) {
                    List<Kitap> benzerKitaplar = kitapService.getKitaplarByKategoriId(kitap.getKategori().getId())
                            .stream()
                            .filter(k -> !k.getId().equals(id))
                            .limit(4)
                            .collect(Collectors.toList());
                    model.addAttribute("relatedBooks", benzerKitaplar);
                } else {
                    model.addAttribute("relatedBooks", Collections.emptyList());
                }
                
                return "kitaplar/detail";
            } else {
                model.addAttribute("error", "Kitap bulunamadı.");
                return "error/404";
            }
            
        } catch (Exception e) {
            model.addAttribute("error", "Kitap detayları yüklenirken bir hata oluştu: " + e.getMessage());
            return "error/500";
        }
    }
}