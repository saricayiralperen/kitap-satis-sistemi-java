package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.service.KitapService;
import com.alperen.kitapsatissistemi.service.KategoriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Collections;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private KitapService kitapService;

    @Autowired
    private KategoriService kategoriService;

    @GetMapping
    public String index(Model model) {
        try {
            // Ana sayfada gösterilecek kitapları getir (örneğin en popüler 8 kitap)
            List<Kitap> featuredBooks = kitapService.findAll().stream()
                    .limit(8)
                    .collect(Collectors.toList());
            
            // Kategorileri getir
            List<Kategori> categories = kategoriService.findAll().stream()
                    .limit(4)
                    .collect(Collectors.toList());
            
            // İstatistikler için örnek veriler
            long totalBooks = kitapService.count();
            long totalCategories = kategoriService.count();
            
            model.addAttribute("featuredBooks", featuredBooks);
            model.addAttribute("categories", categories);
            model.addAttribute("totalBooks", totalBooks);
            model.addAttribute("totalCategories", totalCategories);
            model.addAttribute("happyCustomers", 1250); // Örnek veri
            model.addAttribute("customerSatisfaction", 98); // Örnek veri
            
        } catch (Exception e) {
            // Hata durumunda boş listeler gönder
            model.addAttribute("featuredBooks", Collections.emptyList());
            model.addAttribute("categories", Collections.emptyList());
            model.addAttribute("totalBooks", 0L);
            model.addAttribute("totalCategories", 0L);
            model.addAttribute("happyCustomers", 0);
            model.addAttribute("customerSatisfaction", 0);
        }
        
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }
}