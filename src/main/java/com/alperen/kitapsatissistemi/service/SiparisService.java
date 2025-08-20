package com.alperen.kitapsatissistemi.service;

import com.alperen.kitapsatissistemi.entity.Siparis;
import com.alperen.kitapsatissistemi.entity.SiparisDetay;
import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.repository.SiparisRepository;
import com.alperen.kitapsatissistemi.repository.KullaniciRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * SiparisService - .NET C# projesindeki business logic'ten dönüştürülmüştür
 */
@Service
@Transactional
public class SiparisService {
    
    private final SiparisRepository siparisRepository;
    private final KullaniciRepository kullaniciRepository;
    
    @Autowired
    public SiparisService(SiparisRepository siparisRepository, 
                         KullaniciRepository kullaniciRepository) {
        this.siparisRepository = siparisRepository;
        this.kullaniciRepository = kullaniciRepository;
    }
    
    /**
     * Tüm siparişleri getir
     */
    @Transactional(readOnly = true)
    public List<Siparis> getAllSiparisler() {
        return siparisRepository.findAll();
    }
    
    /**
     * Tüm siparişleri detayları ile birlikte getir
     */
    @Transactional(readOnly = true)
    public List<Siparis> getAllSiparislerWithDetails() {
        return siparisRepository.findAllWithDetails();
    }
    
    /**
     * ID'ye göre sipariş getir
     */
    @Transactional(readOnly = true)
    public Optional<Siparis> getSiparisById(Long id) {
        return siparisRepository.findById(id);
    }
    
    /**
     * ID'ye göre sipariş getir (detayları ile birlikte)
     */
    @Transactional(readOnly = true)
    public Optional<Siparis> getSiparisByIdWithDetails(Long id) {
        return siparisRepository.findByIdWithDetails(id);
    }
    
    /**
     * Kullanıcı ID'sine göre siparişleri getir
     */
    @Transactional(readOnly = true)
    public List<Siparis> getSiparislerByKullaniciId(Long kullaniciId) {
        return siparisRepository.findByKullanici_Id(kullaniciId);
    }
    
    /**
     * Kullanıcı ID'sine göre siparişleri detayları ile birlikte getir
     */
    @Transactional(readOnly = true)
    public List<Siparis> getSiparislerByKullaniciIdWithDetails(Long kullaniciId) {
        return siparisRepository.findByKullanici_IdWithDetails(kullaniciId);
    }
    
    /**
     * Duruma göre siparişleri getir
     */
    @Transactional(readOnly = true)
    public List<Siparis> getSiparislerByDurum(String durum) {
        return siparisRepository.findByDurum(durum);
    }
    
    /**
     * Tarih aralığına göre siparişleri getir
     */
    @Transactional(readOnly = true)
    public List<Siparis> getSiparislerByTarihAraligi(LocalDateTime baslangic, LocalDateTime bitis) {
        return siparisRepository.findBySiparisTarihiBetween(baslangic, bitis);
    }
    
    /**
     * Tutar aralığına göre siparişleri getir
     */
    @Transactional(readOnly = true)
    public List<Siparis> getSiparislerByTutarAraligi(BigDecimal minTutar, BigDecimal maxTutar) {
        return siparisRepository.findByToplamTutarBetween(minTutar, maxTutar);
    }
    
