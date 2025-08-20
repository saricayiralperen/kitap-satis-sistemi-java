package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.SiparisDetay;
import com.alperen.kitapsatissistemi.service.SiparisDetayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * SiparisDetayController - .NET C# projesindeki API controller'dan dönüştürülmüştür
 */
@RestController
@RequestMapping("/api/siparis-detaylar")
@CrossOrigin(origins = "*")
public class SiparisDetayController {
    
    private final SiparisDetayService siparisDetayService;
    
    @Autowired
    public SiparisDetayController(SiparisDetayService siparisDetayService) {
        this.siparisDetayService = siparisDetayService;
    }
    
    /**
     * Tüm sipariş detaylarını getir
     * GET /api/siparis-detaylar
     */
    @GetMapping
    public ResponseEntity<List<SiparisDetay>> getAllSiparisDetaylar(@RequestParam(defaultValue = "false") boolean withDetails) {
        try {
            List<SiparisDetay> siparisDetaylar;
            if (withDetails) {
                siparisDetaylar = siparisDetayService.getAllSiparisDetaylarWithDetails();
            } else {
                siparisDetaylar = siparisDetayService.getAllSiparisDetaylar();
            }
            return ResponseEntity.ok(siparisDetaylar);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * ID'ye göre sipariş detayı getir
     * GET /api/siparis-detaylar/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<SiparisDetay> getSiparisDetayById(@PathVariable Long id) {
        try {
            Optional<SiparisDetay> siparisDetay = siparisDetayService.getSiparisDetayById(id);
            return siparisDetay.map(ResponseEntity::ok)
                              .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Sipariş ID'sine göre sipariş detaylarını getir
     * GET /api/siparis-detaylar/siparis/{siparisId}
     */
    @GetMapping("/siparis/{siparisId}")
    public ResponseEntity<List<SiparisDetay>> getSiparisDetaylarBySiparisId(@PathVariable Long siparisId,
                                                                           @RequestParam(defaultValue = "false") boolean withKitap) {
        try {
            List<SiparisDetay> siparisDetaylar;
            if (withKitap) {
                siparisDetaylar = siparisDetayService.getSiparisDetaylarBySiparisIdWithKitap(siparisId);
            } else {
                siparisDetaylar = siparisDetayService.getSiparisDetaylarBySiparisId(siparisId);
            }
            return ResponseEntity.ok(siparisDetaylar);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Kitap ID'sine göre sipariş detaylarını getir
     * GET /api/siparis-detaylar/kitap/{kitapId}
     */
    @GetMapping("/kitap/{kitapId}")
    public ResponseEntity<List<SiparisDetay>> getSiparisDetaylarByKitapId(@PathVariable Long kitapId,
                                                                         @RequestParam(defaultValue = "false") boolean withSiparis) {
        try {
            List<SiparisDetay> siparisDetaylar;
            if (withSiparis) {
                siparisDetaylar = siparisDetayService.getSiparisDetaylarByKitapIdWithSiparis(kitapId);
            } else {
                siparisDetaylar = siparisDetayService.getSiparisDetaylarByKitapId(kitapId);
            }
            return ResponseEntity.ok(siparisDetaylar);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Sipariş ve kitap ID'sine göre sipariş detayı getir
     * GET /api/siparis-detaylar/siparis/{siparisId}/kitap/{kitapId}
     */
    @GetMapping("/siparis/{siparisId}/kitap/{kitapId}")
    public ResponseEntity<SiparisDetay> getSiparisDetayBySiparisIdAndKitapId(@PathVariable Long siparisId,
                                                                           @PathVariable Long kitapId) {
        try {
            Optional<SiparisDetay> siparisDetay = siparisDetayService.getSiparisDetayBySiparisIdAndKitapId(siparisId, kitapId);
            return siparisDetay.map(ResponseEntity::ok)
                              .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Yeni sipariş detayı oluştur
     * POST /api/siparis-detaylar
     */
    @PostMapping
    public ResponseEntity<?> createSiparisDetay(@Valid @RequestBody SiparisDetay siparisDetay) {
        try {
            SiparisDetay yeniSiparisDetay = siparisDetayService.createSiparisDetay(siparisDetay);
            return ResponseEntity.status(HttpStatus.CREATED).body(yeniSiparisDetay);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Sipariş detayı oluşturulurken bir hata oluştu");
        }
    }
    
    /**
     * Sipariş detayı güncelle
     * PUT /api/siparis-detaylar/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSiparisDetay(@PathVariable Long id, 
                                               @Valid @RequestBody SiparisDetay siparisDetayDetaylari) {
        try {
            SiparisDetay guncellenenSiparisDetay = siparisDetayService.updateSiparisDetay(id, siparisDetayDetaylari);
            return ResponseEntity.ok(guncellenenSiparisDetay);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Sipariş detayı güncellenirken bir hata oluştu");
        }
    }
    
    /**
     * Sipariş detayı sil
     * DELETE /api/siparis-detaylar/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSiparisDetay(@PathVariable Long id) {
        try {
            siparisDetayService.deleteSiparisDetay(id);
            return ResponseEntity.ok().body("Sipariş detayı başarıyla silindi");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Sipariş detayı silinirken bir hata oluştu");
        }
    }
    
    /**
     * Siparişin tüm detaylarını sil
     * DELETE /api/siparis-detaylar/siparis/{siparisId}
     */
    @DeleteMapping("/siparis/{siparisId}")
    public ResponseEntity<?> deleteAllSiparisDetaylarBySiparisId(@PathVariable Long siparisId) {
        try {
            siparisDetayService.deleteAllSiparisDetaylarBySiparisId(siparisId);
            return ResponseEntity.ok().body("Siparişin tüm detayları başarıyla silindi");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Sipariş detayları silinirken bir hata oluştu");
        }
    }
    
    /**
     * Kitabın toplam satış adedini getir
     * GET /api/siparis-detaylar/kitap/{kitapId}/total-quantity
     */
    @GetMapping("/kitap/{kitapId}/total-quantity")
    public ResponseEntity<Integer> getTotalSalesQuantityByKitapId(@PathVariable Long kitapId) {
        try {
            Integer totalQuantity = siparisDetayService.getTotalSalesQuantityByKitapId(kitapId);
            return ResponseEntity.ok(totalQuantity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Kitabın toplam satış tutarını getir
     * GET /api/siparis-detaylar/kitap/{kitapId}/total-amount
     */
    @GetMapping("/kitap/{kitapId}/total-amount")
    public ResponseEntity<BigDecimal> getTotalSalesAmountByKitapId(@PathVariable Long kitapId) {
        try {
            BigDecimal totalAmount = siparisDetayService.getTotalSalesAmountByKitapId(kitapId);
            return ResponseEntity.ok(totalAmount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * En çok satan kitapları getir
     * GET /api/siparis-detaylar/best-selling-books
     */
    @GetMapping("/best-selling-books")
    public ResponseEntity<List<Object[]>> getBestSellingBooks() {
        try {
            List<Object[]> bestSellingBooks = siparisDetayService.getBestSellingBooks();
            return ResponseEntity.ok(bestSellingBooks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * En çok gelir getiren kitapları getir
     * GET /api/siparis-detaylar/highest-earning-books
     */
    @GetMapping("/highest-earning-books")
    public ResponseEntity<List<Object[]>> getHighestEarningBooks() {
        try {
            List<Object[]> highestEarningBooks = siparisDetayService.getHighestEarningBooks();
            return ResponseEntity.ok(highestEarningBooks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Siparişin toplam tutarını hesapla
     * GET /api/siparis-detaylar/siparis/{siparisId}/total-amount
     */
    @GetMapping("/siparis/{siparisId}/total-amount")
    public ResponseEntity<BigDecimal> calculateTotalOrderAmount(@PathVariable Long siparisId) {
        try {
            BigDecimal totalAmount = siparisDetayService.calculateTotalOrderAmount(siparisId);
            return ResponseEntity.ok(totalAmount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Kategoriye göre satış istatistiklerini getir
     * GET /api/siparis-detaylar/stats/by-category
     */
    @GetMapping("/stats/by-category")
    public ResponseEntity<List<Object[]>> getSalesStatsByCategory() {
        try {
            List<Object[]> stats = siparisDetayService.getSalesStatsByCategory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Yazara göre satış istatistiklerini getir
     * GET /api/siparis-detaylar/stats/by-author
     */
    @GetMapping("/stats/by-author")
    public ResponseEntity<List<Object[]>> getSalesStatsByAuthor() {
        try {
            List<Object[]> stats = siparisDetayService.getSalesStatsByAuthor();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Sipariş ID'sine göre sipariş detayı sayısını getir
     * GET /api/siparis-detaylar/siparis/{siparisId}/count
     */
    @GetMapping("/siparis/{siparisId}/count")
    public ResponseEntity<Long> getSiparisDetayCountBySiparisId(@PathVariable Long siparisId) {
        try {
            long count = siparisDetayService.getSiparisDetayCountBySiparisId(siparisId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Kitap ID'sine göre sipariş detayı sayısını getir
     * GET /api/siparis-detaylar/kitap/{kitapId}/count
     */
    @GetMapping("/kitap/{kitapId}/count")
    public ResponseEntity<Long> getSiparisDetayCountByKitapId(@PathVariable Long kitapId) {
        try {
            long count = siparisDetayService.getSiparisDetayCountByKitapId(kitapId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Toplam sipariş detayı sayısını getir
     * GET /api/siparis-detaylar/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getSiparisDetayCount() {
        try {
            long count = siparisDetayService.getSiparisDetayCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
