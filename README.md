# 📚 Kitap Satış Sistemi - Java Spring Boot

Bu proje, .NET C# ile yazılmış olan kitap satış sisteminin Java Spring Boot'a dönüştürülmüş halidir.

## 🚀 Özellikler

- **Kullanıcı Yönetimi**: Kayıt, giriş, profil yönetimi
- **Kitap Kataloğu**: Kitap listeleme, detay görüntüleme, kategori bazlı filtreleme
- **Sepet Sistemi**: Ürün ekleme/çıkarma, miktar güncelleme
- **Sipariş Yönetimi**: Sipariş oluşturma, sipariş geçmişi
- **Favori Sistemi**: Kitapları favorilere ekleme/çıkarma
- **Admin Paneli**: Kitap ve kategori yönetimi
- **Responsive Tasarım**: Mobil uyumlu arayüz

## 🛠️ Teknolojiler

- **Backend**: Java 11, Spring Boot 2.7.14
- **Database**: H2 (geliştirme), PostgreSQL (production)
- **ORM**: Spring Data JPA, Hibernate
- **Security**: Spring Security
- **Template Engine**: Thymeleaf
- **Frontend**: Bootstrap 5, jQuery
- **Build Tool**: Maven

## 📋 Kurulum

### Gereksinimler
- Java 11 veya üzeri
- Maven 3.6 veya üzeri

### Adımlar

1. **Projeyi klonlayın**
   ```bash
   git clone https://github.com/[username]/kitap-satis-sistemi-java.git
   cd kitap-satis-sistemi-java
   ```

2. **Uygulamayı çalıştırın**
   ```bash
   ./mvnw spring-boot:run
   ```
   
   Windows için:
   ```cmd
   .\mvnw.cmd spring-boot:run
   ```

3. **Tarayıcıda açın**
   ```
   http://localhost:8081
   ```

## 📱 Kullanım

### Örnek Kullanıcılar
- **Admin**: admin@test.com / admin123
- **Kullanıcı**: user@test.com / user123

### Ana Sayfalar
- **Ana Sayfa**: `/`
- **Kitaplar**: `/kitaplar`
- **Kategoriler**: `/kategori-listesi`
- **Sepet**: `/sepet`
- **Siparişler**: `/siparisler`
- **Favoriler**: `/favoriler`
- **Admin Panel**: `/admin`

## 🗂️ Proje Yapısı

```
src/main/java/com/alperen/kitapsatissistemi/
├── controller/          # Web ve API controller'ları
├── entity/             # JPA entity'leri
├── repository/         # Data repository'leri
├── service/            # Business logic
├── config/             # Konfigürasyon sınıfları
└── KitapSatisSistemiApplication.java

src/main/resources/
├── templates/          # Thymeleaf şablonları
├── static/            # CSS, JS, resim dosyaları
└── application.properties
```

## 🔧 Konfigürasyon

### Veritabanı
Geliştirme ortamında H2 in-memory database kullanılır. Production için `application.properties` dosyasını güncelleyin:

```properties
# PostgreSQL için
spring.datasource.url=jdbc:postgresql://localhost:5432/kitap_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## Proje Yapısı

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── alperen/
│   │           └── kitapsatissistemi/
│   │               ├── entity/          # Varlık sınıfları
│   │               │   ├── Kategori.java
│   │               │   ├── Kitap.java
│   │               │   ├── Kullanici.java
│   │               │   ├── Favori.java
│   │               │   ├── Siparis.java
│   │               │   └── SiparisDetay.java
│   │               ├── repository/      # Veri erişim katmanı
│   │               │   ├── KategoriRepository.java
│   │               │   ├── KitapRepository.java
│   │               │   ├── KullaniciRepository.java
│   │               │   ├── FavoriRepository.java
│   │               │   ├── SiparisRepository.java
│   │               │   └── SiparisDetayRepository.java
│   │               ├── service/         # İş mantığı katmanı
│   │               │   ├── KategoriService.java
│   │               │   ├── KitapService.java
│   │               │   ├── KullaniciService.java
│   │               │   ├── FavoriService.java
│   │               │   ├── SiparisService.java
│   │               │   └── SiparisDetayService.java
│   │               ├── controller/      # REST API katmanı
│   │               │   ├── KategoriController.java
│   │               │   ├── KitapController.java
│   │               │   ├── KullaniciController.java
│   │               │   ├── FavoriController.java
│   │               │   ├── SiparisController.java
│   │               │   └── SiparisDetayController.java
│   │               └── KitapSatisSistemiApplication.java
│   └── resources/
│       └── application.properties       # Uygulama konfigürasyonu
└── test/                               # Test dosyaları
```

