package com.alperen.kitapsatissistemi.repository;

import com.alperen.kitapsatissistemi.entity.Siparis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * SiparisRepository - .NET C# projesindeki veritabanı işlemlerinden dönüştürülmüştür
 */
@Repository
public interface SiparisRepository extends JpaRepository<Siparis, Long> {
    
    /**
     * Kullanıcının tüm siparişlerini bulma
     */
    List<Siparis> findByKullanici_Id(Long kullaniciId);
    
    /**
     * Duruma göre siparişleri bulma
     */
    List<Siparis> findByDurum(String durum);
    
    /**
     * Kullanıcı ve duruma göre siparişleri bulma
     */
    List<Siparis> findByKullanici_IdAndDurum(Long kullaniciId, String durum);
    
    /**
     * Belirli tarihten sonraki siparişleri bulma
     */
    List<Siparis> findBySiparisTarihiAfter(LocalDateTime tarih);
    
    /**
     * Belirli tarihten önceki siparişleri bulma
     */
    List<Siparis> findBySiparisTarihiBefore(LocalDateTime tarih);
    
    /**
     * Belirli tarih aralığındaki siparişleri bulma
     */
    List<Siparis> findBySiparisTarihiBetween(LocalDateTime baslangic, LocalDateTime bitis);
    
    /**
     * Toplam tutar aralığına göre siparişleri bulma
     */
    List<Siparis> findByToplamTutarBetween(BigDecimal minTutar, BigDecimal maxTutar);
    
    /**
     * Kullanıcının siparişlerini sipariş detayları ile birlikte getirme
     */
    @Query("SELECT s FROM Siparis s LEFT JOIN FETCH s.siparisDetaylari WHERE s.kullanici.id = :kullanici_Id")
    List<Siparis> findByKullanici_IdWithDetaylar(@Param("kullanici_Id") Long kullaniciId);
    
    /**
     * Sipariş detayları ile birlikte sipariş getirme
     */
    @Query("SELECT s FROM Siparis s LEFT JOIN FETCH s.siparisDetaylari sd LEFT JOIN FETCH sd.kitap WHERE s.id = :id")
    Optional<Siparis> findByIdWithDetaylar(@Param("id") Long id);
    
    /**
     * Kullanıcının sipariş sayısını bulma
     */
    long countByKullanici_Id(Long kullaniciId);
    
    /**
     * Duruma göre sipariş sayısını bulma
     */
    long countByDurum(String durum);
    
    /**
     * Kullanıcının toplam harcamasını bulma
     */
    @Query("SELECT COALESCE(SUM(s.toplamTutar), 0) FROM Siparis s WHERE s.kullanici.id = :kullanici_Id AND s.durum = 'Onaylandı'")
    BigDecimal findToplamHarcamaByKullanici_Id(@Param("kullanici_Id") Long kullaniciId);
    
    /**
     * En son siparişleri bulma
     */
    @Query("SELECT s FROM Siparis s ORDER BY s.siparisTarihi DESC")
    List<Siparis> findLatestSiparisler();
    
    /**
     * En yüksek tutarlı siparişleri bulma
     */
    @Query("SELECT s FROM Siparis s ORDER BY s.toplamTutar DESC")
    List<Siparis> findTopSiparisByTutar();
    
    /**
     * Beklemedeki siparişleri bulma
     */
    @Query("SELECT s FROM Siparis s WHERE s.durum = 'Beklemede' ORDER BY s.siparisTarihi ASC")
    List<Siparis> findBeklemedekiSiparisler();
    
    /**
     * Onaylanmış siparişleri bulma
     */
    @Query("SELECT s FROM Siparis s WHERE s.durum = 'Onaylandı' ORDER BY s.siparisTarihi DESC")
    List<Siparis> findOnaylanmisSiparisler();
    
    /**
     * İptal edilmiş siparişleri bulma
     */
    @Query("SELECT s FROM Siparis s WHERE s.durum = 'İptal Edildi' ORDER BY s.siparisTarihi DESC")
    List<Siparis> findIptalEdilmisSiparisler();
    
    /**
     * Sayfalama ile durum bazlı siparişler
     */
    @EntityGraph(attributePaths = {"kullanici", "siparisDetaylari", "siparisDetaylari.kitap"})
    Page<Siparis> findByDurum(String durum, Pageable pageable);
    
    /**
     * Günlük satış istatistikleri
     */
    @Query("SELECT CAST(s.siparisTarihi AS date) as tarih, COUNT(s) as adet, SUM(s.toplamTutar) as toplam FROM Siparis s WHERE s.durum = 'Onaylandı' GROUP BY CAST(s.siparisTarihi AS date) ORDER BY CAST(s.siparisTarihi AS date) DESC")
    List<Object[]> findGunlukSatisIstatistikleri();
    
    /**
     * Aylık satış istatistikleri
     */
    @Query("SELECT YEAR(s.siparisTarihi) as yil, MONTH(s.siparisTarihi) as ay, COUNT(s) as adet, SUM(s.toplamTutar) as toplam FROM Siparis s WHERE s.durum = 'Onaylandı' GROUP BY YEAR(s.siparisTarihi), MONTH(s.siparisTarihi) ORDER BY YEAR(s.siparisTarihi) DESC, MONTH(s.siparisTarihi) DESC")
    List<Object[]> getMonthlySalesStats();
    
    /**
     * En çok alışveriş yapan müşteriler
     */
    @Query("SELECT s.kullanici.id, COUNT(s) as siparisAdet, SUM(s.toplamTutar) as toplamHarcama FROM Siparis s WHERE s.durum = 'Onaylandı' GROUP BY s.kullanici.id ORDER BY toplamHarcama DESC")
    List<Object[]> getTopCustomers();
    
    /**
     * Tüm siparişleri detaylarıyla birlikte getir
     */
    @Query("SELECT s FROM Siparis s LEFT JOIN FETCH s.siparisDetaylari")
    List<Siparis> findAllWithDetails();
    
    /**
     * ID'ye göre sipariş detaylarıyla birlikte getir
     */
    @Query("SELECT s FROM Siparis s LEFT JOIN FETCH s.siparisDetaylari WHERE s.id = :id")
    Optional<Siparis> findByIdWithDetails(@Param("id") Long id);
    
    /**
     * Kullanıcı ID'ye göre siparişleri detaylarıyla birlikte getir
     */
    @Query("SELECT s FROM Siparis s LEFT JOIN FETCH s.siparisDetaylari WHERE s.kullanici.id = :kullanici_Id")
    List<Siparis> findByKullanici_IdWithDetails(@Param("kullanici_Id") Long kullaniciId);
    
    /**
     * Son eklenen siparişleri getir (ID'ye göre azalan sırada)
     */
    @Query("SELECT s FROM Siparis s ORDER BY s.id DESC")
    List<Siparis> findTopByOrderByIdDesc(@Param("limit") int limit);
}
