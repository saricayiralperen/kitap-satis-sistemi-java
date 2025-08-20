package com.alperen.kitapsatissistemi.service;

import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.repository.KitapRepository;
import com.alperen.kitapsatissistemi.repository.KategoriRepository;
import com.alperen.kitapsatissistemi.repository.SiparisDetayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * KitapService - .NET C# projesindeki business logic'ten dönüştürülmüştür
 */
@Service
@Transactional
public class KitapService {
    
    private final KitapRepository kitapRepository;
    private final KategoriRepository kategoriRepository;
    private final SiparisDetayRepository siparisDetayRepository;
    
    @Autowired
    public KitapService(KitapRepository kitapRepository, KategoriRepository kategoriRepository, SiparisDetayRepository siparisDetayRepository) {
        this.kitapRepository = kitapRepository;
        this.kategoriRepository = kategoriRepository;
        this.siparisDetayRepository = siparisDetayRepository;
    }
    
    /**
     * Tüm kitapları getir
     */
    @Transactional(readOnly = true)
    public List<Kitap> getAllKitaplar() {
        return kitapRepository.findAll();
    }
    
    /**
     * Tüm kitapları kategori bilgileri ile birlikte getir
     */
    @Transactional(readOnly = true)
    public List<Kitap> getAllKitaplarWithKategori() {
        return kitapRepository.findAllWithKategori();
    }
    
    /**
     * ID'ye göre kitap getir
     */
    @Transactional(readOnly = true)
    public Optional<Kitap> getKitapById(Long id) {
        return kitapRepository.findById(id);
    }
    
    /**
     * ID'ye göre kitap getir (kategori bilgisi ile birlikte)
     */
    @Transactional(readOnly = true)
    public Optional<Kitap> getKitapByIdWithKategori(Long id) {
        return kitapRepository.findByIdWithKategori(id);
    }
    
    /**
     * Kitap adına göre arama
     */
    @Transactional(readOnly = true)
    public List<Kitap> searchKitaplarByAd(String ad) {
        return kitapRepository.findByAdContainingIgnoreCase(ad);
    }
    
    /**
     * Yazar adına göre arama
     */
    @Transactional(readOnly = true)
    public List<Kitap> searchKitaplarByYazar(String yazar) {
        return kitapRepository.findByYazarContainingIgnoreCase(yazar);
    }
    
    /**
     * Kitap adı ve yazar adına göre arama
     */
    @Transactional(readOnly = true)
    public List<Kitap> searchKitaplarByAdAndYazar(String ad, String yazar) {
        return kitapRepository.findByAdAndYazar(ad, yazar);
    }
    
    /**
     * Kategori ID'sine göre kitapları getir
     */
    @Transactional(readOnly = true)
    public List<Kitap> getKitaplarByKategoriId(Long kategoriId) {
        return kitapRepository.findByKategori_Id(kategoriId);
    }
    
    /**
     * Fiyat aralığına göre kitapları getir
     */
    @Transactional(readOnly = true)
    public List<Kitap> getKitaplarByFiyatAraligi(BigDecimal minFiyat, BigDecimal maxFiyat) {
        return kitapRepository.findByFiyatBetween(minFiyat, maxFiyat);
    }
    
    /**
     * Yeni kitap oluştur
     */
    public Kitap createKitap(Kitap kitap) {
        // Input validation
        if (kitap == null) {
            throw new BusinessException("Kitap bilgileri boş olamaz");
        }
        if (!StringUtils.hasText(kitap.getAd())) {
            throw new BusinessException("Kitap adı boş olamaz");
        }
        if (!StringUtils.hasText(kitap.getYazar())) {
            throw new BusinessException("Kitap yazarı boş olamaz");
        }
        if (kitap.getFiyat() == null || kitap.getFiyat().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Kitap fiyatı pozitif olmalıdır");
        }
        if (kitap.getKategoriId() == null) {
            throw new BusinessException("Kategori ID'si boş olamaz");
        }
        
        // Kategori var mı kontrol et
        if (!kategoriRepository.existsById(kitap.getKategoriId())) {
            throw new EntityNotFoundBusinessException("Kategori", kitap.getKategoriId());
        }
        
        // Kitap adını ve yazarını trim et
        kitap.setAd(kitap.getAd().trim());
        kitap.setYazar(kitap.getYazar().trim());
        if (StringUtils.hasText(kitap.getAciklama())) {
            kitap.setAciklama(kitap.getAciklama().trim());
        }
        
        return kitapRepository.save(kitap);
    }
    
