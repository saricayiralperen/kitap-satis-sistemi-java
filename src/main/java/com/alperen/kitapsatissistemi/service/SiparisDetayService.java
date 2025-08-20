package com.alperen.kitapsatissistemi.service;

import com.alperen.kitapsatissistemi.entity.SiparisDetay;
import com.alperen.kitapsatissistemi.entity.Siparis;
import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.repository.SiparisDetayRepository;
import com.alperen.kitapsatissistemi.repository.SiparisRepository;
import com.alperen.kitapsatissistemi.repository.KitapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * SiparisDetayService - .NET C# projesindeki business logic'ten dönüştürülmüştür
 */
@Service
@Transactional
public class SiparisDetayService {
    
    private final SiparisDetayRepository siparisDetayRepository;
    private final SiparisRepository siparisRepository;
    private final KitapRepository kitapRepository;
    
    @Autowired
    public SiparisDetayService(SiparisDetayRepository siparisDetayRepository,
                              SiparisRepository siparisRepository,
                              KitapRepository kitapRepository) {
        this.siparisDetayRepository = siparisDetayRepository;
        this.siparisRepository = siparisRepository;
        this.kitapRepository = kitapRepository;
    }
    
    /**
     * Tüm sipariş detaylarını getir
     */
    @Transactional(readOnly = true)
    public List<SiparisDetay> getAllSiparisDetaylar() {
        return siparisDetayRepository.findAll();
    }
    
    /**
     * Tüm sipariş detaylarını kitap ve sipariş bilgileri ile birlikte getir
     */
    @Transactional(readOnly = true)
    public List<SiparisDetay> getAllSiparisDetaylarWithDetails() {
        return siparisDetayRepository.findAllWithKitapAndSiparis();
    }
    
    /**
     * ID'ye göre sipariş detayı getir
     */
    @Transactional(readOnly = true)
    public Optional<SiparisDetay> getSiparisDetayById(Long id) {
        return siparisDetayRepository.findById(id);
    }
    
    /**
     * Sipariş ID'sine göre sipariş detaylarını getir
     */
    @Transactional(readOnly = true)
    public List<SiparisDetay> getSiparisDetaylarBySiparisId(Long siparisId) {
        return siparisDetayRepository.findBySiparis_Id(siparisId);
    }
    
    /**
     * Sipariş ID'sine göre sipariş detaylarını kitap bilgileri ile birlikte getir
     */
    @Transactional(readOnly = true)
    public List<SiparisDetay> getSiparisDetaylarBySiparisIdWithKitap(Long siparisId) {
        return siparisDetayRepository.findBySiparis_IdWithKitap(siparisId);
    }
    
    /**
     * Kitap ID'sine göre sipariş detaylarını getir
     */
    @Transactional(readOnly = true)
    public List<SiparisDetay> getSiparisDetaylarByKitapId(Long kitapId) {
        return siparisDetayRepository.findByKitap_Id(kitapId);
    }
    
    /**
     * Kitap ID'sine göre sipariş detaylarını sipariş bilgileri ile birlikte getir
     */
    @Transactional(readOnly = true)
    public List<SiparisDetay> getSiparisDetaylarByKitapIdWithSiparis(Long kitapId) {
        return siparisDetayRepository.findByKitap_IdWithSiparis(kitapId);
    }
    
    /**
     * Sipariş ve kitap ID'sine göre sipariş detayı getir
     */
    @Transactional(readOnly = true)
    public Optional<SiparisDetay> getSiparisDetayBySiparisIdAndKitapId(Long siparisId, Long kitapId) {
        return siparisDetayRepository.findBySiparis_IdAndKitap_Id(siparisId, kitapId);
    }
    
    /**
     * Yeni sipariş detayı oluştur
     */
    public SiparisDetay createSiparisDetay(SiparisDetay siparisDetay) {
        // Sipariş var mı kontrol et
        Siparis siparis = siparisRepository.findById(siparisDetay.getSiparisId())
            .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı, ID: " + siparisDetay.getSiparisId()));
        
        // Kitap var mı kontrol et
        Kitap kitap = kitapRepository.findById(siparisDetay.getKitapId())
            .orElseThrow(() -> new RuntimeException("Kitap bulunamadı, ID: " + siparisDetay.getKitapId()));
        
        // Adet pozitif mi kontrol et
        if (siparisDetay.getAdet() <= 0) {
            throw new RuntimeException("Adet 0'dan büyük olmalıdır");
        }
        
        // Fiyat pozitif mi kontrol et
        if (siparisDetay.getFiyat().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Fiyat 0'dan büyük olmalıdır");
        }
        
        // Navigation property'leri ayarla
        siparisDetay.setSiparis(siparis);
        siparisDetay.setKitap(kitap);
        
        return siparisDetayRepository.save(siparisDetay);
    }
    
