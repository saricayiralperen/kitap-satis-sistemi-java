package com.alperen.kitapsatissistemi.repository;

import com.alperen.kitapsatissistemi.entity.Favori;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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
    List<Favori> findByKullanici_Id(Long kullaniciId);
    
    /**
     * Kullanıcının tüm favorilerini sayfalama ile bulma
     */
    @EntityGraph(attributePaths = {"kitap", "kitap.kategori"})
    Page<Favori> findByKullanici_Id(Long kullaniciId, Pageable pageable);
    
    /**
     * Belirli kitabı favori olarak ekleyen kullanıcıları bulma
     */
    List<Favori> findByKitap_Id(Long kitapId);
    
    /**
     * Belirli kitabı favori olarak ekleyen kullanıcıları sayfalama ile bulma
     */
    @EntityGraph(attributePaths = {"kullanici", "kitap", "kitap.kategori"})
    Page<Favori> findByKitap_Id(Long kitapId, Pageable pageable);
    
    /**
     * Kullanıcı ve kitap ID'sine göre favori bulma
     */
    Optional<Favori> findByKullanici_IdAndKitap_Id(Long kullaniciId, Long kitapId);
    
    /**
     * Kullanıcının belirli kitabı favori olarak eklemiş mi kontrolü
     */
    boolean existsByKullanici_IdAndKitap_Id(Long kullaniciId, Long kitapId);
    
    /**
     * Kullanıcının favori sayısını bulma
     */
    long countByKullanici_Id(Long kullaniciId);
    
    /**
     * Kitabın favori sayısını bulma
     */
    long countByKitap_Id(Long kitapId);
    
    /**
     * Kullanıcının favorilerini kitap bilgileri ile birlikte getirme
     */
    @Query("SELECT f FROM Favori f LEFT JOIN FETCH f.kitap k LEFT JOIN FETCH k.kategori WHERE f.kullanici.id = :kullanici_Id")
    List<Favori> findByKullanici_IdWithKitap(@Param("kullanici_Id") Long kullaniciId);
    
    /**
     * Kullanıcının favorilerini kullanıcı bilgileri ile birlikte getirme
     */
    @Query("SELECT f FROM Favori f LEFT JOIN f.kullanici WHERE f.kitap.id = :kitap_Id")
    List<Favori> findByKitap_IdWithKullanici(@Param("kitap_Id") Long kitapId);
    
    /**
     * Tüm favorileri kitap ve kullanıcı bilgileri ile birlikte getirme
     */
    @Query("SELECT f FROM Favori f LEFT JOIN f.kitap LEFT JOIN f.kullanici")
    List<Favori> findAllWithKitapAndKullanici();
    
    /**
     * En çok favori edilen kitapları bulma
     */
    @Query("SELECT f.kitap.id, COUNT(f) as favoriSayisi FROM Favori f GROUP BY f.kitap.id ORDER BY favoriSayisi DESC")
    List<Object[]> findMostFavoriteKitaplar();
    
    /**
     * Kullanıcının favori kitap ID'lerini getirme
     */
    @Query("SELECT f.kitap.id FROM Favori f WHERE f.kullanici.id = :kullanici_Id")
    List<Long> findKitapIdsByKullaniciId(@Param("kullanici_Id") Long kullaniciId);
    
    /**
     * Kullanıcı ID'sine göre tüm favorileri silme
     */
    void deleteByKullanici_Id(Long kullaniciId);
    
    /**
     * Kitap ID'sine göre tüm favorileri silme
     */
    void deleteByKitap_Id(Long kitapId);
    
    /**
     * Tüm favorileri kullanıcı ve kitap bilgileri ile birlikte getir
     */
    @Query("SELECT f FROM Favori f LEFT JOIN FETCH f.kullanici LEFT JOIN FETCH f.kitap")
    List<Favori> findAllWithKullaniciAndKitap();
    
    /**
     * En çok favorilenen kitaplar
     */
    @Query("SELECT f.kitap.id, COUNT(f) as favoriSayisi FROM Favori f GROUP BY f.kitap.id ORDER BY favoriSayisi DESC")
    List<Object[]> findMostFavoritedBooks();
    
    /**
     * En aktif kullanıcılar (en çok favori ekleyen)
     */
    @Query("SELECT f.kullanici.id, COUNT(f) as favoriSayisi FROM Favori f GROUP BY f.kullanici.id ORDER BY favoriSayisi DESC")
    List<Object[]> findMostActiveUsers();
}