## Kurulum ve Çalıştırma

### Gereksinimler

- Java 11 veya üzeri
- Maven 3.6 veya üzeri

### Adımlar

1. **Projeyi klonlayın:**
   ```bash
   git clone <repository-url>
   cd JavaSpringProjesiAlperen
   ```

2. **Bağımlılıkları yükleyin:**
   ```bash
   mvn clean install
   ```

3. **Uygulamayı çalıştırın:**
   ```bash
   mvn spring-boot:run
   ```

4. **Tarayıcıda açın:**
   - Uygulama: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console
   - Swagger UI: http://localhost:8080/swagger-ui.html

## API Endpoints

### Kategoriler
- `GET /api/kategoriler` - Tüm kategorileri listele
- `GET /api/kategoriler/{id}` - ID'ye göre kategori getir
- `POST /api/kategoriler` - Yeni kategori oluştur
- `PUT /api/kategoriler/{id}` - Kategori güncelle
- `DELETE /api/kategoriler/{id}` - Kategori sil

### Kitaplar
- `GET /api/kitaplar` - Tüm kitapları listele
- `GET /api/kitaplar/{id}` - ID'ye göre kitap getir
- `POST /api/kitaplar` - Yeni kitap oluştur
- `PUT /api/kitaplar/{id}` - Kitap güncelle
- `DELETE /api/kitaplar/{id}` - Kitap sil

### Kullanıcılar
- `GET /api/kullanicilar` - Tüm kullanıcıları listele
- `POST /api/kullanicilar/register` - Kullanıcı kaydı
- `POST /api/kullanicilar/login` - Kullanıcı girişi
- `PUT /api/kullanicilar/{id}` - Kullanıcı güncelle

### Favoriler
- `GET /api/favoriler` - Tüm favorileri listele
- `POST /api/favoriler` - Favoriye ekle
- `DELETE /api/favoriler/{id}` - Favoriden çıkar

### Siparişler
- `GET /api/siparisler` - Tüm siparişleri listele
- `POST /api/siparisler` - Yeni sipariş oluştur
- `PUT /api/siparisler/{id}` - Sipariş güncelle
- `DELETE /api/siparisler/{id}` - Sipariş sil

### Sipariş Detayları
- `GET /api/siparis-detaylar` - Tüm sipariş detaylarını listele
- `POST /api/siparis-detaylar` - Yeni sipariş detayı oluştur
- `PUT /api/siparis-detaylar/{id}` - Sipariş detayı güncelle
- `DELETE /api/siparis-detaylar/{id}` - Sipariş detayı sil

## Veritabanı

### Geliştirme Ortamı
Geliştirme ortamında H2 in-memory veritabanı kullanılmaktadır. Uygulama her başlatıldığında veritabanı sıfırlanır.

### Production Ortamı
Production ortamında MySQL veritabanı kullanılabilir. `application.properties` dosyasında gerekli konfigürasyonları yapmanız gerekmektedir.

## Özellikler

- ✅ Kategori yönetimi
- ✅ Kitap yönetimi
- ✅ Kullanıcı yönetimi ve kimlik doğrulama
- ✅ Favori kitaplar
- ✅ Sipariş yönetimi
- ✅ Sipariş detayları
- ✅ Arama ve filtreleme
- ✅ İstatistikler ve raporlar
- ✅ REST API
- ✅ Swagger dokümantasyonu
- ✅ Validation (doğrulama)
- ✅ Exception handling (hata yönetimi)

## Geliştirici

**Alperen Sarıçayır**

## Lisans

Bu proje MIT lisansı altında lisanslanmıştır.

## Notlar

Bu proje, orijinal .NET C# Kitap Satış Sistemi projesinin Java Spring Boot'a dönüştürülmüş halidir. Tüm işlevsellik ve API endpoint'leri orijinal projeyle uyumlu olacak şekilde tasarlanmıştır.