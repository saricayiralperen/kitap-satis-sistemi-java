package com.alperen.kitapsatissistemi.repository;

import com.alperen.kitapsatissistemi.entity.Favori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * FavoriRepository - .NET C# projesindeki veritabanı işlemlerinden dönüştürülmüştür
 */
@Repository
public interface FavoriRepository extends JpaRepository<Favori, Long> {
    
    /**
     * Kullanıcının tüm favorilerini bulma
     */
    List<Favori> findByKullaniciId(Long kullaniciId);
    
    /**
     * Belirli kitabı favori olarak ekleyen kullanıcıları bulma
     */
    List<Favori> findByKitapId(Long kitapId);
    
    /**
     * Kullanıcı ve kitap ID'sine göre favori bulma
     */
    Optional<Favori> findByKullaniciIdAndKitapId(Long kullaniciId, Long kitapId);
    
    /**
     * Kullanıcının belirli kitabı favori olarak eklemiş mi kontrolü
     */
    boolean existsByKullaniciIdAndKitapId(Long kullaniciId, Long kitapId);
    
    /**
     * Kullanıcının favori sayısını bulma
     */
    long countByKullaniciId(Long kullaniciId);
    
    /**
     * Kitabın favori sayısını bulma
     */
    long countByKitapId(Long kitapId);
    
    /**
     * Kullanıcının favorilerini kitap bilgileri ile birlikte getirme
     */
    @Query("SELECT f FROM Favori f LEFT JOIN FETCH f.kitap k LEFT JOIN FETCH k.kategori WHERE f.kullaniciId = :kullaniciId")
    List<Favori> findByKullaniciIdWithKitap(@Param("kullaniciId") Long kullaniciId);
    
    /**
     * Kullanıcının favorilerini kullanıcı bilgileri ile birlikte getirme
     */
    @Query("SELECT f FROM Favori f LEFT JOIN f.kullanici WHERE f.kitapId = :kitapId")
    List<Favori> findByKitapIdWithKullanici(@Param("kitapId") Long kitapId);
    
    /**
     * Tüm favorileri kitap ve kullanıcı bilgileri ile birlikte getirme
     */
    @Query("SELECT f FROM Favori f LEFT JOIN f.kitap LEFT JOIN f.kullanici")
    List<Favori> findAllWithKitapAndKullanici();
    
    /**
     * En çok favori edilen kitapları bulma
     */
    @Query("SELECT f.kitapId, COUNT(f) as favoriSayisi FROM Favori f GROUP BY f.kitapId ORDER BY favoriSayisi DESC")
    List<Object[]> findMostFavoriteKitaplar();
    
    /**
     * Kullanıcının favori kitap ID'lerini getirme
     */
    @Query("SELECT f.kitapId FROM Favori f WHERE f.kullaniciId = :kullaniciId")
    List<Long> findKitapIdsByKullaniciId(@Param("kullaniciId") Long kullaniciId);
    
    /**
     * Kullanıcı ID'sine göre tüm favorileri silme
     */
    void deleteByKullaniciId(Long kullaniciId);
    
    /**
     * Kitap ID'sine göre tüm favorileri silme
     */
    void deleteByKitapId(Long kitapId);
    
    /**
     * Tüm favorileri kullanıcı ve kitap bilgileri ile birlikte getir
     */
    @Query("SELECT f FROM Favori f LEFT JOIN FETCH f.kullanici LEFT JOIN FETCH f.kitap")
    List<Favori> findAllWithKullaniciAndKitap();
    
    /**
     * En çok favorilenen kitaplar
     */
    @Query("SELECT f.kitapId, COUNT(f) as favoriSayisi FROM Favori f GROUP BY f.kitapId ORDER BY favoriSayisi DESC")
    List<Object[]> findMostFavoritedBooks();
    
    /**
     * En aktif kullanıcılar (en çok favori ekleyen)
     */
    @Query("SELECT f.kullaniciId, COUNT(f) as favoriSayisi FROM Favori f GROUP BY f.kullaniciId ORDER BY favoriSayisi DESC")
    List<Object[]> findMostActiveUsers();
}