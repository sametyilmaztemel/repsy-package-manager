# Repsy Paket Yöneticisi

Yazılım paketlerini yönetmek ve dağıtmak için geliştirilmiş bir Spring Boot uygulaması.

## Özellikler

- Versiyonlama ile paket dağıtımı
- Paket indirme fonksiyonalitesi
- Güvenli kimlik doğrulama ve yetkilendirme
- Dosya boyutu sınırlaması
- RESTful API endpointleri
- Swagger dokümantasyonu

## Gereksinimler

- Java 17 veya üzeri
- Maven 3.8 veya üzeri
- Git

## Kurulum ve Çalıştırma

1. Projeyi klonlayın:
```bash
git clone https://github.com/yourusername/repsy-package-manager.git
cd repsy-package-manager
```

2. Projeyi derleyin:
```bash
./mvnw clean install
```

3. Uygulamayı çalıştırın:
```bash
./mvnw spring-boot:run
```

Uygulama `http://localhost:8080` adresinde başlayacaktır.

## API Dokümantasyonu

Uygulama çalışırken, Swagger dokümantasyonuna şu adresten erişebilirsiniz:
`http://localhost:8080/swagger-ui.html`

## Güvenlik

Uygulama temel kimlik doğrulama ile Spring Security kullanmaktadır. Varsayılan olarak:
- Paket indirme endpointleri herkese açıktır
- Paket dağıtım endpointleri kimlik doğrulaması gerektirir
- Basitlik için CSRF koruması devre dışı bırakılmıştır

## Proje Yapısı

```
src/
├── main/
│   ├── java/
│   │   └── com/repsy/packagemanager/
│   │       ├── config/         # Konfigürasyon sınıfları
│   │       ├── controller/     # REST controller'lar
│   │       ├── entity/         # JPA entity'leri
│   │       ├── exception/      # Hata yönetimi
│   │       ├── model/          # Veri modelleri
│   │       ├── repository/     # JPA repository'leri
│   │       ├── service/        # İş mantığı
│   │       └── storage/        # Dosya depolama stratejileri
│   └── resources/
│       └── application.yml     # Uygulama konfigürasyonu
└── test/                       # Test sınıfları
```

## Testler

Testleri çalıştırmak için:
```bash
./mvnw test
```

## Katkıda Bulunma

1. Projeyi fork edin
2. Özellik branch'inizi oluşturun (`git checkout -b ozellik/harika-ozellik`)
3. Değişikliklerinizi commit edin (`git commit -m 'Harika bir özellik ekle'`)
4. Branch'inizi push edin (`git push origin ozellik/harika-ozellik`)
5. Pull Request açın

## Deployment

Proje, tüm deployment sürecini otomatik olarak yöneten bir script içermektedir. Kullanmak için:

1. Script'i yapılandırın:
   ```bash
   # deploy.sh dosyasını düzenleyip bu değişkenleri güncelleyin:
   GITHUB_USERNAME="kullaniciadiniz"
   DOCKER_USERNAME="repsy"
   DOCKER_IMAGE_NAME="package-manager"
   EMAIL="contact@repsy.io"
   ```

2. Script'i çalıştırılabilir yapın:
   ```bash
   chmod +x deploy.sh
   ```

3. Deployment script'ini çalıştırın:
   ```bash
   ./deploy.sh
   ```

Script şunları yapacaktır:
- Git deposunu başlatır (eğer henüz yapılmadıysa)
- Public GitHub deposu oluşturur
- Storage modüllerini ayrı kütüphanelere ayırır
- Dockerfile oluşturur ve Docker imajını build eder
- Docker imajını Docker Hub'a push eder
- Tüm gerekli linkleri içeren gönderim e-postasını gönderir
