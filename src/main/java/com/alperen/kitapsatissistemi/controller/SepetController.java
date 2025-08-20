package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.entity.SepetItem;
import com.alperen.kitapsatissistemi.entity.SiparisDetay;
import com.alperen.kitapsatissistemi.entity.Siparis;
import com.alperen.kitapsatissistemi.service.KitapService;
import com.alperen.kitapsatissistemi.service.SiparisService;
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
    
    @Autowired
    private SiparisService siparisService;

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
            // Kullanıcı giriş kontrolü
            Boolean isLoggedIn = (Boolean) session.getAttribute("IsLoggedIn");
            
            if (isLoggedIn == null || !isLoggedIn) {
                // Kullanıcı giriş yapmamış - giriş sayfasına yönlendir
                redirectAttributes.addFlashAttribute("errorMessage", "Sepete ürün eklemek için önce giriş yapmalısınız!");
                redirectAttributes.addFlashAttribute("returnUrl", request.getHeader("Referer"));
                return "redirect:/kullanici/login";
            }

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
            redirectAttributes.addFlashAttribute("errorMessage", "Sepete eklenirken bir hata oluştu: " + e.getMessage());
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
    public String siparisiTamamlaOnay(@RequestParam String il,
                                     @RequestParam String ilce,
                                     @RequestParam String adres,
                                     @RequestParam(required = false) String postaKodu,
                                     @RequestParam String telefon,
                                     @RequestParam String kartSahibi,
                                     @RequestParam String kartNumarasi,
                                     @RequestParam String sonKullanma,
                                     @RequestParam String cvv,
                                     @RequestParam(required = false) String sozlesme,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        List<SepetItem> sepet = getSepetFromSession(session);
        
        if (sepet.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sepetiniz boş!");
            return "redirect:/sepet";
        }

        // Kullanıcı giriş kontrolü
        Boolean isLoggedIn = (Boolean) session.getAttribute("IsLoggedIn");
        Long kullaniciId = (Long) session.getAttribute("KullaniciId");
        String kullaniciAd = (String) session.getAttribute("KullaniciAd");
        
        // Debug için session bilgilerini logla
        System.out.println("=== SESSION DEBUG ===");
        System.out.println("Session ID: " + session.getId());
        System.out.println("IsLoggedIn: " + isLoggedIn);
        System.out.println("KullaniciId: " + kullaniciId);
        System.out.println("KullaniciAd: " + kullaniciAd);
        System.out.println("Session Max Inactive Interval: " + session.getMaxInactiveInterval());
        System.out.println("Session Creation Time: " + session.getCreationTime());
        System.out.println("Session Last Accessed Time: " + session.getLastAccessedTime());
        System.out.println("====================");
        
        if (isLoggedIn == null || !isLoggedIn) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sipariş vermek için giriş yapmalısınız!");
            return "redirect:/kullanici/login";
        }

        // Sözleşme kabul kontrolü
        if (sozlesme == null || !"on".equals(sozlesme)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Kullanım şartlarını kabul etmelisiniz!");
            return "redirect:/sepet/siparis-tamamla";
        }

        try {
            // Sipariş detaylarını hazırla
            List<SiparisDetay> siparisDetaylari = new ArrayList<>();
            for (SepetItem item : sepet) {
                // Kitap nesnesini al
                Optional<Kitap> kitapOpt = kitapService.getKitapById((long) item.getKitapId());
                if (kitapOpt.isPresent()) {
                    SiparisDetay detay = new SiparisDetay();
                    detay.setKitap(kitapOpt.get());
                    detay.setAdet(item.getAdet());
                    detay.setFiyat(item.getFiyat());
                    siparisDetaylari.add(detay);
                } else {
                    throw new RuntimeException("Kitap bulunamadı: " + item.getKitapId());
                }
            }
            
            // Siparişi kaydet
            Siparis yeniSiparis = siparisService.createSiparis(kullaniciId, siparisDetaylari);
            
            System.out.println("=== SİPARİŞ KAYDED İLDİ ===" );
            System.out.println("Sipariş ID: " + yeniSiparis.getId());
            System.out.println("Kullanıcı ID: " + yeniSiparis.getKullaniciId());
            System.out.println("Toplam Tutar: " + yeniSiparis.getToplamTutar());
            System.out.println("Durum: " + yeniSiparis.getDurum());
            System.out.println("=========================");
            
            // Sepeti temizle
            saveSepetToSession(session, new ArrayList<>());
            
            redirectAttributes.addFlashAttribute("successMessage", "Siparişiniz başarıyla tamamlandı! Sipariş No: " + yeniSiparis.getId() + " - Teslimat adresi: " + il + ", " + ilce);
            return "redirect:/sepet";
            
        } catch (Exception e) {
            System.out.println("=== SİPARİŞ KAYDETME HATASI ===" );
            System.out.println("Hata: " + e.getMessage());
            e.printStackTrace();
            System.out.println("===============================");
            
            redirectAttributes.addFlashAttribute("errorMessage", "Sipariş kaydedilirken bir hata oluştu: " + e.getMessage());
            return "redirect:/sepet/siparis-tamamla";
        }
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
