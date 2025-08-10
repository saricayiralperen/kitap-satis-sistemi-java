package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.service.KullaniciService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

/**
 * AdminKullanicilarController - .NET C# projesindeki AdminKullanicilarController'dan dönüştürülmüştür
 */
@Controller
@RequestMapping("/admin/kullanicilar")
public class AdminKullanicilarController {

    @Autowired
    private KullaniciService kullaniciService;

    /**
     * Admin kullanıcılar listesi
     * GET /admin/kullanicilar
     */
    @GetMapping
    public String index(Model model, HttpSession session,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "adSoyad") String sortBy,
                       @RequestParam(defaultValue = "asc") String sortDir,
                       @RequestParam(required = false) String search) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by(direction, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Kullanici> kullaniciPage;
            if (search != null && !search.trim().isEmpty()) {
                kullaniciPage = kullaniciService.findByAdSoyadContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    search.trim(), search.trim(), pageable);
            } else {
                kullaniciPage = kullaniciService.findAll(pageable);
            }
            
            model.addAttribute("kullanicilar", kullaniciPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", kullaniciPage.getTotalPages());
            model.addAttribute("totalElements", kullaniciPage.getTotalElements());
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("search", search);
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kullanıcılar yüklenirken hata oluştu: " + e.getMessage());
        }
        
        model.addAttribute("title", "Kullanıcı Yönetimi");
        return "admin/kullanicilar/index";
    }

    /**
     * Yeni kullanıcı ekleme sayfası
     * GET /admin/kullanicilar/create
     */
    @GetMapping("/create")
    public String create(Model model, HttpSession session) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        model.addAttribute("kullanici", new Kullanici());
        model.addAttribute("title", "Yeni Kullanıcı Ekle");
        return "admin/kullanicilar/create";
    }

    /**
     * Yeni kullanıcı kaydetme
     * POST /admin/kullanicilar/create
     */
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute Kullanici kullanici,
                        BindingResult bindingResult,
                        Model model,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Yeni Kullanıcı Ekle");
            return "admin/kullanicilar/create";
        }
        
        try {
            // Email kontrolü
            if (kullaniciService.existsByEmail(kullanici.getEmail())) {
                model.addAttribute("errorMessage", "Bu email adresi zaten kullanılmaktadır.");
                model.addAttribute("title", "Yeni Kullanıcı Ekle");
                return "admin/kullanicilar/create";
            }
            
            kullaniciService.save(kullanici);
            redirectAttributes.addFlashAttribute("successMessage", "Kullanıcı başarıyla eklendi.");
            return "redirect:/admin/kullanicilar";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kullanıcı eklenirken hata oluştu: " + e.getMessage());
            model.addAttribute("title", "Yeni Kullanıcı Ekle");
            return "admin/kullanicilar/create";
        }
    }

    /**
     * Kullanıcı düzenleme sayfası
     * GET /admin/kullanicilar/edit/{id}
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, HttpSession session) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            Optional<Kullanici> kullaniciOpt = kullaniciService.findById(id);
            if (kullaniciOpt.isPresent()) {
                Kullanici kullanici = kullaniciOpt.get();
                // Şifre hash'ini temizle (güvenlik için)
                kullanici.setSifreHash(null);
                model.addAttribute("kullanici", kullanici);
                model.addAttribute("title", "Kullanıcı Düzenle");
                return "admin/kullanicilar/edit";
            } else {
                model.addAttribute("errorMessage", "Kullanıcı bulunamadı.");
                return "redirect:/admin/kullanicilar";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kullanıcı yüklenirken hata oluştu: " + e.getMessage());
            return "redirect:/admin/kullanicilar";
        }
    }

    /**
     * Kullanıcı güncelleme
     * POST /admin/kullanicilar/edit/{id}
     */
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                      @Valid @ModelAttribute Kullanici kullanici,
                      BindingResult bindingResult,
                      Model model,
                      HttpSession session,
                      RedirectAttributes redirectAttributes) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Kullanıcı Düzenle");
            return "admin/kullanicilar/edit";
        }
        
        try {
            // Mevcut kullanıcıyı getir
            Optional<Kullanici> mevcutKullaniciOpt = kullaniciService.findById(id);
            if (!mevcutKullaniciOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Kullanıcı bulunamadı.");
                return "redirect:/admin/kullanicilar";
            }
            
            Kullanici mevcutKullanici = mevcutKullaniciOpt.get();
            
            // Email değişmişse ve başka kullanıcı tarafından kullanılıyorsa hata ver
            if (!mevcutKullanici.getEmail().equals(kullanici.getEmail()) && 
                kullaniciService.existsByEmail(kullanici.getEmail())) {
                model.addAttribute("errorMessage", "Bu email adresi zaten kullanılmaktadır.");
                model.addAttribute("title", "Kullanıcı Düzenle");
                return "admin/kullanicilar/edit";
            }
            
            // Şifre değişmemişse eski şifreyi koru
            if (kullanici.getSifreHash() == null || kullanici.getSifreHash().isEmpty()) {
                kullanici.setSifreHash(mevcutKullanici.getSifreHash());
            }
            
            kullanici.setId(id);
            kullaniciService.save(kullanici);
            redirectAttributes.addFlashAttribute("successMessage", "Kullanıcı başarıyla güncellendi.");
            return "redirect:/admin/kullanicilar";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kullanıcı güncellenirken hata oluştu: " + e.getMessage());
            model.addAttribute("title", "Kullanıcı Düzenle");
            return "admin/kullanicilar/edit";
        }
    }

    /**
     * Kullanıcı silme
     * POST /admin/kullanicilar/delete/{id}
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            // Kendi hesabını silmeye çalışıyor mu kontrol et
            Long currentUserId = (Long) session.getAttribute("KullaniciId");
            if (currentUserId != null && currentUserId.equals(id)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Kendi hesabınızı silemezsiniz.");
                return "redirect:/admin/kullanicilar";
            }
            
            kullaniciService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Kullanıcı başarıyla silindi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Kullanıcı silinirken hata oluştu: " + e.getMessage());
        }
        
        return "redirect:/admin/kullanicilar";
    }

    /**
     * Admin kontrolü yardımcı metodu
     */
    private boolean isAdmin(HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("IsAdmin");
        String rol = (String) session.getAttribute("KullaniciRol");
        return (isAdmin != null && isAdmin) || "Admin".equals(rol);
    }
}