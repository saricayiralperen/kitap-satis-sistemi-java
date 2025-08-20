package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.service.KitapService;
import com.alperen.kitapsatissistemi.service.KategoriService;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.DuplicateEntityException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * KitapController - .NET C# projesindeki API controller'dan dönüştürülmüştür
 */
@RestController
@RequestMapping("/api/kitaplar")
public class KitapController {
    
    @Autowired
    private KitapService kitapService;
    
    @Autowired
    private KategoriService kategoriService;
    
    /**
     * Tüm kitapları getir
     * GET /api/kitaplar
     */
    @GetMapping
    public ResponseEntity<List<Kitap>> getAllKitaplar(@RequestParam(defaultValue = "false") boolean withKategori) {
        try {
            List<Kitap> kitaplar;
            if (withKategori) {
                kitaplar = kitapService.getAllKitaplarWithKategori();
            } else {
                kitaplar = kitapService.getAllKitaplar();
            }
            return ResponseEntity.ok(kitaplar);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * ID'ye göre kitap getir
     * GET /api/kitaplar/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Kitap> getKitapById(@PathVariable Long id, 
                                             @RequestParam(defaultValue = "false") boolean withKategori) {
        try {
            Optional<Kitap> kitap;
            if (withKategori) {
                kitap = kitapService.getKitapByIdWithKategori(id);
            } else {
                kitap = kitapService.getKitapById(id);
            }
            return kitap.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Kitap adına göre arama
     * GET /api/kitaplar/search/ad?q={ad}
     */
    @GetMapping("/search/ad")
    public ResponseEntity<List<Kitap>> searchKitaplarByAd(@RequestParam String q) {
        try {
            List<Kitap> kitaplar = kitapService.searchKitaplarByAd(q);
            return ResponseEntity.ok(kitaplar);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Yazar adına göre arama
     * GET /api/kitaplar/search/yazar?q={yazar}
     */
    @GetMapping("/search/yazar")
    public ResponseEntity<List<Kitap>> searchKitaplarByYazar(@RequestParam String q) {
        try {
            List<Kitap> kitaplar = kitapService.searchKitaplarByYazar(q);
            return ResponseEntity.ok(kitaplar);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Kategori ID'sine göre kitapları getir
     * GET /api/kitaplar/kategori/{kategoriId}
     */
    @GetMapping("/kategori/{kategoriId}")
    public ResponseEntity<List<Kitap>> getKitaplarByKategoriId(@PathVariable Long kategoriId) {
        try {
            List<Kitap> kitaplar = kitapService.getKitaplarByKategoriId(kategoriId);
            return ResponseEntity.ok(kitaplar);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Fiyat aralığına göre kitapları getir
     * GET /api/kitaplar/fiyat-araligi?min={min}&max={max}
     */
    @GetMapping("/fiyat-araligi")
    public ResponseEntity<List<Kitap>> getKitaplarByFiyatAraligi(@RequestParam BigDecimal min, 
                                                                @RequestParam BigDecimal max) {
        try {
            List<Kitap> kitaplar = kitapService.getKitaplarByFiyatAraligi(min, max);
            return ResponseEntity.ok(kitaplar);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * En pahalı kitapları getir
     * GET /api/kitaplar/en-pahali
     */
    @GetMapping("/en-pahali")
    public ResponseEntity<List<Kitap>> getEnPahaliKitaplar() {
        try {
            List<Kitap> kitaplar = kitapService.getEnPahaliKitaplar();
            return ResponseEntity.ok(kitaplar);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * En ucuz kitapları getir
     * GET /api/kitaplar/en-ucuz
     */
    @GetMapping("/en-ucuz")
    public ResponseEntity<List<Kitap>> getEnUcuzKitaplar() {
        try {
            List<Kitap> kitaplar = kitapService.getEnUcuzKitaplar();
            return ResponseEntity.ok(kitaplar);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Resim URL'si olan kitapları getir
     * GET /api/kitaplar/with-resim
     */
    @GetMapping("/with-resim")
    public ResponseEntity<List<Kitap>> getKitaplarWithResim() {
        try {
            List<Kitap> kitaplar = kitapService.getKitaplarWithResim();
            return ResponseEntity.ok(kitaplar);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Yeni kitap oluştur
     * POST /api/kitaplar
     */
    @PostMapping
    public ResponseEntity<?> createKitap(@Valid @RequestBody Kitap kitap) {
        try {
            Kitap yeniKitap = kitapService.createKitap(kitap);
            return ResponseEntity.status(HttpStatus.CREATED).body(yeniKitap);
        } catch (DuplicateEntityException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Kitap oluşturulurken bir hata oluştu");
        }
    }
    
    /**
     * Kitap güncelle
     * PUT /api/kitaplar/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateKitap(@PathVariable Long id, 
                                        @Valid @RequestBody Kitap kitapDetaylari) {
        try {
            Kitap guncellenenKitap = kitapService.updateKitap(id, kitapDetaylari);
            return ResponseEntity.ok(guncellenenKitap);
        } catch (EntityNotFoundBusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DuplicateEntityException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Kitap güncellenirken bir hata oluştu");
        }
    }
    
    /**
     * Kitap sil
     * DELETE /api/kitaplar/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKitap(@PathVariable Long id) {
        try {
            kitapService.deleteKitap(id);
            return ResponseEntity.ok().body("Kitap başarıyla silindi");
        } catch (EntityNotFoundBusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Kitap silinirken bir hata oluştu");
        }
    }
    
    /**
     * Kitap var mı kontrol et
     * GET /api/kitaplar/{id}/exists
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> checkKitapExists(@PathVariable Long id) {
        try {
            boolean exists = kitapService.existsById(id);
            return ResponseEntity.ok(exists);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Kategori ID'sine göre kitap sayısını getir
     * GET /api/kitaplar/kategori/{kategoriId}/count
     */
    @GetMapping("/kategori/{kategoriId}/count")
    public ResponseEntity<Long> getKitapCountByKategoriId(@PathVariable Long kategoriId) {
        try {
            long count = kitapService.getKitapCountByKategoriId(kategoriId);
            return ResponseEntity.ok(count);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Toplam kitap sayısını getir
     * GET /api/kitaplar/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getKitapCount() {
        try {
            long count = kitapService.getKitapCount();
            return ResponseEntity.ok(count);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
