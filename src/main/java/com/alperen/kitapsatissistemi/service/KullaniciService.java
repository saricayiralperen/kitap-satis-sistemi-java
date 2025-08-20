package com.alperen.kitapsatissistemi.service;

import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.DuplicateEntityException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.repository.KullaniciRepository;
import com.alperen.kitapsatissistemi.repository.SiparisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    private final PasswordEncoder passwordEncoder;
    private final SiparisRepository siparisRepository;
    
    @Autowired
    public KullaniciService(KullaniciRepository kullaniciRepository, PasswordEncoder passwordEncoder, SiparisRepository siparisRepository) {
        this.kullaniciRepository = kullaniciRepository;
        this.passwordEncoder = passwordEncoder;
        this.siparisRepository = siparisRepository;
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
        // Email'i normalize et
        String normalizedEmail = email != null ? email.trim().toLowerCase() : "";
        return kullaniciRepository.findByEmail(normalizedEmail);
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
        // Input validation
        if (kullanici == null) {
            throw new BusinessException("Kullanıcı bilgisi boş olamaz");
        }
        
        if (!StringUtils.hasText(sifre)) {
            throw new BusinessException("Şifre boş olamaz");
        }
        
        if (!StringUtils.hasText(kullanici.getEmail())) {
            throw new BusinessException("Email adresi boş olamaz");
        }
        
        if (!StringUtils.hasText(kullanici.getAdSoyad())) {
            throw new BusinessException("Ad soyad boş olamaz");
        }
        
        // Email ve ad soyad temizle
        kullanici.setEmail(kullanici.getEmail().trim().toLowerCase());
        kullanici.setAdSoyad(kullanici.getAdSoyad().trim());
        
        // Şifre uzunluk kontrolü
        if (sifre.length() < 6) {
            throw new BusinessException("Şifre en az 6 karakter olmalıdır");
        }
        
        // Email benzersiz mi kontrol et
        if (kullaniciRepository.existsByEmail(kullanici.getEmail())) {
            throw new DuplicateEntityException("Kullanıcı", "email", kullanici.getEmail());
        }
        
        // Şifreyi hash'le
        kullanici.setSifreHash(passwordEncoder.encode(sifre));
        
        // Kayıt tarihini ayarla
        kullanici.setKayitTarihi(LocalDateTime.now());
        
        // Rol belirtilmemişse User yap
        if (!StringUtils.hasText(kullanici.getRol())) {
            kullanici.setRol("User");
        }
        
        return kullaniciRepository.save(kullanici);
    }
    
    /**
     * Kullanıcı girişi doğrula
     */
    @Transactional(readOnly = true)
    public Optional<Kullanici> authenticateKullanici(String email, String sifre) {
        // Email'i normalize et (kayıt sırasında da toLowerCase yapılıyor)
        String normalizedEmail = email != null ? email.trim().toLowerCase() : "";
        Optional<Kullanici> kullaniciOpt = kullaniciRepository.findByEmail(normalizedEmail);
        
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
        // Input validation
        if (id == null) {
            throw new BusinessException("Kullanıcı ID'si boş olamaz");
        }
        
        if (kullaniciDetaylari == null) {
            throw new BusinessException("Kullanıcı bilgisi boş olamaz");
        }
        
        if (!StringUtils.hasText(kullaniciDetaylari.getEmail())) {
            throw new BusinessException("Email adresi boş olamaz");
        }
        
        if (!StringUtils.hasText(kullaniciDetaylari.getAdSoyad())) {
            throw new BusinessException("Ad soyad boş olamaz");
        }
        
        // Email ve ad soyad temizle
        kullaniciDetaylari.setEmail(kullaniciDetaylari.getEmail().trim().toLowerCase());
        kullaniciDetaylari.setAdSoyad(kullaniciDetaylari.getAdSoyad().trim());
        
        return kullaniciRepository.findById(id)
                .map(kullanici -> {
                    // Email değiştiriliyorsa benzersizlik kontrolü yap
                    if (!kullanici.getEmail().equals(kullaniciDetaylari.getEmail()) &&
                        kullaniciRepository.existsByEmail(kullaniciDetaylari.getEmail())) {
                        throw new DuplicateEntityException("Kullanıcı", "email", kullaniciDetaylari.getEmail());
                    }
                    
                    kullanici.setAdSoyad(kullaniciDetaylari.getAdSoyad());
                    kullanici.setEmail(kullaniciDetaylari.getEmail());
                    if (StringUtils.hasText(kullaniciDetaylari.getRol())) {
                        kullanici.setRol(kullaniciDetaylari.getRol());
                    }
                    
                    return kullaniciRepository.save(kullanici);
                })
                .orElseThrow(() -> new EntityNotFoundBusinessException("Kullanıcı", id));
    }
    
    /**
     * Kullanıcı şifresini değiştir
     */
    public void changePassword(Long id, String eskiSifre, String yeniSifre) {
        // Input validation
        if (id == null) {
            throw new BusinessException("Kullanıcı ID'si boş olamaz");
        }
        if (!StringUtils.hasText(eskiSifre)) {
            throw new BusinessException("Eski şifre boş olamaz");
        }
        if (!StringUtils.hasText(yeniSifre)) {
            throw new BusinessException("Yeni şifre boş olamaz");
        }
        if (yeniSifre.length() < 6) {
            throw new BusinessException("Yeni şifre en az 6 karakter olmalıdır");
        }
        
        Kullanici kullanici = kullaniciRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundBusinessException("Kullanıcı", id));
        
        // Eski şifreyi doğrula
        if (!passwordEncoder.matches(eskiSifre, kullanici.getSifreHash())) {
            throw new BusinessException("Eski şifre yanlış");
        }
        
        // Yeni şifreyi hash'le ve kaydet
        kullanici.setSifreHash(passwordEncoder.encode(yeniSifre));
        kullaniciRepository.save(kullanici);
    }
    
    /**
     * Admin tarafından kullanıcı şifresini sıfırla
     */
    public void resetPassword(Long id, String yeniSifre) {
        // Input validation
        if (id == null) {
            throw new BusinessException("Kullanıcı ID'si boş olamaz");
        }
        if (!StringUtils.hasText(yeniSifre)) {
            throw new BusinessException("Yeni şifre boş olamaz");
        }
        if (yeniSifre.length() < 6) {
            throw new BusinessException("Yeni şifre en az 6 karakter olmalıdır");
        }
        
        Kullanici kullanici = kullaniciRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundBusinessException("Kullanıcı", id));
        
        kullanici.setSifreHash(passwordEncoder.encode(yeniSifre));
        kullaniciRepository.save(kullanici);
    }
    
    /**
     * Kullanıcı sayısını getir
     */
    @Transactional(readOnly = true)
    public long getKullaniciCount() {
        return kullaniciRepository.count();
    }
    
    /**
     * Son eklenen kullanıcıları getir
     */
    @Transactional(readOnly = true)
    public List<Kullanici> getLatestKullanicilar(int limit) {
        return kullaniciRepository.findTopByOrderByKayitTarihiDesc(limit);
    }
    
    /**
     * Kullanıcı sil
     */
    public void deleteKullanici(Long id) {
        // Input validation
        if (id == null) {
            throw new BusinessException("Kullanıcı ID'si boş olamaz");
        }
        
        // Kullanıcı var mı kontrol et
        Kullanici kullanici = kullaniciRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundBusinessException("Kullanıcı", id));
        
        // Bu kullanıcıya ait siparişler varsa silme işlemini engelle
        // Sipariş sayısını repository üzerinden kontrol et
        long siparisSayisi = siparisRepository.countByKullanici_Id(kullanici.getId());
        if (siparisSayisi > 0) {
            throw new BusinessException("Bu kullanıcıya ait siparişler bulunduğu için silinemez.");
        }
        
        kullaniciRepository.deleteById(id);
    }
    
    /**
     * Email var mı kontrol et
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        // Email'i normalize et
        String normalizedEmail = email != null ? email.trim().toLowerCase() : "";
        return kullaniciRepository.existsByEmail(normalizedEmail);
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