    /**
     * Kitap güncelle
     */
    public Kitap updateKitap(Long id, Kitap kitapDetaylari) {
        // Input validation
        if (id == null) {
            throw new BusinessException("Kitap ID'si boş olamaz");
        }
        if (kitapDetaylari == null) {
            throw new BusinessException("Kitap detayları boş olamaz");
        }
        if (!StringUtils.hasText(kitapDetaylari.getAd())) {
            throw new BusinessException("Kitap adı boş olamaz");
        }
        if (!StringUtils.hasText(kitapDetaylari.getYazar())) {
            throw new BusinessException("Kitap yazarı boş olamaz");
        }
        if (kitapDetaylari.getFiyat() == null || kitapDetaylari.getFiyat().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Kitap fiyatı pozitif olmalıdır");
        }
        if (kitapDetaylari.getKategoriId() == null) {
            throw new BusinessException("Kategori ID'si boş olamaz");
        }
        
        // Kategori var mı kontrol et
        if (!kategoriRepository.existsById(kitapDetaylari.getKategoriId())) {
            throw new EntityNotFoundBusinessException("Kategori", kitapDetaylari.getKategoriId());
        }
        
        return kitapRepository.findById(id)
                .map(kitap -> {
                    kitap.setAd(kitapDetaylari.getAd().trim());
                    kitap.setYazar(kitapDetaylari.getYazar().trim());
                    kitap.setFiyat(kitapDetaylari.getFiyat());
                    if (StringUtils.hasText(kitapDetaylari.getAciklama())) {
                        kitap.setAciklama(kitapDetaylari.getAciklama().trim());
                    }
                    if (StringUtils.hasText(kitapDetaylari.getResimUrl())) {
                        kitap.setResimUrl(kitapDetaylari.getResimUrl().trim());
                    }
                    
                    // Kategori güncelleme
                    if (kitapDetaylari.getKategori() != null) {
                        kitap.setKategori(kitapDetaylari.getKategori());
                    }
                    
                    return kitapRepository.save(kitap);
                })
                .orElseThrow(() -> new EntityNotFoundBusinessException("Kitap", id));
    }
    
    /**
     * Kitap sil
     */
    public void deleteKitap(Long id) {
        // Input validation
        if (id == null) {
            throw new BusinessException("Kitap ID'si boş olamaz");
        }

        // Kitap var mı kontrol et
        if (!kitapRepository.existsById(id)) {
            throw new EntityNotFoundBusinessException("Kitap", id);
        }

        // Bu kitaba ait siparişler varsa silme işlemini engelle
        if (siparisDetayRepository.countByKitap_Id(id) > 0) {
            throw new BusinessException("Bu kitaba ait siparişler bulunduğu için silinemez. Kitap sadece stoktan kaldırılabilir.");
        }

        kitapRepository.deleteById(id);
    }
    
    /**
     * Kitap var mı kontrol et
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return kitapRepository.existsById(id);
    }
    
    /**
     * En pahalı kitapları getir
     */
    @Transactional(readOnly = true)
    public List<Kitap> getEnPahaliKitaplar() {
        return kitapRepository.findTopByOrderByFiyatDesc();
    }
    
    /**
     * En ucuz kitapları getir
     */
    @Transactional(readOnly = true)
    public List<Kitap> getEnUcuzKitaplar() {
        return kitapRepository.findTopByOrderByFiyatAsc();
    }
    
