package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.entity.SepetItem;
import com.alperen.kitapsatissistemi.service.KitapService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/sepet")
public class SepetController {

    @Autowired
    private KitapService kitapService;

    private static final String SEPET_SESSION_KEY = "Sepet";
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Sepetim sayfasını gösterecek metot
    @GetMapping
    public String index(Model model, HttpSession session) {
        List<SepetItem> sepet = getSepetFromSession(session);
        model.addAttribute("sepetItems", sepet);
        return "sepet/index";
    }

    // Sepete ürün ekleme
    @PostMapping("/ekle")
    public String sepeteEkle(@RequestParam int kitapId, 
                           @RequestParam(defaultValue = "1") int adet,
                           HttpSession session,
                           RedirectAttributes redirectAttributes,
                           HttpServletRequest request) {

        try {
            // Kitabı getir
            Optional<Kitap> kitapOpt = kitapService.findById((long) kitapId);
            if (!kitapOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Kitap bulunamadı!");
                return "redirect:" + request.getHeader("Referer");
            }

            Kitap kitap = kitapOpt.get();

            // Sepeti session'dan al
            List<SepetItem> sepet = getSepetFromSession(session);

            // Kitap zaten sepette mi kontrol et
            Optional<SepetItem> mevcutItem = sepet.stream()
                    .filter(item -> item.getKitapId() == kitapId)
                    .findFirst();

            if (mevcutItem.isPresent()) {
                // Adetini artır
                mevcutItem.get().setAdet(mevcutItem.get().getAdet() + adet);
            } else {
                // Yeni SepetItem oluştur
                SepetItem yeniItem = new SepetItem(
                        kitapId,
                        kitap.getAd(),
                        kitap.getFiyat(),
                        adet,
                        kitap.getResimUrl()
                );
                sepet.add(yeniItem);
            }

            // Sepeti session'a geri kaydet
            saveSepetToSession(session, sepet);

            redirectAttributes.addFlashAttribute("successMessage", kitap.getAd() + " sepete eklendi!");
            return "redirect:" + request.getHeader("Referer");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sepete eklenirken bir hata oluştu!");
            return "redirect:" + request.getHeader("Referer");
        }
    }

    // Sepetteki toplam ürün sayısını dönecek metot (AJAX çağrıları için)
    @GetMapping("/count")
    @ResponseBody
    public int getSepetItemCount(HttpSession session) {
        List<SepetItem> sepet = getSepetFromSession(session);
        return sepet.stream().mapToInt(SepetItem::getAdet).sum();
    }

    // Sepetten ürün silme metodu
    @PostMapping("/sil")
    public String sepettenSil(@RequestParam int kitapId, 
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        List<SepetItem> sepet = getSepetFromSession(session);
        
        Optional<SepetItem> silinecekItem = sepet.stream()
                .filter(item -> item.getKitapId() == kitapId)
                .findFirst();

        if (silinecekItem.isPresent()) {
            sepet.remove(silinecekItem.get());
            saveSepetToSession(session, sepet);
            redirectAttributes.addFlashAttribute("infoMessage", 
                    silinecekItem.get().getKitapAd() + " sepetten kaldırıldı.");
        }
        
        return "redirect:/sepet";
    }

    // Sepetteki ürün adedini güncelleme metodu
    @PostMapping("/adet-guncelle")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> adetGuncelle(@RequestParam int kitapId, 
                                                           @RequestParam int adet,
                                                           HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<SepetItem> sepet = getSepetFromSession(session);
            
            Optional<SepetItem> guncellenecekItem = sepet.stream()
                    .filter(item -> item.getKitapId() == kitapId)
                    .findFirst();

            if (guncellenecekItem.isPresent()) {
                // Adet minimum 1 olmalı
                if (adet <= 0) {
                    adet = 1;
                }
                
                guncellenecekItem.get().setAdet(adet);
                saveSepetToSession(session, sepet);
                
                response.put("success", true);
                response.put("message", guncellenecekItem.get().getKitapAd() + " adedi " + adet + " olarak güncellendi.");
                response.put("yeniAdet", adet);
            } else {
                response.put("success", false);
                response.put("message", "Ürün bulunamadı.");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Güncelleme sırasında hata oluştu.");
        }
        
        return ResponseEntity.ok(response);
    }

    // Siparişi Tamamla (GET)
    @GetMapping("/siparis-tamamla")
    public String siparisiTamamla(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        List<SepetItem> sepet = getSepetFromSession(session);
        
        if (sepet.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sepetiniz boş!");
            return "redirect:/sepet";
        }
        
        model.addAttribute("sepetItems", sepet);
        return "sepet/siparis-tamamla";
    }

    // Siparişi Tamamla (POST)
    @PostMapping("/siparis-tamamla")
    public String siparisiTamamlaOnay(@RequestParam String adSoyad,
                                     @RequestParam String email,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        List<SepetItem> sepet = getSepetFromSession(session);
        
        if (sepet.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sepetiniz boş!");
            return "redirect:/sepet";
        }

        // Burada sipariş kaydetme işlemi yapılacak
        // Şimdilik sadece sepeti temizleyip başarı mesajı gösteriyoruz
        
        // Sepeti temizle
        saveSepetToSession(session, new ArrayList<>());
        
        redirectAttributes.addFlashAttribute("successMessage", "Siparişiniz başarıyla tamamlandı!");
        return "redirect:/sepet";
    }

    // Sepet içeriğini Session'dan okuyan yardımcı metot
    private List<SepetItem> getSepetFromSession(HttpSession session) {
        try {
            String sepetJson = (String) session.getAttribute(SEPET_SESSION_KEY);
            if (sepetJson == null || sepetJson.isEmpty()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(sepetJson, new TypeReference<List<SepetItem>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // Sepet içeriğini Session'a yazan yardımcı metot
    private void saveSepetToSession(HttpSession session, List<SepetItem> sepet) {
        try {
            String sepetJson = objectMapper.writeValueAsString(sepet);
            session.setAttribute(SEPET_SESSION_KEY, sepetJson);
        } catch (Exception e) {
            // Hata durumunda boş sepet kaydet
            session.setAttribute(SEPET_SESSION_KEY, "[]");
        }
    }
}