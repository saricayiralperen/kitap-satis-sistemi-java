package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Siparis;
import com.alperen.kitapsatissistemi.entity.SiparisDetay;
import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.service.SiparisService;
import com.alperen.kitapsatissistemi.service.KitapService;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * SiparisController - .NET C# projesindeki API controller'dan dönüştürülmüştür
 */
@RestController
@RequestMapping("/api/siparisler")
@CrossOrigin(origins = "*")
public class SiparisController {
    
    private final SiparisService siparisService;
    private final KitapService kitapService;
    
    @Autowired
    public SiparisController(SiparisService siparisService, KitapService kitapService) {
        this.siparisService = siparisService;
        this.kitapService = kitapService;
    }
    
    /**
     * Tüm siparişleri getir
     * GET /api/siparisler
     */
    @GetMapping
    public ResponseEntity<List<Siparis>> getAllSiparisler(@RequestParam(defaultValue = "false") boolean withDetails) {
        try {
            List<Siparis> siparisler;
            if (withDetails) {
                siparisler = siparisService.getAllSiparislerWithDetails();
            } else {
                siparisler = siparisService.getAllSiparisler();
            }
            return ResponseEntity.ok(siparisler);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * ID'ye göre sipariş getir
     * GET /api/siparisler/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Siparis> getSiparisById(@PathVariable Long id,
                                                 @RequestParam(defaultValue = "false") boolean withDetails) {
        try {
            Optional<Siparis> siparis;
            if (withDetails) {
                siparis = siparisService.getSiparisByIdWithDetails(id);
            } else {
                siparis = siparisService.getSiparisById(id);
            }
            return siparis.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Kullanıcı ID'sine göre siparişleri getir
     * GET /api/siparisler/kullanici/{kullaniciId}
     */
    @GetMapping("/kullanici/{kullaniciId}")
    public ResponseEntity<List<Siparis>> getSiparislerByKullaniciId(@PathVariable Long kullaniciId,
                                                                   @RequestParam(defaultValue = "false") boolean withDetails) {
        try {
            List<Siparis> siparisler;
            if (withDetails) {
                siparisler = siparisService.getSiparislerByKullaniciIdWithDetails(kullaniciId);
            } else {
                siparisler = siparisService.getSiparislerByKullaniciId(kullaniciId);
            }
            return ResponseEntity.ok(siparisler);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Duruma göre siparişleri getir
     * GET /api/siparisler/durum/{durum}
     */
    @GetMapping("/durum/{durum}")
    public ResponseEntity<List<Siparis>> getSiparislerByDurum(@PathVariable String durum) {
        try {
            List<Siparis> siparisler = siparisService.getSiparislerByDurum(durum);
            return ResponseEntity.ok(siparisler);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Tarih aralığına göre siparişleri getir
     * GET /api/siparisler/tarih-araligi?baslangic={baslangic}&bitis={bitis}
     */
    @GetMapping("/tarih-araligi")
    public ResponseEntity<List<Siparis>> getSiparislerByTarihAraligi(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime baslangic,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime bitis) {
        try {
            List<Siparis> siparisler = siparisService.getSiparislerByTarihAraligi(baslangic, bitis);
            return ResponseEntity.ok(siparisler);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Tutar aralığına göre siparişleri getir
     * GET /api/siparisler/tutar-araligi?min={min}&max={max}
     */
    @GetMapping("/tutar-araligi")
    public ResponseEntity<List<Siparis>> getSiparislerByTutarAraligi(@RequestParam BigDecimal min,
                                                                    @RequestParam BigDecimal max) {
        try {
            List<Siparis> siparisler = siparisService.getSiparislerByTutarAraligi(min, max);
            return ResponseEntity.ok(siparisler);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Yeni sipariş oluştur
     * POST /api/siparisler
     */
    @PostMapping
    public ResponseEntity<?> createSiparis(@Valid @RequestBody Map<String, Object> request) {
        try {
            Long kullaniciId = Long.valueOf(request.get("kullaniciId").toString());
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detaylarMap = (List<Map<String, Object>>) request.get("siparisDetaylari");
            
            List<SiparisDetay> siparisDetaylari = detaylarMap.stream()
                    .map(detayMap -> {
                        SiparisDetay detay = new SiparisDetay();
                        Long kitapId = Long.valueOf(detayMap.get("kitapId").toString());
                        Kitap kitap = kitapService.getKitapById(kitapId)
                            .orElseThrow(() -> new RuntimeException("Kitap bulunamadı: " + kitapId));
                        detay.setKitap(kitap);
                        detay.setAdet(Integer.valueOf(detayMap.get("adet").toString()));
                        detay.setFiyat(new BigDecimal(detayMap.get("fiyat").toString()));
                        return detay;
                    })
                    .collect(Collectors.toList());
            
            Siparis yeniSiparis = siparisService.createSiparis(kullaniciId, siparisDetaylari);
            return ResponseEntity.status(HttpStatus.CREATED).body(yeniSiparis);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Sipariş oluşturulurken bir hata oluştu");
        }
    }
    
    /**
     * Sipariş durumunu güncelle
     * PUT /api/siparisler/{id}/durum
     */
    @PutMapping("/{id}/durum")
    public ResponseEntity<?> updateSiparisDurum(@PathVariable Long id, 
                                               @RequestBody Map<String, String> request) {
        try {
            String yeniDurum = request.get("durum");
            Siparis guncellenenSiparis = siparisService.updateSiparisDurum(id, yeniDurum);
            return ResponseEntity.ok(guncellenenSiparis);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Sipariş durumu güncellenirken bir hata oluştu");
        }
    }
    
    /**
     * Sipariş güncelle
     * PUT /api/siparisler/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSiparis(@PathVariable Long id, 
                                          @Valid @RequestBody Siparis siparisDetaylari) {
        try {
            Siparis guncellenenSiparis = siparisService.updateSiparis(id, siparisDetaylari);
            return ResponseEntity.ok(guncellenenSiparis);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Sipariş güncellenirken bir hata oluştu");
        }
    }
    
    /**
     * Sipariş sil
     * DELETE /api/siparisler/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSiparis(@PathVariable Long id) {
        try {
            siparisService.deleteSiparis(id);
            return ResponseEntity.ok().body("Sipariş başarıyla silindi");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Sipariş silinirken bir hata oluştu");
        }
    }
    
    /**
     * Kullanıcının sipariş sayısını getir
     * GET /api/siparisler/kullanici/{kullaniciId}/count
     */
    @GetMapping("/kullanici/{kullaniciId}/count")
    public ResponseEntity<Long> getSiparisCountByKullaniciId(@PathVariable Long kullaniciId) {
        try {
            long count = siparisService.getSiparisCountByKullaniciId(kullaniciId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Duruma göre sipariş sayısını getir
     * GET /api/siparisler/durum/{durum}/count
     */
    @GetMapping("/durum/{durum}/count")
    public ResponseEntity<Long> getSiparisCountByDurum(@PathVariable String durum) {
        try {
            long count = siparisService.getSiparisCountByDurum(durum);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Kullanıcının toplam harcamasını getir
     * GET /api/siparisler/kullanici/{kullaniciId}/total-spent
     */
    @GetMapping("/kullanici/{kullaniciId}/total-spent")
    public ResponseEntity<BigDecimal> getTotalSpentByKullaniciId(@PathVariable Long kullaniciId) {
        try {
            BigDecimal totalSpent = siparisService.getTotalSpentByKullaniciId(kullaniciId);
            return ResponseEntity.ok(totalSpent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Günlük satış istatistiklerini getir
     * GET /api/siparisler/stats/daily
     */
    @GetMapping("/stats/daily")
    public ResponseEntity<List<Object[]>> getDailySalesStats() {
        try {
            List<Object[]> stats = siparisService.getDailySalesStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Aylık satış istatistiklerini getir
     * GET /api/siparisler/stats/monthly
     */
    @GetMapping("/stats/monthly")
    public ResponseEntity<List<Object[]>> getMonthlySalesStats() {
        try {
            List<Object[]> stats = siparisService.getMonthlySalesStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * En çok sipariş veren kullanıcıları getir
     * GET /api/siparisler/top-customers
     */
    @GetMapping("/top-customers")
    public ResponseEntity<List<Object[]>> getTopCustomers() {
        try {
            List<Object[]> topCustomers = siparisService.getTopCustomers();
            return ResponseEntity.ok(topCustomers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Toplam sipariş sayısını getir
     * GET /api/siparisler/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getSiparisCount() {
        try {
            long count = siparisService.getSiparisCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
