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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * AdminKitaplarController - .NET C# projesindeki AdminKitaplarController'dan dönüştürülmüştür
 */
@Controller
@RequestMapping("/admin/kitaplar")
public class AdminKitaplarController {

    @Autowired
    private KitapService kitapService;
    
    @Autowired
    private KategoriService kategoriService;

    /**
     * Admin kitaplar listesi
     * GET /admin/kitaplar
     */
    @GetMapping
    public String index(Model model, HttpSession session,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "ad") String sortBy,
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
            
            Page<Kitap> kitapPage;
            if (search != null && !search.trim().isEmpty()) {
                kitapPage = kitapService.findByAdContainingIgnoreCase(search.trim(), pageable);
            } else {
                kitapPage = kitapService.findAll(pageable);
            }
            
            model.addAttribute("kitaplar", kitapPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", kitapPage.getTotalPages());
            model.addAttribute("totalElements", kitapPage.getTotalElements());
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("search", search);
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kitaplar yüklenirken hata oluştu: " + e.getMessage());
        }
        
        model.addAttribute("title", "Kitap Yönetimi");
        return "admin/kitaplar/index";
    }

    /**
     * Yeni kitap ekleme sayfası
     * GET /admin/kitaplar/create
     */
    @GetMapping("/create")
    public String create(Model model, HttpSession session) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            List<Kategori> kategoriler = kategoriService.findAll();
            model.addAttribute("kategoriler", kategoriler);
            model.addAttribute("kitap", new Kitap());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kategoriler yüklenirken hata oluştu: " + e.getMessage());
        }
        
        model.addAttribute("title", "Yeni Kitap Ekle");
        return "admin/kitaplar/create";
    }

    /**
     * Yeni kitap kaydetme
     * POST /admin/kitaplar/create
     */
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute Kitap kitap,
                        BindingResult bindingResult,
                        Model model,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        if (bindingResult.hasErrors()) {
            try {
                List<Kategori> kategoriler = kategoriService.findAll();
                model.addAttribute("kategoriler", kategoriler);
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Kategoriler yüklenirken hata oluştu: " + e.getMessage());
            }
            model.addAttribute("title", "Yeni Kitap Ekle");
            return "admin/kitaplar/create";
        }
        
        try {
            kitapService.save(kitap);
            redirectAttributes.addFlashAttribute("successMessage", "Kitap başarıyla eklendi.");
            return "redirect:/admin/kitaplar";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kitap eklenirken hata oluştu: " + e.getMessage());
            try {
                List<Kategori> kategoriler = kategoriService.findAll();
                model.addAttribute("kategoriler", kategoriler);
            } catch (Exception ex) {
                model.addAttribute("errorMessage", "Kategoriler yüklenirken hata oluştu: " + ex.getMessage());
            }
            model.addAttribute("title", "Yeni Kitap Ekle");
            return "admin/kitaplar/create";
        }
    }

    /**
     * Kitap düzenleme sayfası
     * GET /admin/kitaplar/edit/{id}
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, HttpSession session) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            Optional<Kitap> kitapOpt = kitapService.findById(id);
            if (kitapOpt.isPresent()) {
                List<Kategori> kategoriler = kategoriService.findAll();
                model.addAttribute("kategoriler", kategoriler);
                model.addAttribute("kitap", kitapOpt.get());
                model.addAttribute("title", "Kitap Düzenle");
                return "admin/kitaplar/edit";
            } else {
                model.addAttribute("errorMessage", "Kitap bulunamadı.");
                return "redirect:/admin/kitaplar";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kitap yüklenirken hata oluştu: " + e.getMessage());
            return "redirect:/admin/kitaplar";
        }
    }

    /**
     * Kitap güncelleme
     * POST /admin/kitaplar/edit/{id}
     */
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                      @Valid @ModelAttribute Kitap kitap,
                      BindingResult bindingResult,
                      Model model,
                      HttpSession session,
                      RedirectAttributes redirectAttributes) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        if (bindingResult.hasErrors()) {
            try {
                List<Kategori> kategoriler = kategoriService.findAll();
                model.addAttribute("kategoriler", kategoriler);
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Kategoriler yüklenirken hata oluştu: " + e.getMessage());
            }
            model.addAttribute("title", "Kitap Düzenle");
            return "admin/kitaplar/edit";
        }
        
        try {
            kitap.setId(id);
            kitapService.save(kitap);
            redirectAttributes.addFlashAttribute("successMessage", "Kitap başarıyla güncellendi.");
            return "redirect:/admin/kitaplar";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kitap güncellenirken hata oluştu: " + e.getMessage());
            try {
                List<Kategori> kategoriler = kategoriService.findAll();
                model.addAttribute("kategoriler", kategoriler);
            } catch (Exception ex) {
                model.addAttribute("errorMessage", "Kategoriler yüklenirken hata oluştu: " + ex.getMessage());
            }
            model.addAttribute("title", "Kitap Düzenle");
            return "admin/kitaplar/edit";
        }
    }

    /**
     * Kitap silme
     * POST /admin/kitaplar/delete/{id}
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            kitapService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Kitap başarıyla silindi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Kitap silinirken hata oluştu: " + e.getMessage());
        }
        
        return "redirect:/admin/kitaplar";
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