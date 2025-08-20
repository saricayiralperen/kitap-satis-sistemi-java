package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.entity.Siparis;
import com.alperen.kitapsatissistemi.service.KullaniciService;
import com.alperen.kitapsatissistemi.service.SiparisService;
import com.alperen.kitapsatissistemi.service.SecurityAuditService;
import com.alperen.kitapsatissistemi.util.InputSanitizer;
import com.alperen.kitapsatissistemi.dto.LoginRequest;
import com.alperen.kitapsatissistemi.dto.RegisterRequest;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.exception.DuplicateEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

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
    
    @Autowired
    private SecurityAuditService securityAuditService;
    
    @Autowired
    private InputSanitizer inputSanitizer;

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
    public String login(@Valid @ModelAttribute LoginRequest loginRequest,
                       BindingResult bindingResult,
                       HttpSession session,
                        RedirectAttributes redirectAttributes,
                        Model model,
                        HttpServletRequest request) {
        String ipAddress = securityAuditService.getClientIpAddress(request);
        String userAgent = securityAuditService.getUserAgent(request);
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Kullanıcı Girişi");
            return "kullanici/login";
        }
        
        try {
            Optional<Kullanici> kullaniciOpt = kullaniciService.authenticateKullanici(loginRequest.getEmail(), loginRequest.getSifre());
            
            if (kullaniciOpt.isPresent()) {
                Kullanici kullanici = kullaniciOpt.get();
                
                // Session'a kullanıcı bilgilerini kaydet
                session.setAttribute("IsLoggedIn", true);
                session.setAttribute("KullaniciId", kullanici.getId());
                session.setAttribute("KullaniciAd", kullanici.getAdSoyad());
                session.setAttribute("KullaniciEmail", kullanici.getEmail());
                session.setAttribute("KullaniciRol", kullanici.getRol());
                
                // Spring Security authentication köprüsü (rolleri SecurityContext'e aktar)
                // ROLE_ prefix'i gereklidir; veritabanındaki roller (Admin/User) buna göre dönüştürülür
                String roleUpper = kullanici.getRol() != null ? kullanici.getRol().toUpperCase() : "USER";
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleUpper);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        kullanici.getEmail(),
                        null,
                        java.util.Collections.singletonList(authority)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                securityAuditService.logSuccessfulLogin(loginRequest.getEmail(), ipAddress, userAgent);
                redirectAttributes.addFlashAttribute("successMessage", "Başarıyla giriş yaptınız!");
                
                // Return URL varsa oraya yönlendir
                String returnUrl = (String) redirectAttributes.getFlashAttributes().get("returnUrl");
                if (returnUrl != null && !returnUrl.isEmpty()) {
                    return "redirect:" + returnUrl;
                }
                
                return "redirect:/";
            } else {
                securityAuditService.logFailedLogin(loginRequest.getEmail(), ipAddress, userAgent, "Invalid credentials");
                model.addAttribute("errorMessage", "Email veya şifre hatalı.");
            }
        } catch (BusinessException e) {
            model.addAttribute("errorMessage", e.getMessage());
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
    public String register(@Valid @ModelAttribute RegisterRequest registerRequest,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model,
                          HttpServletRequest request) {
        String ipAddress = securityAuditService.getClientIpAddress(request);
        String userAgent = securityAuditService.getUserAgent(request);
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Kullanıcı Kaydı");
            return "kullanici/register";
        }
        
        // Input sanitization kontrolü
        if (!inputSanitizer.isSafeInput(registerRequest.getAdSoyad()) || 
            !inputSanitizer.isSafeInput(registerRequest.getEmail()) ||
            !inputSanitizer.isSafeInput(registerRequest.getSifre())) {
            securityAuditService.logSuspiciousInput(ipAddress, registerRequest.getEmail(), "/kullanici/register", userAgent);
            model.addAttribute("errorMessage", "Geçersiz kayıt verisi.");
            model.addAttribute("title", "Kullanıcı Kaydı");
            return "kullanici/register";
        }
        
        try {
            // Şifre kontrolü
            if (!registerRequest.isPasswordMatching()) {
                model.addAttribute("errorMessage", "Şifreler eşleşmiyor.");
                model.addAttribute("title", "Kullanıcı Kaydı");
                return "kullanici/register";
            }
            
            // Email kontrolü
            if (kullaniciService.existsByEmail(registerRequest.getEmail())) {
                model.addAttribute("errorMessage", "Bu email adresi zaten kayıtlı.");
                model.addAttribute("title", "Kullanıcı Kaydı");
                return "kullanici/register";
            }
            
            // Yeni kullanıcı oluştur
            Kullanici kullanici = new Kullanici();
            kullanici.setAdSoyad(inputSanitizer.sanitizeAlphanumeric(registerRequest.getAdSoyad()));
            kullanici.setEmail(inputSanitizer.sanitizeEmail(registerRequest.getEmail()));
            kullanici.setRol("User");
            
            kullaniciService.registerKullanici(kullanici, registerRequest.getSifre());
            
            securityAuditService.logUserRegistration(registerRequest.getEmail(), ipAddress, userAgent);
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
