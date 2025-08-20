package com.alperen.kitapsatissistemi.service;

import com.alperen.kitapsatissistemi.entity.Favori;
import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.repository.FavoriRepository;
import com.alperen.kitapsatissistemi.repository.KullaniciRepository;
import com.alperen.kitapsatissistemi.repository.KitapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * FavoriService - .NET C# projesindeki business logic'ten dönüştürülmüştür
 */
@Service
@Transactional
public class FavoriService {
    
    private final FavoriRepository favoriRepository;
    private final KullaniciRepository kullaniciRepository;
    private final KitapRepository kitapRepository;
    
    @Autowired
    public FavoriService(FavoriRepository favoriRepository, 
                        KullaniciRepository kullaniciRepository,
                        KitapRepository kitapRepository) {
        this.favoriRepository = favoriRepository;
        this.kullaniciRepository = kullaniciRepository;
        this.kitapRepository = kitapRepository;
    }
    
    /**
     * Tüm favorileri getir
     */
    @Transactional(readOnly = true)
    public List<Favori> getAllFavoriler() {
        return favoriRepository.findAll();
    }
    
    /**
     * Tüm favorileri kullanıcı ve kitap bilgileri ile birlikte getir
     */
    @Transactional(readOnly = true)
    public List<Favori> getAllFavorilerWithDetails() {
        return favoriRepository.findAllWithKullaniciAndKitap();
    }
    
    /**
     * ID'ye göre favori getir
     */
    @Transactional(readOnly = true)
    public Optional<Favori> getFavoriById(Long id) {
        return favoriRepository.findById(id);
    }
    
    /**
     * Kullanıcı ID'sine göre favorileri getir
     */
    @Transactional(readOnly = true)
    public List<Favori> getFavorilerByKullaniciId(Long kullaniciId) {
        return favoriRepository.findByKullanici_Id(kullaniciId);
    }
    
    /**
     * Kullanıcı ID'sine göre favorileri detayları ile birlikte getir
     */
    @Transactional(readOnly = true)
    public List<Favori> getFavorilerByKullaniciIdWithDetails(Long kullaniciId) {
        return favoriRepository.findByKullanici_IdWithKitap(kullaniciId);
    }
    
    /**
     * Kitap ID'sine göre favorileri getir
     */
    @Transactional(readOnly = true)
    public List<Favori> getFavorilerByKitapId(Long kitapId) {
        return favoriRepository.findByKitap_Id(kitapId);
    }
    
    /**
     * Kitap ID'sine göre favorileri detayları ile birlikte getir
     */
    @Transactional(readOnly = true)
    public List<Favori> getFavorilerByKitapIdWithDetails(Long kitapId) {
        return favoriRepository.findByKitap_IdWithKullanici(kitapId);
    }
    
    /**
     * Yeni favori ekle
     */
    public Favori addFavori(Long kullaniciId, Long kitapId) {
        // Kullanıcı var mı kontrol et
        Kullanici kullanici = kullaniciRepository.findById(kullaniciId)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı, ID: " + kullaniciId));
        
        // Kitap var mı kontrol et
        Kitap kitap = kitapRepository.findById(kitapId)
            .orElseThrow(() -> new RuntimeException("Kitap bulunamadı, ID: " + kitapId));
        
        // Zaten favori olarak eklenmiş mi kontrol et
        if (favoriRepository.existsByKullanici_IdAndKitap_Id(kullaniciId, kitapId)) {
            throw new RuntimeException("Bu kitap zaten favorilerinizde mevcut");
        }
        
        Favori favori = new Favori(kullanici, kitap);
        
        return favoriRepository.save(favori);
    }
    
    /**
     * Favori sil
     */
    public void deleteFavori(Long id) {
        if (!favoriRepository.existsById(id)) {
            throw new RuntimeException("Favori bulunamadı, ID: " + id);
        }
        
        favoriRepository.deleteById(id);
    }
    
    /**
     * Kullanıcı ve kitap ID'sine göre favori sil
     */
    public void deleteFavoriByKullaniciAndKitap(Long kullaniciId, Long kitapId) {
        Optional<Favori> favoriOpt = favoriRepository.findByKullanici_IdAndKitap_Id(kullaniciId, kitapId);
        
        if (favoriOpt.isPresent()) {
            favoriRepository.delete(favoriOpt.get());
        } else {
            throw new RuntimeException("Favori bulunamadı");
        }
    }
    
    /**
     * Kullanıcının tüm favorilerini sil
     */
    public void deleteAllFavorilerByKullaniciId(Long kullaniciId) {
        List<Favori> favoriler = favoriRepository.findByKullanici_Id(kullaniciId);
        favoriRepository.deleteAll(favoriler);
    }
    
    /**
     * Kitabın tüm favorilerini sil
     */
    public void deleteAllFavorilerByKitapId(Long kitapId) {
        List<Favori> favoriler = favoriRepository.findByKitap_Id(kitapId);
        favoriRepository.deleteAll(favoriler);
    }
    
    /**
     * Favori var mı kontrol et
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return favoriRepository.existsById(id);
    }
    
    /**
     * Kullanıcı ve kitap için favori var mı kontrol et
     */
    @Transactional(readOnly = true)
    public boolean existsByKullaniciIdAndKitapId(Long kullaniciId, Long kitapId) {
        return favoriRepository.existsByKullanici_IdAndKitap_Id(kullaniciId, kitapId);
    }
    
    /**
     * Kullanıcının favori sayısını getir
     */
    @Transactional(readOnly = true)
    public long getFavoriCountByKullaniciId(Long kullaniciId) {
        return favoriRepository.countByKullanici_Id(kullaniciId);
    }
    
    /**
     * Kitabın favori sayısını getir
     */
    @Transactional(readOnly = true)
    public long getFavoriCountByKitapId(Long kitapId) {
        return favoriRepository.countByKitap_Id(kitapId);
    }
    
    /**
     * En çok favorilenen kitapları getir
     */
    @Transactional(readOnly = true)
    public List<Object[]> getEnCokFavorilenenKitaplar() {
        return favoriRepository.findMostFavoritedBooks();
    }
    
    /**
     * En aktif kullanıcıları getir (en çok favori ekleyen)
     */
    @Transactional(readOnly = true)
    public List<Object[]> getEnAktifKullanicilar() {
        return favoriRepository.findMostActiveUsers();
    }
    
    /**
     * Favori sayısını getir
     */
    @Transactional(readOnly = true)
    public long getFavoriCount() {
        return favoriRepository.count();
    }
    
    // Admin controller için ek metodlar
    
    /**
     * Sayfalama ile tüm favorileri getir
     */
    @Transactional(readOnly = true)
    public Page<Favori> findAll(Pageable pageable) {
        return favoriRepository.findAll(pageable);
    }
    
    /**
     * ID ile favori sil
     */
    public void deleteById(Long id) {
        favoriRepository.deleteById(id);
    }
    
    /**
     * Kullanıcı ID'sine göre tüm favorileri sil
     */
    public void deleteByKullaniciId(Long kullaniciId) {
        favoriRepository.deleteByKullanici_Id(kullaniciId);
    }
    
    /**
     * Kitap ID'sine göre tüm favorileri sil
     */
    public void deleteByKitapId(Long kitapId) {
        favoriRepository.deleteByKitap_Id(kitapId);
    }
    
    /**
     * ID ile favori bul
     */
    @Transactional(readOnly = true)
    public Optional<Favori> findById(Long id) {
        return favoriRepository.findById(id);
    }
}
