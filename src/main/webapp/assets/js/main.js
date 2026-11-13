/**
 * SkillSwap - SRM UNIVERSITY AP Platform JavaScript
 * Interactive UI behaviors, validation helpers, modal triggers, and quick test fills.
 */

document.addEventListener('DOMContentLoaded', function () {
    // 1. Auto-dismiss flash alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            if (bsAlert) {
                bsAlert.close();
            }
        }, 5000);
    });

    // 2. SRM AP Student ID input uppercase auto-formatting
    const studentIdInputs = document.querySelectorAll('input[name="studentId"]');
    studentIdInputs.forEach(function (input) {
        input.addEventListener('input', function (e) {
            this.value = this.value.toUpperCase();
            const pattern = /^AP[0-9]{9,13}$/;
            const feedback = document.getElementById('studentIdFeedback');
            if (feedback) {
                if (this.value.length > 2 && !pattern.test(this.value)) {
                    feedback.textContent = "Format: AP followed by 11 digits (e.g. AP21110010001)";
                    feedback.classList.remove('text-muted');
                    feedback.classList.add('text-danger');
                } else {
                    feedback.textContent = "Valid SRM AP Student ID format";
                    feedback.classList.remove('text-danger');
                    feedback.classList.add('text-success');
                }
            }
        });
    });

    // 3. Quick test account filler on Login page
    window.fillLogin = function (identifier, password) {
        const idInput = document.querySelector('input[name="identifier"]');
        const passInput = document.querySelector('input[name="password"]');
        if (idInput && passInput) {
            idInput.value = identifier;
            passInput.value = password;
            // Highlight fields briefly
            idInput.classList.add('border-primary');
            passInput.classList.add('border-primary');
            setTimeout(() => {
                idInput.classList.remove('border-primary');
                passInput.classList.remove('border-primary');
            }, 600);
        }
    };

    // 4. Interactive Star Rating selector in Review Modals
    const starContainers = document.querySelectorAll('.star-rating-selector');
    starContainers.forEach(function (container) {
        const stars = container.querySelectorAll('.star-icon');
        const hiddenInput = container.querySelector('input[type="hidden"]');

        stars.forEach(function (star, index) {
            star.addEventListener('click', function () {
                const rating = index + 1;
                hiddenInput.value = rating;
                stars.forEach((s, i) => {
                    if (i < rating) {
                        s.classList.remove('bi-star');
                        s.classList.add('bi-star-fill', 'text-warning');
                    } else {
                        s.classList.remove('bi-star-fill', 'text-warning');
                        s.classList.add('bi-star');
                    }
                });
            });
        });
    });

    // 5. Client-side card search filter
    const cardSearch = document.getElementById('liveSearchInput');
    if (cardSearch) {
        cardSearch.addEventListener('input', function () {
            const query = this.value.toLowerCase().trim();
            const cards = document.querySelectorAll('.searchable-item');
            cards.forEach(function (card) {
                const text = card.textContent.toLowerCase();
                if (text.includes(query)) {
                    card.style.display = '';
                } else {
                    card.style.display = 'none';
                }
            });
        });
    }

    // 6. Bootstrap Tooltips activation
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
});

