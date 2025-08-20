package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Siparis;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.service.SiparisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * SiparislerWebController - Siparişler web sayfaları için controller
 */
@Controller
@RequestMapping("/siparisler")
public class SiparislerWebController {

    @Autowired
    private SiparisService siparisService;

    /**
     * Kullanıcının siparişleri sayfası
     * GET /siparisler
     */
    @GetMapping
    public String index(Model model, HttpSession session) {
        // Kullanıcı giriş kontrolü
        Long kullaniciId = (Long) session.getAttribute("KullaniciId");
        if (kullaniciId == null) {
            return "redirect:/kullanici/login";
        }
        
        try {
            List<Siparis> siparisler = siparisService.getSiparislerByKullaniciIdWithDetails(kullaniciId);
            model.addAttribute("siparisler", siparisler);
            model.addAttribute("title", "Siparişlerim");
        } catch (BusinessException e) {
            model.addAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Siparişler yüklenirken hata oluştu: " + e.getMessage());
        }
        
        return "siparisler/index";
    }

    /**
     * Sipariş detay sayfası
     * GET /siparisler/{id}
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        // Kullanıcı giriş kontrolü
        Long kullaniciId = (Long) session.getAttribute("KullaniciId");
        if (kullaniciId == null) {
            return "redirect:/kullanici/login";
        }
        
        try {
            Optional<Siparis> siparisOpt = siparisService.getSiparisByIdWithDetails(id);
            if (siparisOpt.isPresent()) {
                Siparis siparis = siparisOpt.get();
                // Kullanıcının kendi siparişi mi kontrol et
                if (!siparis.getKullaniciId().equals(kullaniciId)) {
                    return "redirect:/siparisler";
                }
                model.addAttribute("siparis", siparis);
                model.addAttribute("title", "Sipariş Detayı - #" + id);
            } else {
                return "redirect:/siparisler";
            }
        } catch (BusinessException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "siparisler/index";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Sipariş detayları yüklenirken hata oluştu: " + e.getMessage());
            return "siparisler/index";
        }
        
        return "siparisler/detail";
    }
}
