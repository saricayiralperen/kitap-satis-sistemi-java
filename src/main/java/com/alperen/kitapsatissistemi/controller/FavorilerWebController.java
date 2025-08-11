package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Favori;
import com.alperen.kitapsatissistemi.service.FavoriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * FavorilerWebController - Favoriler web sayfaları için controller
 */
@Controller
@RequestMapping("/favoriler")
public class FavorilerWebController {

    @Autowired
    private FavoriService favoriService;

    /**
     * Favoriler sayfası
     * GET /favoriler
     */
    @GetMapping
    public String index(Model model, HttpSession session) {
        // Kullanıcı giriş kontrolü
        Long kullaniciId = (Long) session.getAttribute("KullaniciId");
        if (kullaniciId == null) {
            return "redirect:/kullanici/login";
        }
        
        try {
            List<Favori> favoriler = favoriService.getFavorilerByKullaniciIdWithDetails(kullaniciId);
            model.addAttribute("favoriler", favoriler);
            model.addAttribute("title", "Favorilerim");
        } catch (Exception e) {
            System.out.println("=== FAVORILER HATA DETAYI ===");
            System.out.println("Hata mesajı: " + e.getMessage());
            System.out.println("Hata sınıfı: " + e.getClass().getName());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Favoriler yüklenirken hata oluştu: " + e.getMessage());
            model.addAttribute("favoriler", new java.util.ArrayList<>());
        }
        
        return "favoriler";
    }

    /**
     * Favorilerden kaldır
     * POST /favoriler/remove/{id}
     */
    @PostMapping("/remove/{id}")
    public String removeFavori(@PathVariable Long id, 
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        // Kullanıcı giriş kontrolü
        Long kullaniciId = (Long) session.getAttribute("KullaniciId");
        if (kullaniciId == null) {
            return "redirect:/kullanici/login";
        }
        
        try {
            favoriService.deleteFavori(id);
            redirectAttributes.addFlashAttribute("successMessage", "Kitap favorilerden kaldırıldı.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Favori kaldırılırken hata oluştu: " + e.getMessage());
        }
        
        return "redirect:/favoriler";
    }
}