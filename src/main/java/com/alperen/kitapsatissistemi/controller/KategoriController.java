package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.service.KategoriService;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.exception.DuplicateEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * KategoriController - .NET C# projesindeki controller'dan dönüştürülmüştür
 */
@RestController
@RequestMapping("/api/kategoriler")
public class KategoriController {
    
    private final KategoriService kategoriService;
    
    @Autowired
    public KategoriController(KategoriService kategoriService) {
        this.kategoriService = kategoriService;
    }
    
    // API metodları

    // REST API endpoints
    
    /**
     * Tüm kategorileri getir
     * GET /api/kategoriler
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Kategori>> getAllKategoriler() {
        try {
            List<Kategori> kategoriler = kategoriService.getAllKategoriler();
            return ResponseEntity.ok(kategoriler);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * ID'ye göre kategori getir
     * GET /api/kategoriler/{id}
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Kategori> getKategoriById(@PathVariable Long id) {
        try {
            Optional<Kategori> kategori = kategoriService.getKategoriById(id);
            return kategori.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Kategori adına göre arama
     * GET /api/kategoriler/search?ad={ad}
     */
    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<List<Kategori>> searchKategoriler(@RequestParam String ad) {
        try {
            List<Kategori> kategoriler = kategoriService.searchKategorilerByAd(ad);
            return ResponseEntity.ok(kategoriler);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Açıklaması olan kategorileri getir
     * GET /api/kategoriler/with-aciklama
     */
    @GetMapping("/with-aciklama")
    @ResponseBody
    public ResponseEntity<List<Kategori>> getKategorilerWithAciklama() {
        try {
            List<Kategori> kategoriler = kategoriService.getKategorilerWithAciklama();
            return ResponseEntity.ok(kategoriler);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Yeni kategori oluştur
     * POST /api/kategoriler
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<?> createKategori(@Valid @RequestBody Kategori kategori) {
        try {
            Kategori yeniKategori = kategoriService.createKategori(kategori);
            return ResponseEntity.status(HttpStatus.CREATED).body(yeniKategori);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Kategori oluşturulurken bir hata oluştu");
        }
    }
    
    /**
     * Kategori güncelle
     * PUT /api/kategoriler/{id}
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> updateKategori(@PathVariable Long id, 
                                           @Valid @RequestBody Kategori kategoriDetaylari) {
        try {
            Kategori guncellenenKategori = kategoriService.updateKategori(id, kategoriDetaylari);
            return ResponseEntity.ok(guncellenenKategori);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Kategori güncellenirken bir hata oluştu");
        }
    }
    
    /**
     * Kategori sil
     * DELETE /api/kategoriler/{id}
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteKategori(@PathVariable Long id) {
        try {
            kategoriService.deleteKategori(id);
            return ResponseEntity.ok().body("Kategori başarıyla silindi");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Kategori silinirken bir hata oluştu");
        }
    }
    
    /**
     * Kategori var mı kontrol et
     * GET /api/kategoriler/{id}/exists
     */
    @GetMapping("/{id}/exists")
    @ResponseBody
    public ResponseEntity<Boolean> checkKategoriExists(@PathVariable Long id) {
        try {
            boolean exists = kategoriService.existsById(id);
            return ResponseEntity.ok(exists);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Kategori adı var mı kontrol et
     * GET /api/kategoriler/check-name?ad={ad}
     */
    @GetMapping("/check-name")
    @ResponseBody
    public ResponseEntity<Boolean> checkKategoriAdExists(@RequestParam String ad) {
        try {
            boolean exists = kategoriService.existsByAd(ad);
            return ResponseEntity.ok(exists);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Toplam kategori sayısını getir
     * GET /api/kategoriler/count
     */
    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<Long> getKategoriCount() {
        try {
            long count = kategoriService.getKategoriCount();
            return ResponseEntity.ok(count);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
