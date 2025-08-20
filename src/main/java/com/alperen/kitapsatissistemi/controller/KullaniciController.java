package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.service.KullaniciService;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * KullaniciController - .NET C# projesindeki API controller'dan dönüştürülmüştür
 */
@RestController
@RequestMapping("/api/kullanicilar")
@CrossOrigin(origins = "*")
public class KullaniciController {
    
    private final KullaniciService kullaniciService;
    
    @Autowired
    public KullaniciController(KullaniciService kullaniciService) {
        this.kullaniciService = kullaniciService;
    }
    
    /**
     * Tüm kullanıcıları getir
     * GET /api/kullanicilar
     */
    @GetMapping
    public ResponseEntity<List<Kullanici>> getAllKullanicilar() {
        try {
            List<Kullanici> kullanicilar = kullaniciService.getAllKullanicilar();
            // Şifre hash'lerini gizle
            kullanicilar.forEach(k -> k.setSifreHash(null));
            return ResponseEntity.ok(kullanicilar);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * ID'ye göre kullanıcı getir
     * GET /api/kullanicilar/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Kullanici> getKullaniciById(@PathVariable Long id) {
        try {
            Optional<Kullanici> kullanici = kullaniciService.getKullaniciById(id);
            if (kullanici.isPresent()) {
                Kullanici k = kullanici.get();
                k.setSifreHash(null); // Şifre hash'ini gizle
                return ResponseEntity.ok(k);
            }
            return ResponseEntity.notFound().build();
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Email'e göre kullanıcı getir
     * GET /api/kullanicilar/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Kullanici> getKullaniciByEmail(@PathVariable String email) {
        try {
            Optional<Kullanici> kullanici = kullaniciService.getKullaniciByEmail(email);
            if (kullanici.isPresent()) {
                Kullanici k = kullanici.get();
                k.setSifreHash(null); // Şifre hash'ini gizle
                return ResponseEntity.ok(k);
            }
            return ResponseEntity.notFound().build();
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Role göre kullanıcıları getir
     * GET /api/kullanicilar/rol/{rol}
     */
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Kullanici>> getKullanicilarByRol(@PathVariable String rol) {
        try {
            List<Kullanici> kullanicilar = kullaniciService.getKullanicilarByRol(rol);
            // Şifre hash'lerini gizle
            kullanicilar.forEach(k -> k.setSifreHash(null));
            return ResponseEntity.ok(kullanicilar);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Admin kullanıcıları getir
     * GET /api/kullanicilar/adminler
     */
    @GetMapping("/adminler")
    public ResponseEntity<List<Kullanici>> getAdminKullanicilar() {
        try {
            List<Kullanici> kullanicilar = kullaniciService.getAdminKullanicilar();
            // Şifre hash'lerini gizle
            kullanicilar.forEach(k -> k.setSifreHash(null));
            return ResponseEntity.ok(kullanicilar);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Ad soyada göre arama
     * GET /api/kullanicilar/search?q={adSoyad}
     */
    @GetMapping("/search")
    public ResponseEntity<List<Kullanici>> searchKullanicilar(@RequestParam String q) {
        try {
            List<Kullanici> kullanicilar = kullaniciService.searchKullanicilarByAdSoyad(q);
            // Şifre hash'lerini gizle
            kullanicilar.forEach(k -> k.setSifreHash(null));
            return ResponseEntity.ok(kullanicilar);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Kullanıcı kaydı
     * POST /api/kullanicilar/register
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerKullanici(@Valid @RequestBody Map<String, Object> request) {
        try {
            Kullanici kullanici = new Kullanici();
            kullanici.setAdSoyad((String) request.get("adSoyad"));
            kullanici.setEmail((String) request.get("email"));
            kullanici.setRol((String) request.get("rol"));
            
            String sifre = (String) request.get("sifre");
            
            Kullanici yeniKullanici = kullaniciService.registerKullanici(kullanici, sifre);
            yeniKullanici.setSifreHash(null); // Şifre hash'ini gizle
            
            return ResponseEntity.status(HttpStatus.CREATED).body(yeniKullanici);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Kullanıcı kaydı sırasında bir hata oluştu");
        }
    }
    
    /**
     * Kullanıcı girişi
     * POST /api/kullanicilar/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginKullanici(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String sifre = loginRequest.get("sifre");
            
            Optional<Kullanici> kullanici = kullaniciService.authenticateKullanici(email, sifre);
            
            if (kullanici.isPresent()) {
                Kullanici k = kullanici.get();
                k.setSifreHash(null); // Şifre hash'ini gizle
                return ResponseEntity.ok(k);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                    .body("Email veya şifre hatalı");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Giriş sırasında bir hata oluştu");
        }
    }
    
    /**
     * Kullanıcı güncelle
     * PUT /api/kullanicilar/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateKullanici(@PathVariable Long id, 
                                            @Valid @RequestBody Kullanici kullaniciDetaylari) {
        try {
            Kullanici guncellenenKullanici = kullaniciService.updateKullanici(id, kullaniciDetaylari);
            guncellenenKullanici.setSifreHash(null); // Şifre hash'ini gizle
            return ResponseEntity.ok(guncellenenKullanici);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Kullanıcı güncellenirken bir hata oluştu");
        }
    }
    
    /**
     * Şifre değiştir
     * PUT /api/kullanicilar/{id}/change-password
     */
    @PutMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long id, 
                                           @RequestBody Map<String, String> passwordRequest) {
        try {
            String eskiSifre = passwordRequest.get("eskiSifre");
            String yeniSifre = passwordRequest.get("yeniSifre");
            
            kullaniciService.changePassword(id, eskiSifre, yeniSifre);
            return ResponseEntity.ok().body("Şifre başarıyla değiştirildi");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Şifre değiştirilirken bir hata oluştu");
        }
    }
    
    /**
     * Şifre sıfırla (Admin)
     * PUT /api/kullanicilar/{id}/reset-password
     */
    @PutMapping("/{id}/reset-password")
    public ResponseEntity<?> resetPassword(@PathVariable Long id, 
                                          @RequestBody Map<String, String> passwordRequest) {
        try {
            String yeniSifre = passwordRequest.get("yeniSifre");
            
            kullaniciService.resetPassword(id, yeniSifre);
            return ResponseEntity.ok().body("Şifre başarıyla sıfırlandı");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Şifre sıfırlanırken bir hata oluştu");
        }
    }
    
    /**
     * Kullanıcı sil
     * DELETE /api/kullanicilar/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKullanici(@PathVariable Long id) {
        try {
            kullaniciService.deleteKullanici(id);
            return ResponseEntity.ok().body("Kullanıcı başarıyla silindi");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Kullanıcı silinirken bir hata oluştu");
        }
    }
    
    /**
     * Email var mı kontrol et
     * GET /api/kullanicilar/check-email?email={email}
     */
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        try {
            boolean exists = kullaniciService.existsByEmail(email);
            return ResponseEntity.ok(exists);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Toplam kullanıcı sayısını getir
     * GET /api/kullanicilar/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getKullaniciCount() {
        try {
            long count = kullaniciService.getKullaniciCount();
            return ResponseEntity.ok(count);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Role göre kullanıcı sayısını getir
     * GET /api/kullanicilar/count/rol/{rol}
     */
    @GetMapping("/count/rol/{rol}")
    public ResponseEntity<Long> getKullaniciCountByRol(@PathVariable String rol) {
        try {
            long count = kullaniciService.getKullaniciCountByRol(rol);
            return ResponseEntity.ok(count);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
