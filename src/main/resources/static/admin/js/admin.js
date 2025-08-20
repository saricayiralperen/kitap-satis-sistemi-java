/**
 * Admin Panel JavaScript Functions
 * Kitap Satış Sistemi - Admin Panel
 */

// Global variables
let currentPage = 1;
let pageSize = 10;
let sortBy = 'id';
let sortDir = 'desc';
let searchTerm = '';
let filterStatus = '';

// Initialize admin panel
document.addEventListener('DOMContentLoaded', function() {
    initializeAdminPanel();
    initializeEventListeners();
    initializeTooltips();
    initializeModals();
    initializeDataTables();
    initializeCharts();
});

/**
 * Initialize admin panel
 */
function initializeAdminPanel() {
    // Set active navigation item
    setActiveNavigation();
    
    // Initialize form validation
    initializeFormValidation();
    
    // Initialize search functionality
    initializeSearch();
    
    // Initialize filters
    initializeFilters();
    
    // Auto-hide alerts after 5 seconds
    setTimeout(function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            if (alert.classList.contains('alert-success') || alert.classList.contains('alert-info')) {
                const bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            }
        });
    }, 5000);
}

/**
 * Set active navigation item
 */
function setActiveNavigation() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.sidebar .nav-link');
    
    navLinks.forEach(function(link) {
        const href = link.getAttribute('href');
        if (currentPath.includes(href) && href !== '/admin') {
            link.classList.add('active');
        } else if (currentPath === '/admin' && href === '/admin') {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });
}

/**
 * Initialize event listeners
 */
function initializeEventListeners() {
    // Sidebar toggle for mobile
    const sidebarToggle = document.getElementById('sidebarToggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function() {
            document.querySelector('.sidebar').classList.toggle('show');
        });
    }
    
    // Search input
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('input', debounce(function() {
            searchTerm = this.value;
            currentPage = 1;
            loadData();
        }, 300));
    }
    
    // Filter selects
    const filterSelects = document.querySelectorAll('.filter-select');
    filterSelects.forEach(function(select) {
        select.addEventListener('change', function() {
            currentPage = 1;
            loadData();
        });
    });
    
    // Sort buttons
    const sortButtons = document.querySelectorAll('.sort-btn');
    sortButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            const newSortBy = this.dataset.sort;
            if (sortBy === newSortBy) {
                sortDir = sortDir === 'asc' ? 'desc' : 'asc';
            } else {
                sortBy = newSortBy;
                sortDir = 'asc';
            }
            currentPage = 1;
            loadData();
        });
    });
    
    // Page size select
    const pageSizeSelect = document.getElementById('pageSizeSelect');
    if (pageSizeSelect) {
        pageSizeSelect.addEventListener('change', function() {
            pageSize = parseInt(this.value);
            currentPage = 1;
            loadData();
        });
    }
    
    // Delete buttons
    const deleteButtons = document.querySelectorAll('.delete-btn');
    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const id = this.dataset.id;
            const name = this.dataset.name || 'Bu öğe';
            confirmDelete(id, name, this.href);
        });
    });
    
    // Status change buttons
    const statusButtons = document.querySelectorAll('.status-btn');
    statusButtons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const id = this.dataset.id;
            const status = this.dataset.status;
            changeStatus(id, status, this.href);
        });
    });
}

/**
 * Initialize tooltips
 */
function initializeTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

/**
 * Initialize modals
 */
function initializeModals() {
    // Auto-focus first input in modals
    const modals = document.querySelectorAll('.modal');
    modals.forEach(function(modal) {
        modal.addEventListener('shown.bs.modal', function() {
            const firstInput = this.querySelector('input, select, textarea');
            if (firstInput) {
                firstInput.focus();
            }
        });
    });
}

/**
 * Initialize form validation
 */
function initializeFormValidation() {
    const forms = document.querySelectorAll('.needs-validation');
    forms.forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
                
                // Focus first invalid field
                const firstInvalid = form.querySelector(':invalid');
                if (firstInvalid) {
                    firstInvalid.focus();
                }
            }
            form.classList.add('was-validated');
        }, false);
    });
}

/**
 * Initialize search functionality
 */
function initializeSearch() {
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
        searchForm.addEventListener('submit', function(e) {
            e.preventDefault();
            currentPage = 1;
            loadData();
        });
    }
}

/**
 * Initialize filters
 */
function initializeFilters() {
    const filterForm = document.getElementById('filterForm');
    if (filterForm) {
        filterForm.addEventListener('submit', function(e) {
            e.preventDefault();
            currentPage = 1;
            loadData();
        });
    }
    
    // Clear filters button
    const clearFiltersBtn = document.getElementById('clearFilters');
    if (clearFiltersBtn) {
        clearFiltersBtn.addEventListener('click', function() {
            const filterInputs = document.querySelectorAll('.filter-input');
            filterInputs.forEach(function(input) {
                if (input.type === 'select-one') {
                    input.selectedIndex = 0;
                } else {
                    input.value = '';
                }
            });
            currentPage = 1;
            loadData();
        });
    }
}

