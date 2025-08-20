package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.exception.BusinessException;
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
import java.util.Optional;

/**
 * AdminKategorilerController - .NET C# projesindeki AdminKategorilerController'dan dönüştürülmüştür
 */
@Controller
@RequestMapping("/admin/kategoriler")
public class AdminKategorilerController {

    @Autowired
    private KategoriService kategoriService;

    /**
     * Admin kategoriler listesi
     * GET /admin/kategoriler
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
            
            Page<Kategori> kategoriPage;
            if (search != null && !search.trim().isEmpty()) {
                kategoriPage = kategoriService.findByAdContainingIgnoreCase(search.trim(), pageable);
            } else {
                kategoriPage = kategoriService.findAll(pageable);
            }
            
            model.addAttribute("kategoriler", kategoriPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", kategoriPage.getTotalPages());
            model.addAttribute("totalElements", kategoriPage.getTotalElements());
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("search", search);
            
        } catch (BusinessException e) {
            model.addAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kategoriler yüklenirken hata oluştu: " + e.getMessage());
        }
        
        model.addAttribute("title", "Kategori Yönetimi");
        return "admin/kategoriler/index";
    }

    /**
     * Yeni kategori ekleme sayfası
     * GET /admin/kategoriler/create
     */
    @GetMapping("/create")
    public String create(Model model, HttpSession session) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        model.addAttribute("kategori", new Kategori());
        model.addAttribute("title", "Yeni Kategori Ekle");
        return "admin/kategoriler/create";
    }

    /**
     * Yeni kategori kaydetme
     * POST /admin/kategoriler/create
     */
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute Kategori kategori,
                        BindingResult bindingResult,
                        Model model,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Yeni Kategori Ekle");
            return "admin/kategoriler/create";
        }
        
        try {
            kategoriService.save(kategori);
            redirectAttributes.addFlashAttribute("successMessage", "Kategori başarıyla eklendi.");
            return "redirect:/admin/kategoriler";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kategori eklenirken hata oluştu: " + e.getMessage());
            model.addAttribute("title", "Yeni Kategori Ekle");
            return "admin/kategoriler/create";
        }
    }

    /**
     * Kategori düzenleme sayfası
     * GET /admin/kategoriler/edit/{id}
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, HttpSession session) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            Optional<Kategori> kategoriOpt = kategoriService.findById(id);
            if (kategoriOpt.isPresent()) {
                model.addAttribute("kategori", kategoriOpt.get());
                model.addAttribute("title", "Kategori Düzenle");
                return "admin/kategoriler/edit";
            } else {
                model.addAttribute("errorMessage", "Kategori bulunamadı.");
                return "redirect:/admin/kategoriler";
            }
        } catch (BusinessException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/kategoriler";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kategori yüklenirken hata oluştu: " + e.getMessage());
            return "redirect:/admin/kategoriler";
        }
    }

    /**
     * Kategori güncelleme
     * POST /admin/kategoriler/edit/{id}
     */
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                      @Valid @ModelAttribute Kategori kategori,
                      BindingResult bindingResult,
                      Model model,
                      HttpSession session,
                      RedirectAttributes redirectAttributes) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Kategori Düzenle");
            return "admin/kategoriler/edit";
        }
        
        try {
            kategori.setId(id);
            kategoriService.save(kategori);
            redirectAttributes.addFlashAttribute("successMessage", "Kategori başarıyla güncellendi.");
            return "redirect:/admin/kategoriler";
        } catch (BusinessException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("title", "Kategori Düzenle");
            return "admin/kategoriler/edit";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Kategori güncellenirken hata oluştu: " + e.getMessage());
            model.addAttribute("title", "Kategori Düzenle");
            return "admin/kategoriler/edit";
        }
    }

    /**
     * Kategori silme
     * POST /admin/kategoriler/delete/{id}
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            kategoriService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Kategori başarıyla silindi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Kategori silinirken hata oluştu: " + e.getMessage());
        }
        
        return "redirect:/admin/kategoriler";
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
