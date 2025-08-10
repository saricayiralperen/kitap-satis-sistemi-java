package com.alperen.kitapsatissistemi.service;

import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.repository.KullaniciRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * KullaniciService - .NET C# projesindeki business logic'ten dönüştürülmüştür
 */
@Service
@Transactional
public class KullaniciService {
    
    private final KullaniciRepository kullaniciRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    public KullaniciService(KullaniciRepository kullaniciRepository) {
        this.kullaniciRepository = kullaniciRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    /**
     * Tüm kullanıcıları getir
     */
    @Transactional(readOnly = true)
    public List<Kullanici> getAllKullanicilar() {
        return kullaniciRepository.findAll();
    }
    
    /**
     * ID'ye göre kullanıcı getir
     */
    @Transactional(readOnly = true)
    public Optional<Kullanici> getKullaniciById(Long id) {
        return kullaniciRepository.findById(id);
    }
    
    /**
     * Email'e göre kullanıcı getir
     */
    @Transactional(readOnly = true)
    public Optional<Kullanici> getKullaniciByEmail(String email) {
        return kullaniciRepository.findByEmail(email);
    }
    
    /**
     * Role göre kullanıcıları getir
     */
    @Transactional(readOnly = true)
    public List<Kullanici> getKullanicilarByRol(String rol) {
        return kullaniciRepository.findByRol(rol);
    }
    
    /**
     * Admin kullanıcıları getir
     */
    @Transactional(readOnly = true)
    public List<Kullanici> getAdminKullanicilar() {
        return kullaniciRepository.findByRol("Admin");
    }
    
    /**
     * Normal kullanıcıları getir
     */
    @Transactional(readOnly = true)
    public List<Kullanici> getNormalKullanicilar() {
        return kullaniciRepository.findByRol("User");
    }
    
    /**
     * Ad soyada göre arama
     */
    @Transactional(readOnly = true)
    public List<Kullanici> searchKullanicilarByAdSoyad(String adSoyad) {
        return kullaniciRepository.findByAdSoyadContainingIgnoreCase(adSoyad);
    }
    
    /**
     * Yeni kullanıcı kaydet
     */
    public Kullanici registerKullanici(Kullanici kullanici, String sifre) {
        // Email benzersiz mi kontrol et
        if (kullaniciRepository.existsByEmail(kullanici.getEmail())) {
            throw new RuntimeException("Bu email adresi zaten kayıtlı: " + kullanici.getEmail());
        }
        
        // Şifreyi hash'le
        kullanici.setSifreHash(passwordEncoder.encode(sifre));
        
        // Kayıt tarihini ayarla
        kullanici.setKayitTarihi(LocalDateTime.now());
        
        // Rol belirtilmemişse User yap
        if (kullanici.getRol() == null || kullanici.getRol().isEmpty()) {
            kullanici.setRol("User");
        }
        
        return kullaniciRepository.save(kullanici);
    }
    
    /**
     * Kullanıcı girişi doğrula
     */
    @Transactional(readOnly = true)
    public Optional<Kullanici> authenticateKullanici(String email, String sifre) {
        Optional<Kullanici> kullaniciOpt = kullaniciRepository.findByEmail(email);
        
        if (kullaniciOpt.isPresent()) {
            Kullanici kullanici = kullaniciOpt.get();
            if (passwordEncoder.matches(sifre, kullanici.getSifreHash())) {
                return Optional.of(kullanici);
            }
        }
        
        return Optional.empty();
    }
    
    /**
     * Kullanıcı güncelle
     */
    public Kullanici updateKullanici(Long id, Kullanici kullaniciDetaylari) {
        return kullaniciRepository.findById(id)
                .map(kullanici -> {
                    // Email değiştiriliyorsa benzersizlik kontrolü yap
                    if (!kullanici.getEmail().equals(kullaniciDetaylari.getEmail()) &&
                        kullaniciRepository.existsByEmail(kullaniciDetaylari.getEmail())) {
                        throw new RuntimeException("Bu email adresi zaten kayıtlı: " + kullaniciDetaylari.getEmail());
                    }
                    
                    kullanici.setAdSoyad(kullaniciDetaylari.getAdSoyad());
                    kullanici.setEmail(kullaniciDetaylari.getEmail());
                    kullanici.setRol(kullaniciDetaylari.getRol());
                    
                    return kullaniciRepository.save(kullanici);
                })
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı, ID: " + id));
    }
    
    /**
     * Kullanıcı şifresini değiştir
     */
    public void changePassword(Long id, String eskiSifre, String yeniSifre) {
        Kullanici kullanici = kullaniciRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı, ID: " + id));
        
        // Eski şifreyi doğrula
        if (!passwordEncoder.matches(eskiSifre, kullanici.getSifreHash())) {
            throw new RuntimeException("Eski şifre yanlış");
        }
        
        // Yeni şifreyi hash'le ve kaydet
        kullanici.setSifreHash(passwordEncoder.encode(yeniSifre));
        kullaniciRepository.save(kullanici);
    }
    
    /**
     * Admin tarafından kullanıcı şifresini sıfırla
     */
    public void resetPassword(Long id, String yeniSifre) {
        Kullanici kullanici = kullaniciRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı, ID: " + id));
        
        kullanici.setSifreHash(passwordEncoder.encode(yeniSifre));
        kullaniciRepository.save(kullanici);
    }
    
    /**
     * Kullanıcı sil
     */
    public void deleteKullanici(Long id) {
        if (!kullaniciRepository.existsById(id)) {
            throw new RuntimeException("Kullanıcı bulunamadı, ID: " + id);
        }
        
        // TODO: Bu kullanıcıya ait siparişler varsa silme işlemini engelle
        // SiparisService ile kontrol edilebilir
        
        kullaniciRepository.deleteById(id);
    }
    
    /**
     * Email var mı kontrol et
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return kullaniciRepository.existsByEmail(email);
    }
    
    /**
     * Kullanıcı var mı kontrol et
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return kullaniciRepository.existsById(id);
    }
    
    /**
     * Belirli tarihten sonra kayıt olan kullanıcıları getir
     */
    @Transactional(readOnly = true)
    public List<Kullanici> getKullanicilarByKayitTarihiAfter(LocalDateTime tarih) {
        return kullaniciRepository.findByKayitTarihiAfter(tarih);
    }
    
    /**
     * Belirli tarihten önce kayıt olan kullanıcıları getir
     */
    @Transactional(readOnly = true)
    public List<Kullanici> getKullanicilarByKayitTarihiBefore(LocalDateTime tarih) {
        return kullaniciRepository.findByKayitTarihiBefore(tarih);
    }
    
    /**
     * Toplam kullanıcı sayısı
     */
    @Transactional(readOnly = true)
    public long count() {
        return kullaniciRepository.count();
    }
    
    /**
     * Toplam kullanıcı sayısı (eski metod adı ile uyumluluk için)
     */
    @Transactional(readOnly = true)
    public long getKullaniciCount() {
        return kullaniciRepository.count();
    }
    
    /**
     * Ad soyad veya email'e göre arama (sayfalama ile)
     */
    @Transactional(readOnly = true)
    public Page<Kullanici> findByAdSoyadContainingIgnoreCaseOrEmailContainingIgnoreCase(String adSoyad, String email, Pageable pageable) {
        return kullaniciRepository.findByAdSoyadContainingIgnoreCaseOrEmailContainingIgnoreCase(adSoyad, email, pageable);
    }
    
    /**
     * Role göre kullanıcı sayısını getir
     */
    @Transactional(readOnly = true)
    public long getKullaniciCountByRol(String rol) {
        return kullaniciRepository.countByRol(rol);
    }
    
    // Admin controller için ek metodlar
    
    /**
     * Sayfalama ile tüm kullanıcıları getir
     */
    @Transactional(readOnly = true)
    public Page<Kullanici> findAll(Pageable pageable) {
        return kullaniciRepository.findAll(pageable);
    }
    
    /**
     * Ad soyad ile arama yaparak sayfalama ile kullanıcıları getir
     */
    @Transactional(readOnly = true)
    public Page<Kullanici> findByAdSoyadContainingIgnoreCase(String adSoyad, Pageable pageable) {
        return kullaniciRepository.findByAdSoyadContainingIgnoreCase(adSoyad, pageable);
    }
    
    /**
     * ID ile kullanıcı bul
     */
    @Transactional(readOnly = true)
    public Optional<Kullanici> findById(Long id) {
        return kullaniciRepository.findById(id);
    }
    
    /**
     * Kullanıcı kaydet
     */
    public Kullanici save(Kullanici kullanici) {
        return kullaniciRepository.save(kullanici);
    }
    
    /**
     * Kullanıcıyı ID'ye göre silme
     */
    public void deleteById(Long id) {
        kullaniciRepository.deleteById(id);
    }
}