package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Favori;
import com.alperen.kitapsatissistemi.service.FavoriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * AdminFavorilerController - .NET C# projesindeki AdminFavorilerController'dan dönüştürülmüştür
 */
@Controller
@RequestMapping("/admin/favoriler")
public class AdminFavorilerController {

    @Autowired
    private FavoriService favoriService;

    /**
     * Admin favoriler listesi
     * GET /admin/favoriler
     */
    @GetMapping
    public String index(Model model, HttpSession session,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "id") String sortBy,
                       @RequestParam(defaultValue = "desc") String sortDir) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by(direction, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Favori> favoriPage = favoriService.findAll(pageable);
            
            model.addAttribute("favoriler", favoriPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", favoriPage.getTotalPages());
            model.addAttribute("totalElements", favoriPage.getTotalElements());
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Favoriler yüklenirken hata oluştu: " + e.getMessage());
        }
        
        model.addAttribute("title", "Favori Yönetimi");
        return "admin/favoriler/index";
    }

    /**
     * Favori silme
     * POST /admin/favoriler/delete/{id}
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            favoriService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Favori başarıyla silindi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Favori silinirken hata oluştu: " + e.getMessage());
        }
        
        return "redirect:/admin/favoriler";
    }

    /**
     * Kullanıcının tüm favorilerini silme
     * POST /admin/favoriler/delete-by-user/{kullaniciId}
     */
    @PostMapping("/delete-by-user/{kullaniciId}")
    public String deleteByUser(@PathVariable Long kullaniciId, HttpSession session, RedirectAttributes redirectAttributes) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            favoriService.deleteByKullaniciId(kullaniciId);
            redirectAttributes.addFlashAttribute("successMessage", "Kullanıcının tüm favorileri başarıyla silindi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Favoriler silinirken hata oluştu: " + e.getMessage());
        }
        
        return "redirect:/admin/favoriler";
    }

    /**
     * Kitabın tüm favorilerini silme
     * POST /admin/favoriler/delete-by-book/{kitapId}
     */
    @PostMapping("/delete-by-book/{kitapId}")
    public String deleteByBook(@PathVariable Long kitapId, HttpSession session, RedirectAttributes redirectAttributes) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            favoriService.deleteByKitapId(kitapId);
            redirectAttributes.addFlashAttribute("successMessage", "Kitabın tüm favorileri başarıyla silindi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Favoriler silinirken hata oluştu: " + e.getMessage());
        }
        
        return "redirect:/admin/favoriler";
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