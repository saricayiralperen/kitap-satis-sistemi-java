// AlperenBooks - Site JavaScript

// Tema değiştirme fonksiyonu
function toggleTheme() {
    const currentTheme = document.documentElement.getAttribute('data-theme');
    const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
    
    document.documentElement.setAttribute('data-theme', newTheme);
    localStorage.setItem('theme', newTheme);
    
    // Tema butonunun ikonunu güncelle
    const themeIcon = document.querySelector('.theme-toggle i');
    if (themeIcon) {
        themeIcon.className = newTheme === 'dark' ? 'bi bi-sun-fill' : 'bi bi-moon-fill';
    }
}

// Sayfa yüklendiğinde tema ayarını kontrol et
document.addEventListener('DOMContentLoaded', function() {
    // Kaydedilmiş tema varsa uygula, yoksa sistem tercihini kullan
    const savedTheme = localStorage.getItem('theme');
    const systemPrefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    const theme = savedTheme || (systemPrefersDark ? 'dark' : 'light');
    
    document.documentElement.setAttribute('data-theme', theme);
    
    // Tema butonunun ikonunu ayarla
    const themeIcon = document.querySelector('.theme-toggle i');
    if (themeIcon) {
        themeIcon.className = theme === 'dark' ? 'bi bi-sun-fill' : 'bi bi-moon-fill';
    }
    
    // Animasyonları başlat
    initAnimations();
    
    // Smooth scroll için
    initSmoothScroll();
    
    // Navbar scroll efekti
    initNavbarScroll();
    
    // Lazy loading için
    initLazyLoading();
});

// Animasyonları başlat
function initAnimations() {
    // Intersection Observer ile scroll animasyonları
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };
    
    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('fade-in');
                observer.unobserve(entry.target);
            }
        });
    }, observerOptions);
    
    // Animasyon yapılacak elementleri seç
    const animateElements = document.querySelectorAll('.card, .feature-card, .stats-card, .category-item');
    animateElements.forEach(el => {
        observer.observe(el);
    });
}

// Smooth scroll
function initSmoothScroll() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
}

// Navbar scroll efekti
function initNavbarScroll() {
    const navbar = document.querySelector('.navbar');
    if (!navbar) return;
    
    let lastScrollTop = 0;
    
    window.addEventListener('scroll', function() {
        const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
        
        if (scrollTop > 100) {
            navbar.classList.add('navbar-scrolled');
        } else {
            navbar.classList.remove('navbar-scrolled');
        }
        
        // Navbar gizleme/gösterme (isteğe bağlı)
        if (scrollTop > lastScrollTop && scrollTop > 200) {
            navbar.style.transform = 'translateY(-100%)';
        } else {
            navbar.style.transform = 'translateY(0)';
        }
        
        lastScrollTop = scrollTop;
    });
}

// Lazy loading
function initLazyLoading() {
    const images = document.querySelectorAll('img[data-src]');
    
    const imageObserver = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const img = entry.target;
                img.src = img.dataset.src;
                img.classList.remove('lazy');
                imageObserver.unobserve(img);
            }
        });
    });
    
    images.forEach(img => imageObserver.observe(img));
}

// Form validasyonu
function validateForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return false;
    
    let isValid = true;
    const inputs = form.querySelectorAll('input[required], select[required], textarea[required]');
    
    inputs.forEach(input => {
        if (!input.value.trim()) {
            showFieldError(input, 'Bu alan zorunludur.');
            isValid = false;
        } else {
            clearFieldError(input);
        }
        
        // Email validasyonu
        if (input.type === 'email' && input.value) {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(input.value)) {
                showFieldError(input, 'Geçerli bir email adresi giriniz.');
                isValid = false;
            }
        }
        
        // Telefon validasyonu
        if (input.type === 'tel' && input.value) {
            const phoneRegex = /^[0-9]{10,11}$/;
            if (!phoneRegex.test(input.value.replace(/\s/g, ''))) {
                showFieldError(input, 'Geçerli bir telefon numarası giriniz.');
                isValid = false;
            }
        }
    });
    
    return isValid;
}

// Alan hatası göster
function showFieldError(input, message) {
    clearFieldError(input);
    
    input.classList.add('is-invalid');
    
    const errorDiv = document.createElement('div');
    errorDiv.className = 'invalid-feedback';
    errorDiv.textContent = message;
    
    input.parentNode.appendChild(errorDiv);
}

// Alan hatasını temizle
function clearFieldError(input) {
    input.classList.remove('is-invalid');
    
    const errorDiv = input.parentNode.querySelector('.invalid-feedback');
    if (errorDiv) {
        errorDiv.remove();
    }
}