    /**
     * Sipariş oluştur
     */
    public Siparis createSiparis(Long kullaniciId, List<SiparisDetay> siparisDetaylari) {
        // Input validation
        if (kullaniciId == null) {
            throw new BusinessException("Kullanıcı ID'si boş olamaz");
        }
        if (siparisDetaylari == null || siparisDetaylari.isEmpty()) {
            throw new BusinessException("Sipariş detayları boş olamaz");
        }
        
        // Kullanıcı var mı kontrol et ve kullanıcı nesnesini al
        Kullanici kullanici = kullaniciRepository.findById(kullaniciId)
            .orElseThrow(() -> new EntityNotFoundBusinessException("Kullanıcı", kullaniciId));
        
        // Sipariş detaylarını validate et
        for (SiparisDetay detay : siparisDetaylari) {
            if (detay == null) {
                throw new BusinessException("Sipariş detayı null olamaz");
            }
            if (detay.getAdet() == null || detay.getAdet() <= 0) {
                throw new BusinessException("Sipariş detayı adedi pozitif olmalıdır");
            }
            if (detay.getFiyat() == null || detay.getFiyat().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("Sipariş detayı fiyatı pozitif olmalıdır");
            }
        }
        
        // Toplam tutarı hesapla
        BigDecimal toplamTutar = siparisDetaylari.stream()
                .map(detay -> detay.getFiyat().multiply(BigDecimal.valueOf(detay.getAdet())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Sipariş oluştur
        Siparis siparis = new Siparis(kullanici, toplamTutar);
        
        // Sipariş detaylarını ayarla
        for (SiparisDetay detay : siparisDetaylari) {
            detay.setSiparis(siparis);
        }
        siparis.setSiparisDetaylari(siparisDetaylari);
        
        return siparisRepository.save(siparis);
    }
    
    /**
     * Sipariş durumunu güncelle
     */
    public Siparis updateSiparisDurum(Long id, String yeniDurum) {
        // Input validation
        if (id == null) {
            throw new BusinessException("Sipariş ID'si boş olamaz");
        }
        if (!StringUtils.hasText(yeniDurum)) {
            throw new BusinessException("Yeni durum boş olamaz");
        }
        
        return siparisRepository.findById(id)
                .map(siparis -> {
                    siparis.setDurum(yeniDurum.trim());
                    return siparisRepository.save(siparis);
                })
                .orElseThrow(() -> new EntityNotFoundBusinessException("Sipariş", id));
    }
    
    /**
     * Sipariş güncelle
     */
    public Siparis updateSiparis(Long id, Siparis siparisDetaylari) {
        // Input validation
        if (id == null) {
            throw new BusinessException("Sipariş ID'si boş olamaz");
        }
        if (siparisDetaylari == null) {
            throw new BusinessException("Sipariş detayları boş olamaz");
        }
        
        return siparisRepository.findById(id)
                .map(siparis -> {
                    if (siparisDetaylari.getToplamTutar() != null) {
                        siparis.setToplamTutar(siparisDetaylari.getToplamTutar());
                    }
                    if (StringUtils.hasText(siparisDetaylari.getDurum())) {
                        siparis.setDurum(siparisDetaylari.getDurum().trim());
                    }
                    return siparisRepository.save(siparis);
                })
                .orElseThrow(() -> new EntityNotFoundBusinessException("Sipariş", id));
    }
    
    /**
     * Sipariş sil
     */
    public void deleteSiparis(Long id) {
        // Input validation
        if (id == null) {
            throw new BusinessException("Sipariş ID'si boş olamaz");
        }
        
        // Sipariş var mı kontrol et
        if (!siparisRepository.existsById(id)) {
            throw new EntityNotFoundBusinessException("Sipariş", id);
        }
        
        siparisRepository.deleteById(id);
    }
    
    /**
     * Sipariş var mı kontrol et
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return siparisRepository.existsById(id);
    }
    
    /**
     * Kullanıcının sipariş sayısını getir
     */
    @Transactional(readOnly = true)
    public long getSiparisCountByKullaniciId(Long kullaniciId) {
        return siparisRepository.countByKullanici_Id(kullaniciId);
    }
    
    /**
     * Duruma göre sipariş sayısını getir
     */
    @Transactional(readOnly = true)
    public long getSiparisCountByDurum(String durum) {
        return siparisRepository.countByDurum(durum);
    }
    
    /**
     * Kullanıcının toplam harcamasını getir
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalSpentByKullaniciId(Long kullaniciId) {
        BigDecimal total = siparisRepository.findToplamHarcamaByKullanici_Id(kullaniciId);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Günlük satış istatistiklerini getir
     */
    @Transactional(readOnly = true)
    public List<Object[]> getDailySalesStats() {
        return siparisRepository.findGunlukSatisIstatistikleri();
    }
    
    /**
     * Aylık satış istatistiklerini getir
     */
    @Transactional(readOnly = true)
    public List<Object[]> getMonthlySalesStats() {
        return siparisRepository.getMonthlySalesStats();
    }
    
    /**
     * En çok sipariş veren kullanıcıları getir
     */
    @Transactional(readOnly = true)
    public List<Object[]> getTopCustomers() {
        return siparisRepository.getTopCustomers();
    }
    
    /**
     * Belirli tarihten sonraki siparişleri getir
     */
    @Transactional(readOnly = true)
    public List<Siparis> getSiparislerAfterDate(LocalDateTime tarih) {
        return siparisRepository.findBySiparisTarihiAfter(tarih);
    }
    
    /**
     * Belirli tarihten önceki siparişleri getir
     */
    @Transactional(readOnly = true)
    public List<Siparis> getSiparislerBeforeDate(LocalDateTime tarih) {
        return siparisRepository.findBySiparisTarihiBefore(tarih);
    }
    
    /**
     * Toplam sipariş sayısını getir
     */
    @Transactional(readOnly = true)
    public long getSiparisCount() {
        return siparisRepository.count();
    }
    
    /**
     * Son eklenen siparişleri getir
     */
    @Transactional(readOnly = true)
    public List<Siparis> getLatestSiparisler(int limit) {
        return siparisRepository.findTopByOrderByIdDesc(limit);
    }
    
    // Admin controller için ek metodlar
    
    /**
     * Sayfalama ile tüm siparişleri getir
     */
    @Transactional(readOnly = true)
    public Page<Siparis> findAll(Pageable pageable) {
        return siparisRepository.findAll(pageable);
    }
    
    /**
     * Duruma göre sayfalama ile siparişleri getir
     */
    @Transactional(readOnly = true)
    public Page<Siparis> findByDurum(String durum, Pageable pageable) {
        return siparisRepository.findByDurum(durum, pageable);
    }
    
    /**
     * ID ile sipariş bul
     */
    @Transactional(readOnly = true)
    public Optional<Siparis> findById(Long id) {
        return siparisRepository.findById(id);
    }
    
    /**
     * ID ile sipariş sil
     */
    public void deleteById(Long id) {
        siparisRepository.deleteById(id);
    }
    
    /**
     * Sipariş durumunu güncelle
     */
    public Siparis updateDurum(Long id, String durum) {
        Optional<Siparis> siparisOpt = siparisRepository.findById(id);
        if (siparisOpt.isPresent()) {
            Siparis siparis = siparisOpt.get();
            siparis.setDurum(durum);
            return siparisRepository.save(siparis);
        }
        throw new RuntimeException("Sipariş bulunamadı: " + id);
    }
    
    /**
     * Sipariş kaydet
     */
    public Siparis save(Siparis siparis) {
        return siparisRepository.save(siparis);
    }
    
    /**
     * Toplam sipariş sayısını getir
     */
    @Transactional(readOnly = true)
    public long count() {
        return getSiparisCount();
    }
}
