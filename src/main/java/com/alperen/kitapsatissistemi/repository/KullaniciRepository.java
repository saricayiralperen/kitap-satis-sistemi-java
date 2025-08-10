package com.alperen.kitapsatissistemi.repository;

import com.alperen.kitapsatissistemi.entity.Kullanici;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * KullaniciRepository - .NET C# projesindeki veritabanı işlemlerinden dönüştürülmüştür
 */
@Repository
public interface KullaniciRepository extends JpaRepository<Kullanici, Long> {
    
    /**
     * Email adresine göre kullanıcı bulma (login için)
     */
    Optional<Kullanici> findByEmail(String email);
    
    /**
     * Email adresinin varlığını kontrol etme
     */
    boolean existsByEmail(String email);
    
    /**
     * Email adresinin varlığını kontrol etme (güncelleme için)
     */
    @Query("SELECT COUNT(k) > 0 FROM Kullanici k WHERE k.email = :email AND k.id != :id")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);
    
    /**
     * Role göre kullanıcıları bulma
     */
    List<Kullanici> findByRol(String rol);
    
    /**
     * Admin kullanıcıları bulma
     */
    @Query("SELECT k FROM Kullanici k WHERE k.rol = 'Admin'")
    List<Kullanici> findAdminKullanicilar();
    
    /**
     * Normal kullanıcıları bulma
     */
    @Query("SELECT k FROM Kullanici k WHERE k.rol = 'User'")
    List<Kullanici> findNormalKullanicilar();
    
    /**
     * Ad soyad ile arama
     */
    List<Kullanici> findByAdSoyadContainingIgnoreCase(String adSoyad);
    
    /**
     * Belirli tarihten sonra kayıt olan kullanıcıları bulma
     */
    List<Kullanici> findByKayitTarihiAfter(LocalDateTime tarih);
    
    /**
     * Belirli tarihten önce kayıt olan kullanıcıları bulma
     */
    List<Kullanici> findByKayitTarihiBefore(LocalDateTime tarih);
    
    /**
     * Belirli tarih aralığında kayıt olan kullanıcıları bulma
     */
    List<Kullanici> findByKayitTarihiBetween(LocalDateTime baslangic, LocalDateTime bitis);
    
    /**
     * Kullanıcı sayısını role göre bulma
     */
    long countByRol(String rol);
    
    /**
     * En son kayıt olan kullanıcıları bulma
     */
    @Query("SELECT k FROM Kullanici k ORDER BY k.kayitTarihi DESC")
    List<Kullanici> findLatestKullanicilar();
    
    /**
     * Email ve role göre kullanıcı bulma
     */
    Optional<Kullanici> findByEmailAndRol(String email, String rol);
    
    /**
     * Ad soyad ile arama yaparak sayfalama ile kullanıcıları bulma
     */
    Page<Kullanici> findByAdSoyadContainingIgnoreCase(String adSoyad, Pageable pageable);
    
    /**
     * Ad soyad veya email'e göre arama (sayfalama ile)
     */
    Page<Kullanici> findByAdSoyadContainingIgnoreCaseOrEmailContainingIgnoreCase(String adSoyad, String email, Pageable pageable);
}