package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.*;
import com.alperen.kitapsatissistemi.service.*;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AdminDashboardController - .NET C# projesindeki AdminDashboardController'dan dönüştürülmüştür
 */
@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private KitapService kitapService;
    
    @Autowired
    private KategoriService kategoriService;
    
    @Autowired
    private KullaniciService kullaniciService;
    
    @Autowired
    private SiparisService siparisService;

    /**
     * Admin dashboard ana sayfası
     * GET /admin/dashboard
     */
    @GetMapping("/dashboard")
    public String index(Model model, HttpSession session) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            // Temel istatistikler
            long totalBooks = kitapService.count();
            long totalCategories = kategoriService.count();
            long totalUsers = kullaniciService.count();
            long totalOrders = siparisService.count();
            
            // Bu ay sipariş sayısı
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            long thisMonthOrders = siparisService.getSiparislerAfterDate(startOfMonth).size();
            
            // Kategori dağılımını hesapla
            Map<String, Integer> kategoriDagilimi = new LinkedHashMap<>();
            List<Kategori> kategoriler = kategoriService.getAllKategoriler();
            for (Kategori kategori : kategoriler) {
                long kitapSayisi = kitapService.getKitapCountByKategoriId(kategori.getId());
                kategoriDagilimi.put(kategori.getAd(), (int) kitapSayisi);
            }
            
            // Aylık sipariş verilerini hesapla (son 6 ay)
            Map<String, Integer> aylikSiparisler = new LinkedHashMap<>();
            for (int i = 5; i >= 0; i--) {
                LocalDateTime ay = now.minusMonths(i);
                String ayAdi = ay.getMonth().getDisplayName(TextStyle.FULL, new Locale("tr", "TR"));
                
                LocalDateTime ayBaslangic = ay.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                LocalDateTime ayBitis = ay.withDayOfMonth(ay.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
                
                List<Siparis> aySiparisleri = siparisService.getSiparislerAfterDate(ayBaslangic)
                    .stream()
                    .filter(s -> s.getSiparisTarihi().isBefore(ayBitis))
                    .collect(Collectors.toList());
                
                aylikSiparisler.put(ayAdi, aySiparisleri.size());
            }
            
            // Son siparişler (son 5)
            List<Siparis> sonSiparisler = siparisService.getAllSiparislerWithDetails()
                .stream()
                .sorted((s1, s2) -> s2.getSiparisTarihi().compareTo(s1.getSiparisTarihi()))
                .limit(5)
                .collect(Collectors.toList());
            
            // Popüler kitaplar (ilk 5)
            List<Kitap> populerKitaplar = kitapService.getAllKitaplarWithKategori()
                .stream()
                .limit(5)
                .collect(Collectors.toList());
            
            // Grafik verileri için JavaScript array formatında hazırla
            List<String> aylikSiparislerLabels = new ArrayList<>(aylikSiparisler.keySet());
            List<Integer> aylikSiparislerValues = new ArrayList<>(aylikSiparisler.values());
            
            List<String> kategoriLabels = new ArrayList<>(kategoriDagilimi.keySet());
            List<Integer> kategoriValues = new ArrayList<>(kategoriDagilimi.values());
            
            // Model'e ekle
            model.addAttribute("totalBooks", totalBooks);
            model.addAttribute("totalCategories", totalCategories);
            model.addAttribute("totalUsers", totalUsers);
            model.addAttribute("totalOrders", totalOrders);
            model.addAttribute("thisMonthOrders", thisMonthOrders);
            model.addAttribute("kategoriDagilimi", kategoriDagilimi);
            model.addAttribute("aylikSiparisler", aylikSiparisler);
            model.addAttribute("sonSiparisler", sonSiparisler);
            model.addAttribute("populerKitaplar", populerKitaplar);
            
            // Grafik verileri
            model.addAttribute("aylikSiparislerLabels", aylikSiparislerLabels);
            model.addAttribute("aylikSiparislerValues", aylikSiparislerValues);
            model.addAttribute("kategoriLabels", kategoriLabels);
            model.addAttribute("kategoriValues", kategoriValues);
            
        } catch (BusinessException e) {
            model.addAttribute("errorMessage", "İş kuralı hatası: " + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "İstatistikler yüklenirken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
        
        model.addAttribute("title", "Admin Dashboard");
        return "admin/dashboard";
    }

    /**
     * Admin raporlar sayfası
     * GET /admin/raporlar
     */
    @GetMapping("/raporlar")
    public String raporlar(Model model, HttpSession session) {
        // Admin kontrolü
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        
        try {
            // API'den verileri çek
            List<Kitap> kitaplar = kitapService.getAllKitaplarWithKategori();
            List<Kullanici> kullanicilar = kullaniciService.getAllKullanicilar();
            List<Kategori> kategoriler = kategoriService.getAllKategoriler();
            List<Siparis> siparisler = siparisService.getAllSiparislerWithDetails();
            
            // Rapor verilerini hesapla
            long toplamKitap = kitaplar.size();
            long toplamKullanici = kullanicilar.size();
            long toplamKategori = kategoriler.size();
            
            LocalDateTime now = LocalDateTime.now();
            long buAySiparis = siparisler.stream()
                .filter(s -> s.getSiparisTarihi().getMonth() == now.getMonth() && 
                           s.getSiparisTarihi().getYear() == now.getYear())
                .count();
            
            // Son siparişler (son 5)
            List<Siparis> sonSiparisler = siparisler.stream()
                .sorted((s1, s2) -> s2.getSiparisTarihi().compareTo(s1.getSiparisTarihi()))
                .limit(5)
                .collect(Collectors.toList());
            
            // Popüler kitaplar (ilk 5 - daha sonra sipariş verilerine göre güncellenebilir)
            List<Kitap> populerKitaplar = kitaplar.stream()
                .limit(5)
                .collect(Collectors.toList());
            
            // Kategori dağılımını hesapla
            Map<String, Integer> kategoriDagilimi = new LinkedHashMap<>();
            for (Kategori kategori : kategoriler) {
                long kategoriKitapSayisi = kitaplar.stream()
                    .filter(k -> k.getKategori() != null && k.getKategori().getId().equals(kategori.getId()))
                    .count();
                kategoriDagilimi.put(kategori.getAd(), (int) kategoriKitapSayisi);
            }
            
            // Aylık sipariş verilerini hesapla (son 6 ay)
            Map<String, Integer> aylikSiparisler = new LinkedHashMap<>();
            for (int i = 5; i >= 0; i--) {
                LocalDateTime ay = now.minusMonths(i);
                String ayAdi = ay.getMonth().getDisplayName(TextStyle.FULL, new Locale("tr", "TR"));
                long aySiparisSayisi = siparisler.stream()
                    .filter(s -> s.getSiparisTarihi().getMonth() == ay.getMonth() && 
                               s.getSiparisTarihi().getYear() == ay.getYear())
                    .count();
                aylikSiparisler.put(ayAdi, (int) aySiparisSayisi);
            }
            
            // Model'e ekle
            model.addAttribute("toplamKitap", toplamKitap);
            model.addAttribute("toplamKullanici", toplamKullanici);
            model.addAttribute("toplamKategori", toplamKategori);
            model.addAttribute("buAySiparis", buAySiparis);
            model.addAttribute("sonSiparisler", sonSiparisler);
            model.addAttribute("populerKitaplar", populerKitaplar);
            model.addAttribute("kategoriDagilimi", kategoriDagilimi);
            model.addAttribute("aylikSiparisler", aylikSiparisler);
            
            System.out.println("Toplam Kitap: " + toplamKitap);
            System.out.println("Kategori Dağılımı: " + kategoriDagilimi.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(", ")));
            System.out.println("Aylık Siparişler: " + aylikSiparisler.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(", ")));
            
        } catch (Exception ex) {
            System.out.println("Rapor verileri yüklenirken hata: " + ex.getMessage());
            ex.printStackTrace();
            // Hata durumunda boş değerler
            model.addAttribute("toplamKitap", 0);
            model.addAttribute("toplamKullanici", 0);
            model.addAttribute("toplamKategori", 0);
            model.addAttribute("buAySiparis", 0);
            model.addAttribute("sonSiparisler", new ArrayList<>());
            model.addAttribute("populerKitaplar", new ArrayList<>());
            model.addAttribute("kategoriDagilimi", new HashMap<>());
            model.addAttribute("aylikSiparisler", new HashMap<>());
        }
        
        model.addAttribute("title", "Raporlar");
        return "admin/raporlar";
    }

    // Removed duplicate '/admin/ayarlar' mapping to avoid ambiguity; handled in AdminController.

    /**
     * Admin kontrolü yardımcı metodu
     */
    private boolean isAdmin(HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("IsAdmin");
        String rol = (String) session.getAttribute("KullaniciRol");
        return (isAdmin != null && isAdmin) || "Admin".equals(rol);
    }
}
