package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Favori;
import com.alperen.kitapsatissistemi.service.FavoriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * FavoriController - .NET C# projesindeki API controller'dan dönüştürülmüştür
 */
@RestController
@RequestMapping("/api/favoriler")
@CrossOrigin(origins = "*")
public class FavoriController {
    
    private final FavoriService favoriService;
    
    @Autowired
    public FavoriController(FavoriService favoriService) {
        this.favoriService = favoriService;
    }
    
    /**
     * Tüm favorileri getir
     * GET /api/favoriler
     */
    @GetMapping
    public ResponseEntity<List<Favori>> getAllFavoriler(@RequestParam(defaultValue = "false") boolean withDetails) {
        try {
            List<Favori> favoriler;
            if (withDetails) {
                favoriler = favoriService.getAllFavorilerWithDetails();
            } else {
                favoriler = favoriService.getAllFavoriler();
            }
            return ResponseEntity.ok(favoriler);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * ID'ye göre favori getir
     * GET /api/favoriler/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Favori> getFavoriById(@PathVariable Long id) {
        try {
            Optional<Favori> favori = favoriService.getFavoriById(id);
            return favori.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Kullanıcı ID'sine göre favorileri getir
     * GET /api/favoriler/kullanici/{kullaniciId}
     */
    @GetMapping("/kullanici/{kullaniciId}")
    public ResponseEntity<List<Favori>> getFavorilerByKullaniciId(@PathVariable Long kullaniciId,
                                                                 @RequestParam(defaultValue = "false") boolean withDetails) {
        try {
            List<Favori> favoriler;
            if (withDetails) {
                favoriler = favoriService.getFavorilerByKullaniciIdWithDetails(kullaniciId);
            } else {
                favoriler = favoriService.getFavorilerByKullaniciId(kullaniciId);
            }
            return ResponseEntity.ok(favoriler);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Kitap ID'sine göre favorileri getir
     * GET /api/favoriler/kitap/{kitapId}
     */
    @GetMapping("/kitap/{kitapId}")
    public ResponseEntity<List<Favori>> getFavorilerByKitapId(@PathVariable Long kitapId,
                                                             @RequestParam(defaultValue = "false") boolean withDetails) {
        try {
            List<Favori> favoriler;
            if (withDetails) {
                favoriler = favoriService.getFavorilerByKitapIdWithDetails(kitapId);
            } else {
                favoriler = favoriService.getFavorilerByKitapId(kitapId);
            }
            return ResponseEntity.ok(favoriler);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Yeni favori ekle
     * POST /api/favoriler
     */
    @PostMapping
    public ResponseEntity<?> addFavori(@RequestBody Map<String, Long> request) {
        try {
            Long kullaniciId = request.get("kullaniciId");
            Long kitapId = request.get("kitapId");
            
            Favori yeniFavori = favoriService.addFavori(kullaniciId, kitapId);
            return ResponseEntity.status(HttpStatus.CREATED).body(yeniFavori);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Favori eklenirken bir hata oluştu");
        }
    }
    
    /**
     * Favori sil
     * DELETE /api/favoriler/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFavori(@PathVariable Long id) {
        try {
            favoriService.deleteFavori(id);
            return ResponseEntity.ok().body("Favori başarıyla silindi");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Favori silinirken bir hata oluştu");
        }
    }
    
    /**
     * Kullanıcı ve kitap ID'sine göre favori sil
     * DELETE /api/favoriler/kullanici/{kullaniciId}/kitap/{kitapId}
     */
    @DeleteMapping("/kullanici/{kullaniciId}/kitap/{kitapId}")
    public ResponseEntity<?> deleteFavoriByKullaniciAndKitap(@PathVariable Long kullaniciId, 
                                                           @PathVariable Long kitapId) {
        try {
            favoriService.deleteFavoriByKullaniciAndKitap(kullaniciId, kitapId);
            return ResponseEntity.ok().body("Favori başarıyla silindi");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Favori silinirken bir hata oluştu");
        }
    }
    
    /**
     * Kullanıcının tüm favorilerini sil
     * DELETE /api/favoriler/kullanici/{kullaniciId}
     */
    @DeleteMapping("/kullanici/{kullaniciId}")
    public ResponseEntity<?> deleteAllFavorilerByKullaniciId(@PathVariable Long kullaniciId) {
        try {
            favoriService.deleteAllFavorilerByKullaniciId(kullaniciId);
            return ResponseEntity.ok().body("Kullanıcının tüm favorileri başarıyla silindi");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Favoriler silinirken bir hata oluştu");
        }
    }
    
    /**
     * Kitabın tüm favorilerini sil
     * DELETE /api/favoriler/kitap/{kitapId}
     */
    @DeleteMapping("/kitap/{kitapId}")
    public ResponseEntity<?> deleteAllFavorilerByKitapId(@PathVariable Long kitapId) {
        try {
            favoriService.deleteAllFavorilerByKitapId(kitapId);
            return ResponseEntity.ok().body("Kitabın tüm favorileri başarıyla silindi");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Favoriler silinirken bir hata oluştu");
        }
    }
    
    /**
     * Kullanıcı ve kitap için favori var mı kontrol et
     * GET /api/favoriler/check?kullaniciId={kullaniciId}&kitapId={kitapId}
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkFavoriExists(@RequestParam Long kullaniciId, 
                                                    @RequestParam Long kitapId) {
        try {
            boolean exists = favoriService.existsByKullaniciIdAndKitapId(kullaniciId, kitapId);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Kullanıcının favori sayısını getir
     * GET /api/favoriler/kullanici/{kullaniciId}/count
     */
    @GetMapping("/kullanici/{kullaniciId}/count")
    public ResponseEntity<Long> getFavoriCountByKullaniciId(@PathVariable Long kullaniciId) {
        try {
            long count = favoriService.getFavoriCountByKullaniciId(kullaniciId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Kitabın favori sayısını getir
     * GET /api/favoriler/kitap/{kitapId}/count
     */
    @GetMapping("/kitap/{kitapId}/count")
    public ResponseEntity<Long> getFavoriCountByKitapId(@PathVariable Long kitapId) {
        try {
            long count = favoriService.getFavoriCountByKitapId(kitapId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * En çok favorilenen kitapları getir
     * GET /api/favoriler/most-favorited-books
     */
    @GetMapping("/most-favorited-books")
    public ResponseEntity<List<Object[]>> getEnCokFavorilenenKitaplar() {
        try {
            List<Object[]> result = favoriService.getEnCokFavorilenenKitaplar();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * En aktif kullanıcıları getir (en çok favori ekleyen)
     * GET /api/favoriler/most-active-users
     */
    @GetMapping("/most-active-users")
    public ResponseEntity<List<Object[]>> getEnAktifKullanicilar() {
        try {
            List<Object[]> result = favoriService.getEnAktifKullanicilar();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Toplam favori sayısını getir
     * GET /api/favoriler/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getFavoriCount() {
        try {
            long count = favoriService.getFavoriCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
