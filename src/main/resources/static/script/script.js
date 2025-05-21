document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.close-notification').forEach(button => {
        button.addEventListener('click', function() {
            const popup = button.closest('.notification-popup');
            if (popup) popup.style.display = 'none';
        });
    });
});

function toggleForm(id) {
    const form = document.getElementById("update-form-" + id);
    if (form.style.display === "none") {
        form.style.display = "block";
    } else {
        form.style.display = "none";
    }
}