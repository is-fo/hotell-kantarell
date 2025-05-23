document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.close-notification').forEach(button => {
        button.addEventListener('click', function() {
            const popup = button.closest('.notification-popup');
            if (popup) popup.style.display = 'none';
        });
    });
});

function toggleForm(id) {
    const modal = document.getElementById('modal-' + id);
    modal.style.display = (modal.style.display === 'none' || modal.style.display === '') ? 'block' : 'none';
}