// Toast bildirimi göster
function showToast(message, type = 'info', duration = 3000) {
    const toastContainer = getOrCreateToastContainer();
    
    const toast = document.createElement('div');
    toast.className = `toast align-items-center text-white bg-${type} border-0`;
    toast.setAttribute('role', 'alert');
    toast.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">
                ${message}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
    `;
    
    toastContainer.appendChild(toast);
    
    const bsToast = new bootstrap.Toast(toast, {
        autohide: true,
        delay: duration
    });
    
    bsToast.show();
    
    // Toast kaldırıldıktan sonra DOM'dan sil
    toast.addEventListener('hidden.bs.toast', function() {
        toast.remove();
    });
}

// Toast container oluştur veya mevcut olanı getir
function getOrCreateToastContainer() {
    let container = document.querySelector('.toast-container');
    
    if (!container) {
        container = document.createElement('div');
        container.className = 'toast-container position-fixed bottom-0 end-0 p-3';
        document.body.appendChild(container);
    }
    
    return container;
}

// Loading göster/gizle
function showLoading(element) {
    if (typeof element === 'string') {
        element = document.querySelector(element);
    }
    
    if (element) {
        element.disabled = true;
        const originalText = element.textContent;
        element.dataset.originalText = originalText;
        element.innerHTML = '<span class="loading"></span> Yükleniyor...';
    }
}

function hideLoading(element) {
    if (typeof element === 'string') {
        element = document.querySelector(element);
    }
    
    if (element && element.dataset.originalText) {
        element.disabled = false;
        element.textContent = element.dataset.originalText;
        delete element.dataset.originalText;
    }
}

// AJAX yardımcı fonksiyonlar
function ajaxGet(url, successCallback, errorCallback) {
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        if (successCallback) successCallback(data);
    })
    .catch(error => {
        console.error('Error:', error);
        if (errorCallback) errorCallback(error);
    });
}

function ajaxPost(url, data, successCallback, errorCallback) {
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        if (successCallback) successCallback(data);
    })
    .catch(error => {
        console.error('Error:', error);
        if (errorCallback) errorCallback(error);
    });
}

// Sayfa geçişleri için animasyon
function animatePageTransition() {
    document.body.style.opacity = '0';
    document.body.style.transform = 'translateY(20px)';
    
    setTimeout(() => {
        document.body.style.transition = 'opacity 0.3s ease, transform 0.3s ease';
        document.body.style.opacity = '1';
        document.body.style.transform = 'translateY(0)';
    }, 100);
}

// Sistem tema değişikliğini dinle
window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', function(e) {
    // Kullanıcı manuel tema seçmemişse sistem temasını takip et
    if (!localStorage.getItem('theme')) {
        const theme = e.matches ? 'dark' : 'light';
        document.documentElement.setAttribute('data-theme', theme);
        
        const themeIcon = document.querySelector('.theme-toggle i');
        if (themeIcon) {
            themeIcon.className = theme === 'dark' ? 'bi bi-sun-fill' : 'bi bi-moon-fill';
        }
    }
});

// Klavye navigasyonu
document.addEventListener('keydown', function(e) {
    // ESC tuşu ile modal kapatma
    if (e.key === 'Escape') {
        const openModal = document.querySelector('.modal.show');
        if (openModal) {
            const modal = bootstrap.Modal.getInstance(openModal);
            if (modal) modal.hide();
        }
    }
    
    // Ctrl+K ile arama odaklama (varsa)
    if (e.ctrlKey && e.key === 'k') {
        e.preventDefault();
        const searchInput = document.querySelector('input[type="search"], input[name="search"]');
        if (searchInput) {
            searchInput.focus();
        }
    }
});

// Sayfa yüklenme performansı
window.addEventListener('load', function() {
    // Sayfa yüklenme süresini konsola yazdır (geliştirme için)
    const loadTime = performance.now();
    console.log(`Sayfa yüklenme süresi: ${Math.round(loadTime)}ms`);
    
    // Lazy loading için placeholder'ları kaldır
    document.querySelectorAll('.placeholder-glow').forEach(el => {
        el.classList.remove('placeholder-glow');
    });
});

// Hata yakalama
window.addEventListener('error', function(e) {
    console.error('JavaScript Hatası:', e.error);
    // Geliştirme ortamında hata bildirimi göster
    if (window.location.hostname === 'localhost') {
        showToast('Bir JavaScript hatası oluştu. Konsolu kontrol edin.', 'danger');
    }
});

// Promise hataları
window.addEventListener('unhandledrejection', function(e) {
    console.error('Promise Hatası:', e.reason);
    // Geliştirme ortamında hata bildirimi göster
    if (window.location.hostname === 'localhost') {
        showToast('Bir Promise hatası oluştu. Konsolu kontrol edin.', 'danger');
    }
});