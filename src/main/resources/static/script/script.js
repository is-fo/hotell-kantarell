document.addEventListener('DOMContentLoaded', function() {
    const btn = document.getElementById('close-error');
    if (btn) {
        btn.addEventListener('click', () => {
            const popup = document.getElementById('error-popup');
            if (popup) popup.style.display = 'none';
        });
    }
});