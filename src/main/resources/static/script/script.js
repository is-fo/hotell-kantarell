document.addEventListener('DOMContentLoaded', function() {
    const btn = document.getElementById('close-error');
    if (btn) {
        btn.addEventListener('click', () => {
            const popup = document.getElementById('error-popup');
            if (popup) popup.style.display = 'none';
        });
    }
});

function toggleForm(id) {
    const form = document.getElementById("update-form-" + id);
    if (form.style.display === "none") {
        form.style.display = "block";
    } else {
        form.style.display = "none";
    }
}