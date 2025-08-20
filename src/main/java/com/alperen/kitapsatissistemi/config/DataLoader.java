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
        // Kullanıcıları oluştur (her zaman kontrol et)
        createSampleUsers();
        
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
        
        List<Kategori> savedKategoriler = kategoriRepository.saveAll(kategoriler);

        // Kitapları yükle - Kategori entity'lerini kullan
        List<Kitap> kitaplar = Arrays.asList(
            new Kitap("Suç ve Ceza", "Fyodor Dostoyevski", new BigDecimal("25.50"), "Klasik Rus edebiyatının başyapıtı", savedKategoriler.get(0), "/images/books/suc-ve-ceza.jpg"),
            new Kitap("Bilinçaltının Gücü", "Joseph Murphy", new BigDecimal("30.00"), "Kişisel gelişim kitabı", savedKategoriler.get(3), "/images/books/bilincaltinin-gucu.jpg"),
            new Kitap("Bilinmeyen Bir Kadının Mektubu", "Stefan Zweig", new BigDecimal("15.75"), "Duygusal bir aşk hikayesi", savedKategoriler.get(0), "/images/books/bilinmeyen-kadin-mektubu.jpg"),
            new Kitap("1984", "George Orwell", new BigDecimal("22.00"), "Distopik roman", savedKategoriler.get(1), "/images/books/1984.jpg"),
            new Kitap("Hayvan Çiftliği", "George Orwell", new BigDecimal("18.50"), "Alegorik roman", savedKategoriler.get(0), "/images/books/hayvan-ciftligi.jpg"),
            new Kitap("Dune", "Frank Herbert", new BigDecimal("35.00"), "Bilim kurgu klasiği", savedKategoriler.get(1), "/images/books/dune.jpg"),
            new Kitap("Sapiens", "Yuval Noah Harari", new BigDecimal("28.75"), "İnsanlık tarihi", savedKategoriler.get(2), "/images/books/sapiens.jpg"),
            new Kitap("Homo Deus", "Yuval Noah Harari", new BigDecimal("32.00"), "Geleceğin tarihi", savedKategoriler.get(2), "/images/books/homo-deus.jpg"),
            new Kitap("Küçük Prens", "Antoine de Saint-Exupéry", new BigDecimal("12.50"), "Çocuklar için klasik", savedKategoriler.get(4), "/images/books/kucuk-prens.jpg"),
            new Kitap("Simyacı", "Paulo Coelho", new BigDecimal("20.00"), "Felsefi roman", savedKategoriler.get(3), "/images/books/simyaci.jpg"),
            new Kitap("Kürk Mantolu Madonna", "Sabahattin Ali", new BigDecimal("16.25"), "Türk edebiyatı klasiği", savedKategoriler.get(5), "/images/books/kurk-mantolu-madonna.jpg"),
            new Kitap("Harry Potter ve Felsefe Taşı", "J.K. Rowling", new BigDecimal("24.00"), "Fantastik roman", savedKategoriler.get(4), "/images/books/harry-potter-felsefe-tasi.jpg"),
            new Kitap("Yüzüklerin Efendisi", "J.R.R. Tolkien", new BigDecimal("45.00"), "Fantastik epik", savedKategoriler.get(1), "/images/books/yuzuklerin-efendisi.jpg"),
            new Kitap("Kuyucaklı Yusuf", "Sabahattin Ali", new BigDecimal("23.25"), "Anadolu'nun hikayesi", savedKategoriler.get(0), "/images/books/kuyucakli-yusuf.jpg"),
            new Kitap("Masumiyet Müzesi", "Orhan Pamuk", new BigDecimal("29.00"), "Nobel ödüllü yazarın aşk romanı", savedKategoriler.get(0), "/images/books/masumiyet-muzesi.jpg"),
            new Kitap("Fahrenheit 451", "Ray Bradbury", new BigDecimal("20.75"), "Distopik bilim kurgu", savedKategoriler.get(1), "/images/books/fahrenheit-451.jpg"),
            new Kitap("Brave New World", "Aldous Huxley", new BigDecimal("22.50"), "Distopik roman", savedKategoriler.get(1), "/images/books/brave-new-world.jpg"),
            new Kitap("Zamanın Kısa Tarihi", "Stephen Hawking", new BigDecimal("31.00"), "Fizik ve kozmoloji", savedKategoriler.get(6), "/images/books/zamanin-kisa-tarihi.jpg"),
            new Kitap("Sanat Tarihi", "Ernst Gombrich", new BigDecimal("42.00"), "Sanat tarihi rehberi", savedKategoriler.get(7), "/images/books/sanat-tarihi.jpg"),
            new Kitap("Meditations", "Marcus Aurelius", new BigDecimal("18.00"), "Stoik felsefe", savedKategoriler.get(3), "/images/books/meditations.jpg"),
            new Kitap("Nicomachean Ethics", "Aristotle", new BigDecimal("25.00"), "Etik felsefesi", savedKategoriler.get(3), "/images/books/nicomachean-ethics.jpg"),
            new Kitap("The Republic", "Plato", new BigDecimal("27.50"), "Politik felsefe", savedKategoriler.get(3), "/images/books/the-republic.jpg"),
            new Kitap("İnsan Hakları Evrensel Beyannamesi", "Birleşmiş Milletler", new BigDecimal("8.00"), "İnsan hakları metni", savedKategoriler.get(2), "/images/books/insan-haklari-beyannamesi.jpg"),
            new Kitap("Osmanlı Tarihi", "İlber Ortaylı", new BigDecimal("35.50"), "Osmanlı İmparatorluğu tarihi", savedKategoriler.get(2), "/images/books/osmanli-tarihi.jpg"),
            new Kitap("Nutuk", "Mustafa Kemal Atatürk", new BigDecimal("15.00"), "Kurtuluş Savaşı anıları", savedKategoriler.get(2), "/images/books/nutuk.jpg"),
            new Kitap("Alice Harikalar Diyarında", "Lewis Carroll", new BigDecimal("13.75"), "Çocuk klasiği", savedKategoriler.get(4), "/images/books/alice-harikalar-diyarinda.jpg"),
            new Kitap("Pinokyo", "Carlo Collodi", new BigDecimal("11.50"), "Çocuk masalı", savedKategoriler.get(4), "/images/books/pinokyo.jpg")
        );
        
        kitapRepository.saveAll(kitaplar);
        
        System.out.println("Örnek veriler başarıyla yüklendi: " + kategoriler.size() + " kategori, " + kitaplar.size() + " kitap");
    }

    private void createSampleUsers() {
        System.out.println("createSampleUsers metodu çağrıldı");
        
        // Admin kullanıcısı zaten var mı kontrol et
        boolean adminExists = kullaniciRepository.existsByEmail("admin@kitap.com");
        System.out.println("Admin kullanıcısı var mı: " + adminExists);
        if (!adminExists) {
            Kullanici admin = new Kullanici();
            admin.setAdSoyad("Admin Kullanıcı");
            admin.setEmail("admin@kitap.com");
            // Environment variable'dan şifre al, yoksa varsayılan kullan
            String adminPassword = System.getenv("ADMIN_PASSWORD");
            if (adminPassword == null || adminPassword.trim().isEmpty()) {
                adminPassword = "Admin123!"; // Daha güçlü varsayılan şifre
            }
            admin.setSifreHash(passwordEncoder.encode(adminPassword));
            admin.setRol("Admin");
            admin.setKayitTarihi(LocalDateTime.now());
            
            kullaniciRepository.save(admin);
            System.out.println("Admin kullanıcısı oluşturuldu: admin@kitap.com / Admin123!");
        }
        
        // Test kullanıcısı zaten var mı kontrol et
        boolean testUserExists = kullaniciRepository.existsByEmail("test@kitap.com");
        if (!testUserExists) {
            Kullanici testUser = new Kullanici();
            testUser.setAdSoyad("Test Kullanıcı");
            testUser.setEmail("test@kitap.com");
            testUser.setSifreHash(passwordEncoder.encode("test123"));
            testUser.setRol("User");
            testUser.setKayitTarihi(LocalDateTime.now());
            
            kullaniciRepository.save(testUser);
            System.out.println("Test kullanıcısı oluşturuldu: test@kitap.com / test123");
        }
        
        // Demo kullanıcısı zaten var mı kontrol et
        boolean demoUserExists = kullaniciRepository.existsByEmail("demo@kitap.com");
        if (!demoUserExists) {
            Kullanici demoUser = new Kullanici();
            demoUser.setAdSoyad("Demo Kullanıcı");
            demoUser.setEmail("demo@kitap.com");
            demoUser.setSifreHash(passwordEncoder.encode("demo123"));
            demoUser.setRol("User");
            demoUser.setKayitTarihi(LocalDateTime.now());
            
            kullaniciRepository.save(demoUser);
            System.out.println("Demo kullanıcısı oluşturuldu: demo@kitap.com / demo123");
        }
    }
}