    /**
     * Sipariş detayı güncelle
     */
    public SiparisDetay updateSiparisDetay(Long id, SiparisDetay siparisDetayDetaylari) {
        return siparisDetayRepository.findById(id)
                .map(siparisDetay -> {
                    // Kitap var mı kontrol et ve kitap nesnesini al
                    Kitap kitap = kitapRepository.findById(siparisDetayDetaylari.getKitapId())
                        .orElseThrow(() -> new RuntimeException("Kitap bulunamadı, ID: " + siparisDetayDetaylari.getKitapId()));
                    
                    // Adet pozitif mi kontrol et
                    if (siparisDetayDetaylari.getAdet() <= 0) {
                        throw new RuntimeException("Adet 0'dan büyük olmalıdır");
                    }
                    
                    // Fiyat pozitif mi kontrol et
                    if (siparisDetayDetaylari.getFiyat().compareTo(BigDecimal.ZERO) <= 0) {
                        throw new RuntimeException("Fiyat 0'dan büyük olmalıdır");
                    }
                    
                    siparisDetay.setKitap(kitap);
                    siparisDetay.setAdet(siparisDetayDetaylari.getAdet());
                    siparisDetay.setFiyat(siparisDetayDetaylari.getFiyat());
                    
                    return siparisDetayRepository.save(siparisDetay);
                })
                .orElseThrow(() -> new RuntimeException("Sipariş detayı bulunamadı, ID: " + id));
    }
    
    /**
     * Sipariş detayı sil
     */
    public void deleteSiparisDetay(Long id) {
        if (!siparisDetayRepository.existsById(id)) {
            throw new RuntimeException("Sipariş detayı bulunamadı, ID: " + id);
        }
        
        siparisDetayRepository.deleteById(id);
    }
    
    /**
     * Siparişin tüm detaylarını sil
     */
    public void deleteAllSiparisDetaylarBySiparisId(Long siparisId) {
        List<SiparisDetay> detaylar = siparisDetayRepository.findBySiparis_Id(siparisId);
        siparisDetayRepository.deleteAll(detaylar);
    }
    
    /**
     * Sipariş detayı var mı kontrol et
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return siparisDetayRepository.existsById(id);
    }
    
    /**
     * Kitabın toplam satış adedini getir
     */
    @Transactional(readOnly = true)
    public Integer getTotalSalesQuantityByKitapId(Long kitapId) {
        Integer total = siparisDetayRepository.getTotalSalesQuantityByKitap_Id(kitapId);
        return total != null ? total : 0;
    }
    
    /**
     * Kitabın toplam satış tutarını getir
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalSalesAmountByKitapId(Long kitapId) {
        return siparisDetayRepository.findToplamSatisTutariByKitap_Id(kitapId);
    }
    
    /**
     * En çok satan kitapları getir
     */
    @Transactional(readOnly = true)
    public List<Object[]> getBestSellingBooks() {
        return siparisDetayRepository.findEnCokSatilanKitaplar();
    }
    
    /**
     * En çok gelir getiren kitapları getir
     */
    @Transactional(readOnly = true)
    public List<Object[]> getHighestEarningBooks() {
        return siparisDetayRepository.findEnCokGelirGetirenKitaplar();
    }
    
    /**
     * Siparişin toplam tutarını hesapla
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalOrderAmount(Long siparisId) {
        return siparisDetayRepository.calculateToplamTutarBySiparis_Id(siparisId);
    }
    
    /**
     * Kategoriye göre satış istatistiklerini getir
     */
    @Transactional(readOnly = true)
    public List<Object[]> getSalesStatsByCategory() {
        return siparisDetayRepository.getSalesStatsByCategory();
    }
    
    /**
     * Yazara göre satış istatistiklerini getir
     */
    @Transactional(readOnly = true)
    public List<Object[]> getSalesStatsByAuthor() {
        return siparisDetayRepository.getSalesStatsByAuthor();
    }
    
    /**
     * Sipariş ID'sine göre sipariş detayı sayısını getir
     */
    @Transactional(readOnly = true)
    public long getSiparisDetayCountBySiparisId(Long siparisId) {
        return siparisDetayRepository.countBySiparis_Id(siparisId);
    }
    
    /**
     * Kitap ID'sine göre sipariş detayı sayısını getir
     */
    @Transactional(readOnly = true)
    public long getSiparisDetayCountByKitapId(Long kitapId) {
        return siparisDetayRepository.countByKitap_Id(kitapId);
    }
    
    /**
     * Toplam sipariş detayı sayısını getir
     */
    @Transactional(readOnly = true)
    public long getSiparisDetayCount() {
        return siparisDetayRepository.count();
    }
}