/**
 * Initialize data tables
 */
function initializeDataTables() {
    // Add row click handlers for navigation
    const tableRows = document.querySelectorAll('.data-table tbody tr');
    tableRows.forEach(function(row) {
        if (row.dataset.href) {
            row.style.cursor = 'pointer';
            row.addEventListener('click', function(e) {
                if (!e.target.closest('button, a')) {
                    window.location.href = this.dataset.href;
                }
            });
        }
    });
}

/**
 * Initialize charts (if Chart.js is available)
 */
function initializeCharts() {
    if (typeof Chart !== 'undefined') {
        // Sales chart
        const salesChartCanvas = document.getElementById('salesChart');
        if (salesChartCanvas) {
            initializeSalesChart(salesChartCanvas);
        }
        
        // Orders chart
        const ordersChartCanvas = document.getElementById('ordersChart');
        if (ordersChartCanvas) {
            initializeOrdersChart(ordersChartCanvas);
        }
    }
}

/**
 * Load data with current filters and pagination
 */
function loadData() {
    const currentUrl = new URL(window.location);
    const params = new URLSearchParams();
    
    params.set('page', currentPage);
    params.set('size', pageSize);
    params.set('sort', sortBy + ',' + sortDir);
    
    if (searchTerm) {
        params.set('search', searchTerm);
    }
    
    // Add filter parameters
    const filterInputs = document.querySelectorAll('.filter-input');
    filterInputs.forEach(function(input) {
        if (input.value) {
            params.set(input.name, input.value);
        }
    });
    
    // Update URL without page reload
    currentUrl.search = params.toString();
    window.history.replaceState({}, '', currentUrl);
    
    // Reload page to apply filters
    window.location.reload();
}

/**
 * Confirm delete action
 */
function confirmDelete(id, name, deleteUrl) {
    const modal = document.getElementById('deleteModal');
    if (modal) {
        const modalBody = modal.querySelector('.modal-body');
        const deleteForm = modal.querySelector('#deleteForm');
        
        modalBody.innerHTML = `<p><strong>${name}</strong> öğesini silmek istediğinizden emin misiniz?</p>
                              <div class="alert alert-warning">
                                  <i class="fas fa-exclamation-triangle"></i>
                                  <strong>Uyarı:</strong> Bu işlem geri alınamaz.
                              </div>`;
        
        deleteForm.action = deleteUrl;
        
        const bsModal = new bootstrap.Modal(modal);
        bsModal.show();
    } else {
        if (confirm(`${name} öğesini silmek istediğinizden emin misiniz?`)) {
            window.location.href = deleteUrl;
        }
    }
}

/**
 * Change status
 */
function changeStatus(id, status, statusUrl) {
    const statusText = status === 'active' ? 'aktif' : 'pasif';
    if (confirm(`Bu öğenin durumunu ${statusText} yapmak istediğinizden emin misiniz?`)) {
        fetch(statusUrl, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ status: status })
        })
        .then(response => {
            if (response.ok) {
                showAlert('Durum başarıyla güncellendi.', 'success');
                setTimeout(() => window.location.reload(), 1000);
            } else {
                showAlert('Durum güncellenirken hata oluştu.', 'danger');
            }
        })
        .catch(error => {
            showAlert('Durum güncellenirken hata oluştu.', 'danger');
        });
    }
}

/**
 * Show alert message
 */
function showAlert(message, type = 'info') {
    const alertContainer = document.getElementById('alertContainer') || document.querySelector('.main');
    const alertId = 'alert-' + Date.now();
    
    const alertHtml = `
        <div id="${alertId}" class="alert alert-${type} alert-dismissible fade show" role="alert">
            <i class="fas fa-${getAlertIcon(type)}"></i>
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `;
    
    alertContainer.insertAdjacentHTML('afterbegin', alertHtml);
    
    // Auto-hide after 5 seconds
    setTimeout(() => {
        const alert = document.getElementById(alertId);
        if (alert) {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }
    }, 5000);
}

/**
 * Get alert icon based on type
 */
function getAlertIcon(type) {
    switch (type) {
        case 'success': return 'check-circle';
        case 'danger': return 'exclamation-triangle';
        case 'warning': return 'exclamation-triangle';
        case 'info': return 'info-circle';
        default: return 'info-circle';
    }
}

/**
 * Debounce function
 */
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

/**
 * Format currency
 */
function formatCurrency(amount) {
    return new Intl.NumberFormat('tr-TR', {
        style: 'currency',
        currency: 'TRY'
    }).format(amount);
}

/**
 * Format date
 */