    /**
     * Belirli fiyatın üzerindeki kitapları getir
     */
    @Transactional(readOnly = true)
    public List<Kitap> getKitaplarByFiyatGreaterThan(BigDecimal fiyat) {
        return kitapRepository.findByFiyatGreaterThan(fiyat);
    }
    
    /**
     * Belirli fiyatın altındaki kitapları getir
     */
    @Transactional(readOnly = true)
    public List<Kitap> getKitaplarByFiyatLessThan(BigDecimal fiyat) {
        return kitapRepository.findByFiyatLessThan(fiyat);
    }
    
    /**
     * Resim URL'si olan kitapları getir
     */
    @Transactional(readOnly = true)
    public List<Kitap> getKitaplarWithResim() {
        return kitapRepository.findKitaplarWithResim();
    }
    
    /**
     * Kategori ID'sine göre kitap sayısını getir
     */
    @Transactional(readOnly = true)
    public long getKitapCountByKategoriId(Long kategoriId) {
        return kitapRepository.countByKategori_Id(kategoriId);
    }
    
    /**
     * Kitap sayısını getir
     */
    @Transactional(readOnly = true)
    public long getKitapCount() {
        return kitapRepository.count();
    }
    
    /**
     * Son eklenen kitapları getir
     */
    @Transactional(readOnly = true)
    public List<Kitap> getLatestKitaplar(int limit) {
        return kitapRepository.findTopByOrderByIdDesc(limit);
    }
    
    /**
     * Kategoriye göre kitap sayısını getir
     */
    @Transactional(readOnly = true)
    public long countByKategoriId(Long kategoriId) {
        return kitapRepository.countByKategori_Id(kategoriId);
    }

    // Thymeleaf template'ler için ek metodlar
    
    /**
     * Tüm kitapları getir (alias)
     */
    @Transactional(readOnly = true)
    public List<Kitap> findAll() {
        return getAllKitaplar();
    }

    /**
     * Sayfalama ile tüm kitapları getir
     */
    @Transactional(readOnly = true)
    public Page<Kitap> findAll(Pageable pageable) {
        return kitapRepository.findAll(pageable);
    }

    /**
     * ID'ye göre kitap getir (alias)
     */
    @Transactional(readOnly = true)
    public Optional<Kitap> findById(Long id) {
        return getKitapById(id);
    }

    /**
     * Başlığa göre arama (case insensitive)
     */
    @Transactional(readOnly = true)
    public Page<Kitap> findByAdContainingIgnoreCase(String ad, Pageable pageable) {
        return kitapRepository.findByAdContainingIgnoreCase(ad, pageable);
    }

    /**
     * Kategori ID'ye göre kitapları getir (sayfalama ile)
     */
    @Transactional(readOnly = true)
    public Page<Kitap> findByKategoriId(Long kategoriId, Pageable pageable) {
        return kitapRepository.findByKategori_Id(kategoriId, pageable);
    }

    /**
     * Başlık ve kategori ID'ye göre arama
     */
    @Transactional(readOnly = true)
    public Page<Kitap> findByAdContainingIgnoreCaseAndKategoriId(String ad, Long kategoriId, Pageable pageable) {
        return kitapRepository.findByAdContainingIgnoreCaseAndKategori_Id(ad, kategoriId, pageable);
    }

    /**
     * Kategori ID'ye göre kitapları getir (belirli ID hariç)
     */
    @Transactional(readOnly = true)
    public List<Kitap> findByKategoriIdAndIdNot(Long kategoriId, Long excludeId) {
        return kitapRepository.findByKategori_IdAndIdNot(kategoriId, excludeId);
    }

    /**
     * Kitap kaydet
     */
    public Kitap save(Kitap kitap) {
        return createKitap(kitap);
    }

    /**
     * ID'ye göre kitap sil
     */
    public void deleteById(Long id) {
        deleteKitap(id);
    }

    /**
     * Toplam kitap sayısı
     */
    @Transactional(readOnly = true)
    public long count() {
        return kitapRepository.count();
    }
}
