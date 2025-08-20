package com.alperen.kitapsatissistemi.service;

import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.DuplicateEntityException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.repository.KategoriRepository;
import com.alperen.kitapsatissistemi.repository.KitapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * KategoriService - .NET C# projesindeki business logic'ten dönüştürülmüştür
 */
@Service
@Transactional
public class KategoriService {
    
    private final KategoriRepository kategoriRepository;
    private final KitapRepository kitapRepository;
    
    @Autowired
    public KategoriService(KategoriRepository kategoriRepository, KitapRepository kitapRepository) {
        this.kategoriRepository = kategoriRepository;
        this.kitapRepository = kitapRepository;
    }
    
    /**
     * Tüm kategorileri getir
     */
    @Transactional(readOnly = true)
    public List<Kategori> getAllKategoriler() {
        return kategoriRepository.findAll();
    }
    
    /**
     * ID'ye göre kategori getir
     */
    @Transactional(readOnly = true)
    public Optional<Kategori> getKategoriById(Long id) {
        return kategoriRepository.findById(id);
    }
    
    /**
     * Kategori adına göre kategori getir
     */
    @Transactional(readOnly = true)
    public Optional<Kategori> getKategoriByAd(String ad) {
        return kategoriRepository.findByAd(ad);
    }
    
    /**
     * Kategori adında arama yap
     */
    @Transactional(readOnly = true)
    public List<Kategori> searchKategorilerByAd(String ad) {
        return kategoriRepository.findByAdContainingIgnoreCase(ad);
    }
    
    /**
     * Yeni kategori oluştur
     */
    public Kategori createKategori(Kategori kategori) {
        // Input validation
        if (kategori == null) {
            throw new BusinessException("Kategori bilgisi boş olamaz");
        }
        
        if (!StringUtils.hasText(kategori.getAd())) {
            throw new BusinessException("Kategori adı boş olamaz");
        }
        
        // Kategori adını temizle
        kategori.setAd(kategori.getAd().trim());
        
        // Kategori adının benzersiz olup olmadığını kontrol et
        if (kategoriRepository.existsByAd(kategori.getAd())) {
            throw new DuplicateEntityException("Kategori", "ad", kategori.getAd());
        }
        
        return kategoriRepository.save(kategori);
    }
    
    /**
     * Kategori güncelle
     */
    public Kategori updateKategori(Long id, Kategori kategoriDetaylari) {
        // Input validation
        if (id == null) {
            throw new BusinessException("Kategori ID'si boş olamaz");
        }
        
        if (kategoriDetaylari == null) {
            throw new BusinessException("Kategori bilgisi boş olamaz");
        }
        
        if (!StringUtils.hasText(kategoriDetaylari.getAd())) {
            throw new BusinessException("Kategori adı boş olamaz");
        }
        
        // Kategori adını temizle
        kategoriDetaylari.setAd(kategoriDetaylari.getAd().trim());
        
        return kategoriRepository.findById(id)
                .map(kategori -> {
                    // Kategori adının benzersiz olup olmadığını kontrol et (kendi ID'si hariç)
                    if (kategoriRepository.existsByAdAndIdNot(kategoriDetaylari.getAd(), id)) {
                        throw new DuplicateEntityException("Kategori", "ad", kategoriDetaylari.getAd());
                    }
                    
                    kategori.setAd(kategoriDetaylari.getAd());
                    kategori.setAciklama(kategoriDetaylari.getAciklama());
                    return kategoriRepository.save(kategori);
                })
                .orElseThrow(() -> new EntityNotFoundBusinessException("Kategori", id));
    }
    
    /**
     * Kategori sil
     */
    public void deleteKategori(Long id) {
        // Input validation
        if (id == null) {
            throw new BusinessException("Kategori ID'si boş olamaz");
        }
        
        // Kategori var mı kontrol et
        Kategori kategori = kategoriRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundBusinessException("Kategori", id));
        
        // Bu kategoriye ait kitaplar varsa silme işlemini engelle
        // Kitap sayısını repository üzerinden kontrol et
        long kitapSayisi = kitapRepository.countByKategori_Id(kategori.getId());
        if (kitapSayisi > 0) {
            throw new BusinessException("Bu kategoriye ait kitaplar bulunduğu için silinemez. Önce kitapları başka kategoriye taşıyın.");
        }
        
        kategoriRepository.deleteById(id);
    }
    
    /**
     * Kategori var mı kontrol et
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return kategoriRepository.existsById(id);
    }
    
    /**
     * Kategori adı var mı kontrol et
     */
    @Transactional(readOnly = true)
    public boolean existsByAd(String ad) {
        return kategoriRepository.existsByAd(ad);
    }
    
    /**
     * Açıklaması olan kategorileri getir
     */
    @Transactional(readOnly = true)
    public List<Kategori> getKategorilerWithAciklama() {
        return kategoriRepository.findKategorilerWithAciklama();
    }
    
    /**
     * Kategori sayısını getir
     */
    @Transactional(readOnly = true)
    public long count() {
        return kategoriRepository.count();
    }
    
    /**
     * Kategori adına göre arama (büyük/küçük harf duyarsız)
     */
    @Transactional(readOnly = true)
    public List<Kategori> findByAdContainingIgnoreCase(String ad) {
        return kategoriRepository.findByAdContainingIgnoreCase(ad);
    }
    
    /**
     * Kategori kaydet
     */
    public Kategori save(Kategori kategori) {
        return kategoriRepository.save(kategori);
    }
    
    /**
     * Kategori sil
     */
    public void deleteById(Long id) {
        kategoriRepository.deleteById(id);
    }

    // Thymeleaf template'ler için ek metodlar
    
    /**
     * Tüm kategorileri getir (alias)
     */
    @Transactional(readOnly = true)
    public List<Kategori> findAll() {
        return getAllKategoriler();
    }

    /**
     * ID'ye göre kategori getir (alias)
     */
    @Transactional(readOnly = true)
    public Optional<Kategori> findById(Long id) {
        return getKategoriById(id);
    }

    /**
     * Toplam kategori sayısını getir
     */
    @Transactional(readOnly = true)
    public long getKategoriCount() {
        return kategoriRepository.count();
    }
    
    // Admin controller için ek metodlar
    
    /**
     * Sayfalama ile tüm kategorileri getir
     */
    @Transactional(readOnly = true)
    public Page<Kategori> findAll(Pageable pageable) {
        return kategoriRepository.findAll(pageable);
    }
    
    /**
     * Ad ile arama yaparak sayfalama ile kategorileri getir
     */
    @Transactional(readOnly = true)
    public Page<Kategori> findByAdContainingIgnoreCase(String ad, Pageable pageable) {
        return kategoriRepository.findByAdContainingIgnoreCase(ad, pageable);
    }
}
