package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.entity.Siparis;
import com.alperen.kitapsatissistemi.service.KullaniciService;
import com.alperen.kitapsatissistemi.service.KitapService;
import com.alperen.kitapsatissistemi.service.KategoriService;
import com.alperen.kitapsatissistemi.service.SiparisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * AdminController - C# projesindeki admin panel'den dönüştürülmüştür
 * Admin dashboard, authentication ve yetkilendirme işlemlerini yönetir
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private KullaniciService kullaniciService;
    
    @Autowired
    private KitapService kitapService;
    
    @Autowired
    private KategoriService kategoriService;
    
    @Autowired
    private SiparisService siparisService;
    
    /**
     * Admin giriş sayfası
     * GET /admin/login
     */
    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model) {
        // Zaten giriş yapmışsa dashboard'a yönlendir
        if (session.getAttribute("adminUser") != null) {
            return "redirect:/admin/dashboard";
        }
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
                       RedirectAttributes redirectAttributes) {
        try {
            Optional<Kullanici> kullanici = kullaniciService.authenticateKullanici(email, sifre);
            
            if (kullanici.isPresent() && "Admin".equals(kullanici.get().getRol())) {
                session.setAttribute("adminUser", kullanici.get());
                return "redirect:/admin/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("error", "Geçersiz admin bilgileri!");
                return "redirect:/admin/login";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Giriş sırasında bir hata oluştu!");
            return "redirect:/admin/login";
        }
    }
    
    /**
     * Admin çıkış işlemi
     * GET /admin/logout
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.removeAttribute("adminUser");
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "Başarıyla çıkış yapıldı!");
        return "redirect:/admin/login";
    }
    
    /**
     * Admin dashboard
     * GET /admin/dashboard
     */
    @GetMapping({"/", "/dashboard"})
    public String dashboard(HttpSession session, Model model) {
        // Admin kontrolü
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            // Dashboard istatistikleri
            long toplamKullanici = kullaniciService.getKullaniciCount();
            long toplamKitap = kitapService.getKitapCount();
            long toplamKategori = kategoriService.getKategoriCount();
            long toplamSiparis = siparisService.getSiparisCount();
            
            // Son eklenen kayıtlar
            List<Kullanici> sonKullanicilar = kullaniciService.getLatestKullanicilar(5);
            List<Kitap> sonKitaplar = kitapService.getLatestKitaplar(5);
            List<Siparis> sonSiparisler = siparisService.getLatestSiparisler(5);
            
            model.addAttribute("toplamKullanici", toplamKullanici);
            model.addAttribute("toplamKitap", toplamKitap);
            model.addAttribute("toplamKategori", toplamKategori);
            model.addAttribute("toplamSiparis", toplamSiparis);
            model.addAttribute("sonKullanicilar", sonKullanicilar);
            model.addAttribute("sonKitaplar", sonKitaplar);
            model.addAttribute("sonSiparisler", sonSiparisler);
            
            return "admin/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Dashboard verileri yüklenirken hata oluştu!");
            return "admin/dashboard";
        }
    }
    
    // Kullanıcı yönetimi AdminKullanicilarController'da yapılıyor
    
    // Kategori yönetimi AdminKategorilerController'da yapılıyor
    
    // Kitap yönetimi AdminKitaplarController'da yapılıyor
    
    // Sipariş yönetimi AdminSiparislerController'da yapılıyor
    
    /**
     * Admin giriş kontrolü
     */
    private boolean isAdminLoggedIn(HttpSession session) {
        Kullanici adminUser = (Kullanici) session.getAttribute("adminUser");
        return adminUser != null && "Admin".equals(adminUser.getRol());
    }
    
    /**
     * Model attribute - her sayfada admin kullanıcı bilgisi
     */
    @ModelAttribute("adminUser")
    public Kullanici getAdminUser(HttpSession session) {
        return (Kullanici) session.getAttribute("adminUser");
    }
}