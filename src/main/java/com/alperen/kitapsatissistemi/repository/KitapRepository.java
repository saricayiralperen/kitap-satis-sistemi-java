package com.alperen.kitapsatissistemi.repository;

import com.alperen.kitapsatissistemi.entity.Kitap;
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
 * KitapRepository - .NET C# projesindeki veritabanı işlemlerinden dönüştürülmüştür
 */
@Repository
public interface KitapRepository extends JpaRepository<Kitap, Long> {
    
    /**
     * Kitap adına göre arama
     */
    List<Kitap> findByAdContainingIgnoreCase(String ad);
    
    /**
     * Yazar adına göre arama
     */
    List<Kitap> findByYazarContainingIgnoreCase(String yazar);
    
    /**
     * Kategori ID'sine göre kitapları bulma
     */
    List<Kitap> findByKategori_Id(Long kategoriId);
    
    /**
     * Fiyat aralığına göre kitapları bulma
     */
    List<Kitap> findByFiyatBetween(BigDecimal minFiyat, BigDecimal maxFiyat);
    
    /**
     * Kitap adı ve yazar adına göre arama
     */
    @Query("SELECT k FROM Kitap k WHERE " +
           "(:ad IS NULL OR LOWER(k.ad) LIKE LOWER(CONCAT('%', :ad, '%'))) AND " +
           "(:yazar IS NULL OR LOWER(k.yazar) LIKE LOWER(CONCAT('%', :yazar, '%')))")
    List<Kitap> findByAdAndYazar(@Param("ad") String ad, @Param("yazar") String yazar);
    
    /**
     * Kategori ID'sine göre kitap sayısını bulma
     */
    long countByKategori_Id(Long kategoriId);
    
    /**
     * En pahalı kitapları bulma
     */
    @Query("SELECT k FROM Kitap k ORDER BY k.fiyat DESC")
    List<Kitap> findTopByOrderByFiyatDesc();
    
    /**
     * En ucuz kitapları bulma
     */
    @Query("SELECT k FROM Kitap k ORDER BY k.fiyat ASC")
    List<Kitap> findTopByOrderByFiyatAsc();
    
    /**
     * Belirli bir fiyatın üzerindeki kitapları bulma
     */
    List<Kitap> findByFiyatGreaterThan(BigDecimal fiyat);
    
    /**
     * Belirli bir fiyatın altındaki kitapları bulma
     */
    List<Kitap> findByFiyatLessThan(BigDecimal fiyat);
    
    /**
     * Kategori ile birlikte kitapları getirme
     */
    @Query("SELECT k FROM Kitap k LEFT JOIN k.kategori WHERE k.id = :id")
    Optional<Kitap> findByIdWithKategori(@Param("id") Long id);
    
    /**
     * Tüm kitapları kategori ile birlikte getirme
     */
    @Query("SELECT k FROM Kitap k LEFT JOIN k.kategori")
    List<Kitap> findAllWithKategori();
    
    /**
     * Resim URL'si olan kitapları bulma
     */
    @Query("SELECT k FROM Kitap k WHERE k.resimUrl IS NOT NULL AND k.resimUrl != ''")
    List<Kitap> findKitaplarWithResim();

    // Thymeleaf template'ler için ek metodlar
    
    /**
     * Sayfalama ile tüm kitapları kategori bilgileri ile birlikte getir
     */
    @EntityGraph(attributePaths = {"kategori"})
    @Override
    Page<Kitap> findAll(Pageable pageable);
    
    /**
     * Sayfalama ile kitap adına göre arama (kategori bilgileri ile)
     */
    @EntityGraph(attributePaths = {"kategori"})
    Page<Kitap> findByAdContainingIgnoreCase(String ad, Pageable pageable);
    
    /**
     * Sayfalama ile kategori ID'sine göre kitapları bulma (kategori bilgileri ile)
     */
    @EntityGraph(attributePaths = {"kategori"})
    Page<Kitap> findByKategori_Id(Long kategoriId, Pageable pageable);
    
    /**
     * Sayfalama ile kitap adı ve kategori ID'sine göre arama (kategori bilgileri ile)
     */
    @EntityGraph(attributePaths = {"kategori"})
    Page<Kitap> findByAdContainingIgnoreCaseAndKategori_Id(String ad, Long kategoriId, Pageable pageable);
    
    /**
     * Kategori ID'ye göre kitapları getir (belirli ID hariç)
     */
    List<Kitap> findByKategori_IdAndIdNot(Long kategoriId, Long excludeId);
    
    /**
     * Son eklenen kitapları getir (ID'ye göre azalan sırada)
     */
    @Query("SELECT k FROM Kitap k ORDER BY k.id DESC")
    List<Kitap> findTopByOrderByIdDesc(@Param("limit") int limit);
}
