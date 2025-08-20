package com.alperen.kitapsatissistemi.repository;

import com.alperen.kitapsatissistemi.entity.Kategori;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * KategoriRepository - .NET C# projesindeki veritabanı işlemlerinden dönüştürülmüştür
 */
@Repository
public interface KategoriRepository extends JpaRepository<Kategori, Long> {
    
    /**
     * Kategori adına göre arama
     */
    Optional<Kategori> findByAd(String ad);
    
    /**
     * Kategori adında belirli bir kelime geçen kategorileri bulma
     */
    List<Kategori> findByAdContainingIgnoreCase(String ad);
    
    /**
     * Açıklaması olan kategorileri bulma
     */
    @Query("SELECT k FROM Kategori k WHERE k.aciklama IS NOT NULL AND k.aciklama != ''")
    List<Kategori> findKategorilerWithAciklama();
    
    /**
     * Kategori adının varlığını kontrol etme (güncelleme için)
     */
    @Query("SELECT COUNT(k) > 0 FROM Kategori k WHERE k.ad = :ad AND k.id != :id")
    boolean existsByAdAndIdNot(@Param("ad") String ad, @Param("id") Long id);
    
    /**
     * Kategori adının varlığını kontrol etme (yeni kayıt için)
     */
    boolean existsByAd(String ad);
    
    /**
     * Ad ile arama yaparak sayfalama ile kategorileri bulma
     */
    Page<Kategori> findByAdContainingIgnoreCase(String ad, Pageable pageable);
}
