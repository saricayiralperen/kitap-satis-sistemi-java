package com.alperen.kitapsatissistemi.config;

import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.repository.KategoriRepository;
import com.alperen.kitapsatissistemi.repository.KitapRepository;
import com.alperen.kitapsatissistemi.repository.KullaniciRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * DataLoader - Uygulama başlatıldığında örnek verileri yükler
 * .NET C# projesindeki DataSeeder'dan dönüştürülmüştür
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final KategoriRepository kategoriRepository;
    private final KitapRepository kitapRepository;
    private final KullaniciRepository kullaniciRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(KategoriRepository kategoriRepository, KitapRepository kitapRepository, 
                     KullaniciRepository kullaniciRepository) {
        this.kategoriRepository = kategoriRepository;
        this.kitapRepository = kitapRepository;
        this.kullaniciRepository = kullaniciRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("DataLoader run metodu çağrıldı");
        loadData();
    }

    private void loadData() {
        // Admin kullanıcısını oluştur (her zaman kontrol et)
        createAdminUser();
        
        // Eğer veri zaten varsa yükleme
        if (kategoriRepository.count() > 0) {
            return;
        }

        // Kategorileri yükle
        List<Kategori> kategoriler = Arrays.asList(
            new Kategori("Roman", "Roman kategorisi"),
            new Kategori("Bilim Kurgu", "Bilim kurgu kitapları"),
            new Kategori("Tarih", "Tarih kitapları"),
            new Kategori("Felsefe", "Felsefe kitapları"),
            new Kategori("Çocuk", "Çocuk kitapları"),
            new Kategori("Edebiyat", "Edebiyat kitapları"),
            new Kategori("Bilim", "Bilim kitapları"),
            new Kategori("Sanat", "Sanat kitapları")
        );
        
        kategoriRepository.saveAll(kategoriler);

        // Kitapları yükle
        List<Kitap> kitaplar = Arrays.asList(
            new Kitap("Suç ve Ceza", "Fyodor Dostoyevski", new BigDecimal("25.50"), "Klasik Rus edebiyatının başyapıtı", 1L, "https://via.placeholder.com/400x600/2C3E50/FFFFFF?text=Suç+ve+Ceza%0AFyodor+Dostoyevski"),
            new Kitap("Bilinçaltının Gücü", "Joseph Murphy", new BigDecimal("30.00"), "Kişisel gelişim kitabı", 4L, "https://via.placeholder.com/400x600/34495E/FFFFFF?text=Bilinçaltının+Gücü%0AJoseph+Murphy"),
            new Kitap("Bilinmeyen Bir Kadının Mektubu", "Stefan Zweig", new BigDecimal("15.75"), "Duygusal bir aşk hikayesi", 1L, "https://via.placeholder.com/400x600/8E44AD/FFFFFF?text=Bilinmeyen+Bir+Kadının+Mektubu%0AStefan+Zweig"),
            new Kitap("1984", "George Orwell", new BigDecimal("22.00"), "Distopik roman", 2L, "https://via.placeholder.com/400x600/E74C3C/FFFFFF?text=1984%0AGeorge+Orwell"),
            new Kitap("Hayvan Çiftliği", "George Orwell", new BigDecimal("18.50"), "Alegorik roman", 1L, "https://via.placeholder.com/400x600/27AE60/FFFFFF?text=Hayvan+Çiftliği%0AGeorge+Orwell"),
            new Kitap("Dune", "Frank Herbert", new BigDecimal("35.00"), "Bilim kurgu klasiği", 2L, "https://via.placeholder.com/400x600/F39C12/FFFFFF?text=Dune%0AFrank+Herbert"),
            new Kitap("Sapiens", "Yuval Noah Harari", new BigDecimal("28.75"), "İnsanlık tarihi", 3L, "https://via.placeholder.com/400x600/3498DB/FFFFFF?text=Sapiens%0AYuval+Noah+Harari"),
            new Kitap("Homo Deus", "Yuval Noah Harari", new BigDecimal("32.00"), "Geleceğin tarihi", 3L, "https://via.placeholder.com/400x600/9B59B6/FFFFFF?text=Homo+Deus%0AYuval+Noah+Harari"),
            new Kitap("Küçük Prens", "Antoine de Saint-Exupéry", new BigDecimal("12.50"), "Çocuklar için klasik", 5L, "https://via.placeholder.com/400x600/E67E22/FFFFFF?text=Küçük+Prens%0AAntoine+de+Saint-Exupéry"),
            new Kitap("Simyacı", "Paulo Coelho", new BigDecimal("20.00"), "Felsefi roman", 4L, "https://via.placeholder.com/400x600/16A085/FFFFFF?text=Simyacı%0APaulo+Coelho"),
            new Kitap("Kürk Mantolu Madonna", "Sabahattin Ali", new BigDecimal("16.25"), "Türk edebiyatı klasiği", 6L, "https://via.placeholder.com/400x600/D35400/FFFFFF?text=Kürk+Mantolu+Madonna%0ASabahattin+Ali"),
            new Kitap("Harry Potter ve Felsefe Taşı", "J.K. Rowling", new BigDecimal("24.00"), "Fantastik roman", 5L, "https://via.placeholder.com/400x600/8E44AD/FFFFFF?text=Harry+Potter+ve+Felsefe+Taşı%0AJ.K.+Rowling"),
            new Kitap("Yüzüklerin Efendisi", "J.R.R. Tolkien", new BigDecimal("45.00"), "Fantastik epik", 2L, "https://via.placeholder.com/400x600/2ECC71/FFFFFF?text=Yüzüklerin+Efendisi%0AJ.R.R.+Tolkien"),
            new Kitap("Kuyucaklı Yusuf", "Sabahattin Ali", new BigDecimal("23.25"), "Anadolu'nun hikayesi", 1L, "https://via.placeholder.com/400x600/1ABC9C/FFFFFF?text=Kuyucaklı+Yusuf%0ASabahattin+Ali"),
            new Kitap("Masumiyet Müzesi", "Orhan Pamuk", new BigDecimal("29.00"), "Nobel ödüllü yazarın aşk romanı", 1L, "https://via.placeholder.com/400x600/2980B9/FFFFFF?text=Masumiyet+Müzesi%0AOrhan+Pamuk"),
            new Kitap("Fahrenheit 451", "Ray Bradbury", new BigDecimal("20.75"), "Distopik bilim kurgu", 2L, "https://via.placeholder.com/400x600/E67E22/FFFFFF?text=Fahrenheit+451%0ARay+Bradbury"),
            new Kitap("Brave New World", "Aldous Huxley", new BigDecimal("22.50"), "Distopik roman", 2L, "https://via.placeholder.com/400x600/8E44AD/FFFFFF?text=Brave+New+World%0AAldous+Huxley"),
            new Kitap("Zamanın Kısa Tarihi", "Stephen Hawking", new BigDecimal("31.00"), "Fizik ve kozmoloji", 7L, "https://via.placeholder.com/400x600/2C3E50/FFFFFF?text=Zamanın+Kısa+Tarihi%0AStephen+Hawking"),
            new Kitap("Sanat Tarihi", "Ernst Gombrich", new BigDecimal("42.00"), "Sanat tarihi rehberi", 8L, "https://via.placeholder.com/400x600/D35400/FFFFFF?text=Sanat+Tarihi%0AErnst+Gombrich"),
            new Kitap("Meditations", "Marcus Aurelius", new BigDecimal("18.00"), "Stoik felsefe", 4L, "https://via.placeholder.com/400x600/27AE60/FFFFFF?text=Meditations%0AMarcus+Aurelius"),
            new Kitap("Nicomachean Ethics", "Aristotle", new BigDecimal("25.00"), "Etik felsefesi", 4L, "https://via.placeholder.com/400x600/34495E/FFFFFF?text=Nicomachean+Ethics%0AAristotle"),
            new Kitap("The Republic", "Plato", new BigDecimal("27.50"), "Politik felsefe", 4L, "https://via.placeholder.com/400x600/9B59B6/FFFFFF?text=The+Republic%0APlato"),
            new Kitap("İnsan Hakları Evrensel Beyannamesi", "Birleşmiş Milletler", new BigDecimal("8.00"), "İnsan hakları metni", 3L, "https://via.placeholder.com/400x600/3498DB/FFFFFF?text=İnsan+Hakları+Evrensel+Beyannamesi%0ABirleşmiş+Milletler"),
            new Kitap("Osmanlı Tarihi", "İlber Ortaylı", new BigDecimal("35.50"), "Osmanlı İmparatorluğu tarihi", 3L, "https://via.placeholder.com/400x600/C0392B/FFFFFF?text=Osmanlı+Tarihi%0Aİlber+Ortaylı"),
            new Kitap("Nutuk", "Mustafa Kemal Atatürk", new BigDecimal("15.00"), "Kurtuluş Savaşı anıları", 3L, "https://via.placeholder.com/400x600/E74C3C/FFFFFF?text=Nutuk%0AMustafa+Kemal+Atatürk"),
            new Kitap("Alice Harikalar Diyarında", "Lewis Carroll", new BigDecimal("13.75"), "Çocuk klasiği", 5L, "https://via.placeholder.com/400x600/F39C12/FFFFFF?text=Alice+Harikalar+Diyarında%0ALewis+Carroll"),
            new Kitap("Pinokyo", "Carlo Collodi", new BigDecimal("11.50"), "Çocuk masalı", 5L, "https://via.placeholder.com/400x600/16A085/FFFFFF?text=Pinokyo%0ACarlo+Collodi")
        );
        
        kitapRepository.saveAll(kitaplar);
        
        System.out.println("Örnek veriler başarıyla yüklendi: " + kategoriler.size() + " kategori, " + kitaplar.size() + " kitap");
    }

    private void createAdminUser() {
        System.out.println("createAdminUser metodu çağrıldı");
        // Admin kullanıcısı zaten var mı kontrol et
        boolean adminExists = kullaniciRepository.existsByEmail("admin@kitap.com");
        System.out.println("Admin kullanıcısı var mı: " + adminExists);
        if (!adminExists) {
            Kullanici admin = new Kullanici();
            admin.setAdSoyad("Admin Kullanıcı");
            admin.setEmail("admin@kitap.com");
            admin.setSifreHash(passwordEncoder.encode("admin123"));
            admin.setRol("Admin");
            admin.setKayitTarihi(LocalDateTime.now());
            
            kullaniciRepository.save(admin);
            System.out.println("Admin kullanıcısı oluşturuldu: admin@kitap.com / admin123");
        }
    }
}