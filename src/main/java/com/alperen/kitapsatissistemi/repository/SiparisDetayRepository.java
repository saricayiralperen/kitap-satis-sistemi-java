package com.alperen.kitapsatissistemi.repository;

import com.alperen.kitapsatissistemi.entity.SiparisDetay;
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
    List<SiparisDetay> findBySiparisId(Long siparisId);
    
    /**
     * Kitaba ait sipariş detaylarını bulma
     */
    List<SiparisDetay> findByKitapId(Long kitapId);
    
    /**
     * Sipariş ve kitap ID'sine göre detay bulma
     */
    Optional<SiparisDetay> findBySiparisIdAndKitapId(Long siparisId, Long kitapId);
    
    /**
     * Siparişe ait detayları kitap bilgileri ile birlikte getirme
     */
    @Query("SELECT sd FROM SiparisDetay sd LEFT JOIN sd.kitap LEFT JOIN sd.kitap.kategori WHERE sd.siparisId = :siparisId")
    List<SiparisDetay> findBySiparisIdWithKitap(@Param("siparisId") Long siparisId);
    
    /**
     * Kitaba ait sipariş detaylarını sipariş bilgileri ile birlikte getirme
     */
    @Query("SELECT sd FROM SiparisDetay sd LEFT JOIN sd.siparis LEFT JOIN sd.siparis.kullanici WHERE sd.kitapId = :kitapId")
    List<SiparisDetay> findByKitapIdWithSiparis(@Param("kitapId") Long kitapId);
    
    /**
     * Belirli bir kitabın toplam satış adedini bulma
     */
    @Query("SELECT COALESCE(SUM(sd.adet), 0) FROM SiparisDetay sd JOIN sd.siparis s WHERE sd.kitapId = :kitapId AND s.durum = 'Onaylandı'")
    Long findToplamSatisAdetiByKitapId(@Param("kitapId") Long kitapId);
    
    /**
     * Kitabın toplam satış adedini getir
     */
    @Query("SELECT COALESCE(SUM(sd.adet), 0) FROM SiparisDetay sd JOIN sd.siparis s WHERE sd.kitap.id = :kitapId AND s.durum = 'Onaylandı'")
    Integer getTotalSalesQuantityByKitapId(@Param("kitapId") Long kitapId);
    
    @Query("SELECT sd FROM SiparisDetay sd JOIN FETCH sd.kitap JOIN FETCH sd.siparis")
    List<SiparisDetay> findAllWithKitapAndSiparis();
    
    /**
     * Belirli bir kitabın toplam satış tutarını bulma
     */
    @Query("SELECT COALESCE(SUM(sd.fiyat * sd.adet), 0) FROM SiparisDetay sd JOIN sd.siparis s WHERE sd.kitapId = :kitapId AND s.durum = 'Onaylandı'")
    BigDecimal findToplamSatisTutariByKitapId(@Param("kitapId") Long kitapId);
    
    /**
     * En çok satılan kitapları bulma
     */
    @Query("SELECT sd.kitapId, SUM(sd.adet) as toplamAdet FROM SiparisDetay sd JOIN sd.siparis s WHERE s.durum = 'Onaylandı' GROUP BY sd.kitapId ORDER BY toplamAdet DESC")
    List<Object[]> findEnCokSatilanKitaplar();
    
    /**
     * En çok gelir getiren kitapları bulma
     */
    @Query("SELECT sd.kitapId, SUM(sd.fiyat * sd.adet) as toplamGelir FROM SiparisDetay sd JOIN sd.siparis s WHERE s.durum = 'Onaylandı' GROUP BY sd.kitapId ORDER BY toplamGelir DESC")
    List<Object[]> findEnCokGelirGetirenKitaplar();
    
    /**
     * Siparişin toplam tutarını hesaplama
     */
    @Query("SELECT COALESCE(SUM(sd.fiyat * sd.adet), 0) FROM SiparisDetay sd WHERE sd.siparisId = :siparisId")
    BigDecimal calculateToplamTutarBySiparisId(@Param("siparisId") Long siparisId);
    
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
    long countBySiparisId(Long siparisId);
    
    /**
     * Kitap ID'sine göre sipariş detayı sayısını getir
     */
    long countByKitapId(Long kitapId);
}