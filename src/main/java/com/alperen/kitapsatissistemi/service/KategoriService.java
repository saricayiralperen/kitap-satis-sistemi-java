package com.alperen.kitapsatissistemi.service;

import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.repository.KategoriRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * KategoriService - .NET C# projesindeki business logic'ten dönüştürülmüştür
 */
@Service
@Transactional
public class KategoriService {
    
    private final KategoriRepository kategoriRepository;
    
    @Autowired
    public KategoriService(KategoriRepository kategoriRepository) {
        this.kategoriRepository = kategoriRepository;
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
        // Kategori adının benzersiz olup olmadığını kontrol et
        if (kategoriRepository.existsByAd(kategori.getAd())) {
            throw new RuntimeException("Bu kategori adı zaten mevcut: " + kategori.getAd());
        }
        
        return kategoriRepository.save(kategori);
    }
    
    /**
     * Kategori güncelle
     */
    public Kategori updateKategori(Long id, Kategori kategoriDetaylari) {
        return kategoriRepository.findById(id)
                .map(kategori -> {
                    // Kategori adının benzersiz olup olmadığını kontrol et (kendi ID'si hariç)
                    if (kategoriRepository.existsByAdAndIdNot(kategoriDetaylari.getAd(), id)) {
                        throw new RuntimeException("Bu kategori adı zaten mevcut: " + kategoriDetaylari.getAd());
                    }
                    
                    kategori.setAd(kategoriDetaylari.getAd());
                    kategori.setAciklama(kategoriDetaylari.getAciklama());
                    return kategoriRepository.save(kategori);
                })
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı, ID: " + id));
    }
    
    /**
     * Kategori sil
     */
    public void deleteKategori(Long id) {
        if (!kategoriRepository.existsById(id)) {
            throw new RuntimeException("Kategori bulunamadı, ID: " + id);
        }
        
        // TODO: Bu kategoriye ait kitaplar varsa silme işlemini engelle
        // KitapService ile kontrol edilebilir
        
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