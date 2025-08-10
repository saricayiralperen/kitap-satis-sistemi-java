package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.entity.Siparis;
import com.alperen.kitapsatissistemi.service.KullaniciService;
import com.alperen.kitapsatissistemi.service.SiparisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * KullaniciWebController - Kullanıcı web sayfaları için controller
 * .NET C# projesindeki KullaniciController'dan dönüştürülmüştür
 */
@Controller
@RequestMapping("/kullanici")
public class KullaniciWebController {

    @Autowired
    private KullaniciService kullaniciService;
    
    @Autowired
    private SiparisService siparisService;

    /**
     * Kullanıcı giriş sayfası
     * GET /kullanici/login
     */
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Kullanıcı Girişi");
        return "kullanici/login";
    }

    /**
     * Kullanıcı giriş işlemi
     * POST /kullanici/login
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
                
                // Session bilgilerini set et
                session.setAttribute("KullaniciId", kullanici.getId());
                session.setAttribute("KullaniciAd", kullanici.getAdSoyad());
                session.setAttribute("KullaniciEmail", kullanici.getEmail());
                session.setAttribute("KullaniciRol", kullanici.getRol());
                session.setAttribute("IsLoggedIn", true);
                
                redirectAttributes.addFlashAttribute("successMessage", "Başarıyla giriş yaptınız!");
                return "redirect:/";
            } else {
                model.addAttribute("errorMessage", "Email veya şifre hatalı.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Giriş sırasında bir hata oluştu: " + e.getMessage());
        }
        
        model.addAttribute("title", "Kullanıcı Girişi");
        return "kullanici/login";
    }

    /**
     * Kullanıcı kayıt sayfası
     * GET /kullanici/register
     */
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("kullanici", new Kullanici());
        model.addAttribute("title", "Kullanıcı Kaydı");
        return "kullanici/register";
    }

    /**
     * Kullanıcı kayıt işlemi
     * POST /kullanici/register
     */
    @PostMapping("/register")
    public String register(@RequestParam String adSoyad,
                          @RequestParam String email,
                          @RequestParam String sifre,
                          @RequestParam String sifreTekrar,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        try {
            // Şifre kontrolü
            if (!sifre.equals(sifreTekrar)) {
                model.addAttribute("errorMessage", "Şifreler eşleşmiyor.");
                model.addAttribute("title", "Kullanıcı Kaydı");
                return "kullanici/register";
            }
            
            // Email kontrolü
            if (kullaniciService.existsByEmail(email)) {
                model.addAttribute("errorMessage", "Bu email adresi zaten kayıtlı.");
                model.addAttribute("title", "Kullanıcı Kaydı");
                return "kullanici/register";
            }
            
            // Yeni kullanıcı oluştur
            Kullanici kullanici = new Kullanici();
            kullanici.setAdSoyad(adSoyad);
            kullanici.setEmail(email);
            kullanici.setRol("User");
            
            kullaniciService.registerKullanici(kullanici, sifre);
            
            redirectAttributes.addFlashAttribute("successMessage", "Kayıt başarılı! Şimdi giriş yapabilirsiniz.");
            return "redirect:/kullanici/login";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kayıt sırasında bir hata oluştu: " + e.getMessage());
        }
        
        model.addAttribute("title", "Kullanıcı Kaydı");
        return "kullanici/register";
    }

    /**
     * Kullanıcı profil sayfası
     * GET /kullanici/profil
     */
    @GetMapping("/profil")
    public String profil(Model model, HttpSession session) {
        // Kullanıcı giriş kontrolü
        Long kullaniciId = (Long) session.getAttribute("KullaniciId");
        if (kullaniciId == null) {
            return "redirect:/kullanici/login";
        }
        
        try {
            Optional<Kullanici> kullaniciOpt = kullaniciService.getKullaniciById(kullaniciId);
            if (kullaniciOpt.isPresent()) {
                model.addAttribute("kullanici", kullaniciOpt.get());
                model.addAttribute("title", "Profilim");
            } else {
                return "redirect:/kullanici/login";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Profil bilgileri yüklenirken hata oluştu: " + e.getMessage());
        }
        
        return "kullanici/profil";
    }

    /**
     * Kullanıcı çıkış işlemi
     * GET /kullanici/logout
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "Başarıyla çıkış yaptınız.");
        return "redirect:/";
    }
}