function formatDate(dateString) {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('tr-TR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    }).format(date);
}

/**
 * Copy to clipboard
 */
function copyToClipboard(text) {
    navigator.clipboard.writeText(text).then(() => {
        showAlert('Panoya kopyalandı!', 'success');
    }).catch(() => {
        showAlert('Kopyalama başarısız!', 'danger');
    });
}

/**
 * Export data
 */
function exportData(format = 'excel') {
    const currentUrl = new URL(window.location);
    currentUrl.searchParams.set('export', format);
    window.open(currentUrl.toString(), '_blank');
}

/**
 * Print page
 */
function printPage() {
    window.print();
}

/**
 * Initialize sales chart
 */
function initializeSalesChart(canvas) {
    const ctx = canvas.getContext('2d');
    
    // Sample data - replace with actual data from server
    const salesData = {
        labels: ['Ocak', 'Şubat', 'Mart', 'Nisan', 'Mayıs', 'Haziran'],
        datasets: [{
            label: 'Satışlar',
            data: [12000, 19000, 15000, 25000, 22000, 30000],
            borderColor: 'rgb(102, 126, 234)',
            backgroundColor: 'rgba(102, 126, 234, 0.1)',
            tension: 0.4
        }]
    };
    
    new Chart(ctx, {
        type: 'line',
        data: salesData,
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: 'Aylık Satış Grafiği'
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return formatCurrency(value);
                        }
                    }
                }
            }
        }
    });
}

/**
 * Initialize orders chart
 */
function initializeOrdersChart(canvas) {
    const ctx = canvas.getContext('2d');
    
    // Sample data - replace with actual data from server
    const ordersData = {
        labels: ['Beklemede', 'Onaylandı', 'Kargolandı', 'Teslim Edildi', 'İptal Edildi'],
        datasets: [{
            data: [15, 45, 25, 80, 5],
            backgroundColor: [
                '#ffc107',
                '#17a2b8',
                '#28a745',
                '#007bff',
                '#dc3545'
            ]
        }]
    };
    
    new Chart(ctx, {
        type: 'doughnut',
        data: ordersData,
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: 'Sipariş Durumları'
                },
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

/**
 * Bulk actions
 */
function initializeBulkActions() {
    const selectAllCheckbox = document.getElementById('selectAll');
    const itemCheckboxes = document.querySelectorAll('.item-checkbox');
    const bulkActionSelect = document.getElementById('bulkAction');
    const bulkActionBtn = document.getElementById('bulkActionBtn');
    
    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener('change', function() {
            itemCheckboxes.forEach(checkbox => {
                checkbox.checked = this.checked;
            });
            updateBulkActionButton();
        });
    }
    
    itemCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', updateBulkActionButton);
    });
    
    if (bulkActionBtn) {
        bulkActionBtn.addEventListener('click', function() {
            const selectedItems = Array.from(itemCheckboxes)
                .filter(checkbox => checkbox.checked)
                .map(checkbox => checkbox.value);
            
            const action = bulkActionSelect.value;
            
            if (selectedItems.length === 0) {
                showAlert('Lütfen en az bir öğe seçin.', 'warning');
                return;
            }
            
            if (!action) {
                showAlert('Lütfen bir işlem seçin.', 'warning');
                return;
            }
            
            executeBulkAction(action, selectedItems);
        });
    }
}

/**
 * Update bulk action button state
 */
function updateBulkActionButton() {
    const itemCheckboxes = document.querySelectorAll('.item-checkbox');
    const bulkActionBtn = document.getElementById('bulkActionBtn');
    const selectedCount = Array.from(itemCheckboxes).filter(checkbox => checkbox.checked).length;
    
    if (bulkActionBtn) {
        bulkActionBtn.disabled = selectedCount === 0;
        bulkActionBtn.textContent = selectedCount > 0 ? `İşlem Uygula (${selectedCount})` : 'İşlem Uygula';
    }
}

/**
 * Execute bulk action
 */
function executeBulkAction(action, selectedItems) {
    const actionText = {
        'delete': 'silmek',
        'activate': 'aktif yapmak',
        'deactivate': 'pasif yapmak',
        'export': 'dışa aktarmak'
    };
    
    const confirmText = `${selectedItems.length} öğeyi ${actionText[action]} istediğinizden emin misiniz?`;
    
    if (confirm(confirmText)) {
        // Show loading
        showAlert('İşlem gerçekleştiriliyor...', 'info');
        
        // Send request to server
        fetch('/admin/bulk-action', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                action: action,
                items: selectedItems
            })
        })
        .then(response => {
            if (response.ok) {
                showAlert('İşlem başarıyla tamamlandı.', 'success');
                setTimeout(() => window.location.reload(), 1000);
            } else {
                showAlert('İşlem gerçekleştirilirken hata oluştu.', 'danger');
            }
        })
        .catch(error => {
            showAlert('İşlem gerçekleştirilirken hata oluştu.', 'danger');
        });
    }
}

// Initialize bulk actions when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    initializeBulkActions();
});

// Export functions for global use
window.AdminPanel = {
    showAlert,
    confirmDelete,
    changeStatus,
    formatCurrency,
    formatDate,
    copyToClipboard,
    exportData,
    printPage,
    loadData
};