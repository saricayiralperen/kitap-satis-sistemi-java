package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.service.KullaniciService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * AdminController - .NET C# projesindeki AdminController'dan dönüştürülmüştür
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private KullaniciService kullaniciService;

    /**
     * Admin giriş sayfası
     * GET /admin/login
     */
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Admin Girişi");
        return "admin/login";
    }

    /**
     * Admin giriş işlemi
     * POST /admin/login
     */
    @PostMapping("/login")
    public String login(@RequestParam String email,
                       @RequestParam String sifre,
                       HttpSession session,
                       RedirectAttributes redirectAttributes,
                       Model model) {
        try {
            Optional<Kullanici> kullaniciOpt = kullaniciService.authenticateKullanici(email, sifre);
            
            if (kullaniciOpt.isPresent()) {
                Kullanici kullanici = kullaniciOpt.get();
                
                // Admin kontrolü
                if ("Admin".equals(kullanici.getRol())) {
                    // Session bilgilerini set et
                    session.setAttribute("KullaniciId", kullanici.getId());
                    session.setAttribute("KullaniciAd", kullanici.getAdSoyad());
                    session.setAttribute("KullaniciEmail", kullanici.getEmail());
                    session.setAttribute("KullaniciRol", kullanici.getRol());
                    session.setAttribute("IsAdmin", true);
                    
                    redirectAttributes.addFlashAttribute("successMessage", "Admin olarak başarıyla giriş yaptınız!");
                    return "redirect:/admin/dashboard";
                } else {
                    model.addAttribute("errorMessage", "Admin yetkisi bulunmamaktadır.");
                }
            } else {
                model.addAttribute("errorMessage", "Geçersiz e-posta veya şifre.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Giriş sırasında bir hata oluştu: " + e.getMessage());
        }
        
        model.addAttribute("title", "Admin Girişi");
        return "admin/login";
    }

    /**
     * Admin çıkış işlemi
     * GET /admin/logout
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("infoMessage", "Başarıyla çıkış yaptınız.");
        return "redirect:/";
    }
}