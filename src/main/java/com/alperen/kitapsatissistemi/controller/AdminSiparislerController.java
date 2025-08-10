package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Siparis;
import com.alperen.kitapsatissistemi.service.SiparisService;
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
import java.util.Optional;

/**
 * AdminSiparislerController - .NET C# projesindeki AdminSiparislerController'dan dönüştürülmüştür
 */
@Controller
@RequestMapping("/admin/siparisler")
public class AdminSiparislerController {

    @Autowired
    private SiparisService siparisService;

    /**
     * Admin siparişler listesi
     * GET /admin/siparisler
     */
    @GetMapping
    public String index(Model model, HttpSession session,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "siparisTarihi") String sortBy,
                       @RequestParam(defaultValue = "desc") String sortDir,
                       @RequestParam(required = false) String durum) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by(direction, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Siparis> siparisPage;
            if (durum != null && !durum.trim().isEmpty()) {
                siparisPage = siparisService.findByDurum(durum.trim(), pageable);
            } else {
                siparisPage = siparisService.findAll(pageable);
            }
            
            model.addAttribute("siparisler", siparisPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", siparisPage.getTotalPages());
            model.addAttribute("totalElements", siparisPage.getTotalElements());
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("durum", durum);
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Siparişler yüklenirken hata oluştu: " + e.getMessage());
        }
        
        model.addAttribute("title", "Sipariş Yönetimi");
        return "admin/siparisler/index";
    }

    /**
     * Sipariş detayları
     * GET /admin/siparisler/details/{id}
     */
    @GetMapping("/details/{id}")
    public String details(@PathVariable Long id, Model model, HttpSession session) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            Optional<Siparis> siparisOpt = siparisService.findById(id);
            if (siparisOpt.isPresent()) {
                model.addAttribute("siparis", siparisOpt.get());
                model.addAttribute("title", "Sipariş Detayları");
                return "admin/siparisler/details";
            } else {
                model.addAttribute("errorMessage", "Sipariş bulunamadı.");
                return "redirect:/admin/siparisler";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Sipariş yüklenirken hata oluştu: " + e.getMessage());
            return "redirect:/admin/siparisler";
        }
    }

    /**
     * Sipariş durumu güncelleme
     * POST /admin/siparisler/update-status/{id}
     */
    @PostMapping("/update-status/{id}")
    public String updateStatus(@PathVariable Long id,
                              @RequestParam String durum,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            Optional<Siparis> siparisOpt = siparisService.findById(id);
            if (siparisOpt.isPresent()) {
                Siparis siparis = siparisOpt.get();
                siparis.setDurum(durum);
                siparisService.save(siparis);
                redirectAttributes.addFlashAttribute("successMessage", "Sipariş durumu başarıyla güncellendi.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Sipariş bulunamadı.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sipariş durumu güncellenirken hata oluştu: " + e.getMessage());
        }
        
        return "redirect:/admin/siparisler";
    }

    /**
     * Sipariş silme
     * POST /admin/siparisler/delete/{id}
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            siparisService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Sipariş başarıyla silindi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sipariş silinirken hata oluştu: " + e.getMessage());
        }
        
        return "redirect:/admin/siparisler";
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