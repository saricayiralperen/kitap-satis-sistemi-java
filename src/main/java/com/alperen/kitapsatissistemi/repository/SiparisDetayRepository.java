package com.alperen.kitapsatissistemi.repository;

import com.alperen.kitapsatissistemi.entity.SiparisDetay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * SiparisDetayRepository - .NET C# projesindeki veritabanı işlemlerinden dönüştürülmüştür
 */
@Repository
public interface SiparisDetayRepository extends JpaRepository<SiparisDetay, Long> {
    
    /**
     * Siparişe ait detayları bulma
     */
    List<SiparisDetay> findBySiparis_Id(Long siparisId);
    
    /**
     * Siparişe ait detayları sayfalama ile bulma
     */
    @EntityGraph(attributePaths = {"kitap", "kitap.kategori", "siparis", "siparis.kullanici"})
    Page<SiparisDetay> findBySiparis_Id(Long siparisId, Pageable pageable);
    
    /**
     * Kitaba ait sipariş detaylarını bulma
     */
    List<SiparisDetay> findByKitap_Id(Long kitapId);
    
    /**
     * Kitaba ait sipariş detaylarını sayfalama ile bulma
     */
    @EntityGraph(attributePaths = {"kitap", "kitap.kategori", "siparis", "siparis.kullanici"})
    Page<SiparisDetay> findByKitap_Id(Long kitapId, Pageable pageable);
    
    /**
     * Sipariş ve kitap ID'sine göre detay bulma
     */
    Optional<SiparisDetay> findBySiparis_IdAndKitap_Id(Long siparisId, Long kitapId);
    
    /**
     * Siparişe ait detayları kitap bilgileri ile birlikte getirme
     */
    @Query("SELECT sd FROM SiparisDetay sd LEFT JOIN FETCH sd.kitap k LEFT JOIN FETCH k.kategori WHERE sd.siparis.id = :siparis_Id")
    List<SiparisDetay> findBySiparis_IdWithKitap(@Param("siparis_Id") Long siparisId);
    
    /**
     * Kitaba ait sipariş detaylarını sipariş bilgileri ile birlikte getirme
     */
    @Query("SELECT sd FROM SiparisDetay sd LEFT JOIN FETCH sd.siparis s LEFT JOIN FETCH s.kullanici WHERE sd.kitap.id = :kitap_Id")
    List<SiparisDetay> findByKitap_IdWithSiparis(@Param("kitap_Id") Long kitapId);
    
    /**
     * Belirli bir kitabın toplam satış adedini bulma
     */
    @Query("SELECT COALESCE(SUM(sd.adet), 0) FROM SiparisDetay sd JOIN sd.siparis s WHERE sd.kitap.id = :kitap_Id AND s.durum = 'Onaylandı'")
    Long findToplamSatisAdetiByKitap_Id(@Param("kitap_Id") Long kitapId);
    
    /**
     * Kitabın toplam satış adedini getir
     */
    @Query("SELECT COALESCE(SUM(sd.adet), 0) FROM SiparisDetay sd JOIN sd.siparis s WHERE sd.kitap.id = :kitap_Id AND s.durum = 'Onaylandı'")
    Integer getTotalSalesQuantityByKitap_Id(@Param("kitap_Id") Long kitapId);
    
    @Query("SELECT sd FROM SiparisDetay sd JOIN FETCH sd.kitap JOIN FETCH sd.siparis")
    List<SiparisDetay> findAllWithKitapAndSiparis();
    
    /**
     * Belirli bir kitabın toplam satış tutarını bulma
     */
    @Query("SELECT COALESCE(SUM(sd.fiyat * sd.adet), 0) FROM SiparisDetay sd JOIN sd.siparis s WHERE sd.kitap.id = :kitap_Id AND s.durum = 'Onaylandı'")
    BigDecimal findToplamSatisTutariByKitap_Id(@Param("kitap_Id") Long kitapId);
    
    /**
     * En çok satılan kitapları bulma
     */
    @Query("SELECT sd.kitap.id, SUM(sd.adet) as toplamAdet FROM SiparisDetay sd JOIN sd.siparis s WHERE s.durum = 'Onaylandı' GROUP BY sd.kitap.id ORDER BY toplamAdet DESC")
    List<Object[]> findEnCokSatilanKitaplar();
    
    /**
     * En çok gelir getiren kitapları bulma
     */
    @Query("SELECT sd.kitap.id, SUM(sd.fiyat * sd.adet) as toplamGelir FROM SiparisDetay sd JOIN sd.siparis s WHERE s.durum = 'Onaylandı' GROUP BY sd.kitap.id ORDER BY toplamGelir DESC")
    List<Object[]> findEnCokGelirGetirenKitaplar();
    
    /**
     * Siparişin toplam tutarını hesaplama
     */
    @Query("SELECT COALESCE(SUM(sd.fiyat * sd.adet), 0) FROM SiparisDetay sd WHERE sd.siparis.id = :siparis_Id")
    BigDecimal calculateToplamTutarBySiparis_Id(@Param("siparis_Id") Long siparisId);
    
    /**
     * Belirli adet üzerindeki sipariş detaylarını bulma
     */
    List<SiparisDetay> findByAdetGreaterThan(Integer adet);
    
    /**
     * Belirli fiyat üzerindeki sipariş detaylarını bulma
     */
    List<SiparisDetay> findByFiyatGreaterThan(BigDecimal fiyat);
    
    /**
     * Kategori bazında satış istatistikleri
     */
    @Query("SELECT k.kategori.ad, SUM(sd.adet), SUM(sd.fiyat * sd.adet) FROM SiparisDetay sd JOIN sd.kitap k JOIN sd.siparis s WHERE s.durum = 'Onaylandı' GROUP BY k.kategori.ad ORDER BY SUM(sd.fiyat * sd.adet) DESC")
    List<Object[]> findKategoriBazindaSatisIstatistikleri();
    
    /**
     * Yazar bazında satış istatistikleri
     */
    @Query("SELECT k.yazar, SUM(sd.adet), SUM(sd.fiyat * sd.adet) FROM SiparisDetay sd JOIN sd.kitap k JOIN sd.siparis s WHERE s.durum = 'Onaylandı' GROUP BY k.yazar ORDER BY SUM(sd.fiyat * sd.adet) DESC")
    List<Object[]> findYazarBazindaSatisIstatistikleri();
    
    /**
     * Kategoriye göre satış istatistikleri
     */
    @Query("SELECT k.kategori.ad, SUM(sd.adet), SUM(sd.fiyat * sd.adet) FROM SiparisDetay sd JOIN sd.kitap k JOIN sd.siparis s WHERE s.durum = 'Onaylandı' GROUP BY k.kategori.ad ORDER BY SUM(sd.fiyat * sd.adet) DESC")
    List<Object[]> getSalesStatsByCategory();
    
    /**
     * Yazara göre satış istatistikleri
     */
    @Query("SELECT k.yazar, SUM(sd.adet), SUM(sd.fiyat * sd.adet) FROM SiparisDetay sd JOIN sd.kitap k JOIN sd.siparis s WHERE s.durum = 'Onaylandı' GROUP BY k.yazar ORDER BY SUM(sd.fiyat * sd.adet) DESC")
    List<Object[]> getSalesStatsByAuthor();
    
    /**
     * Sipariş ID'sine göre sipariş detayı sayısını getir
     */
    long countBySiparis_Id(Long siparisId);
    
    /**
     * Kitap ID'sine göre sipariş detayı sayısını getir
     */
    long countByKitap_Id(Long kitapId);
}
