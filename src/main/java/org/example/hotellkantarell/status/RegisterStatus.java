package org.example.hotellkantarell.status;

public enum RegisterStatus {
    MISSING_NAME("Vänligen ange ditt namn"),
    MISSING_PASSWORD("Vänligen ange ett lösenord"),
    MISSING_EMAIL("Vänligen ange din epostadress"),
    EMAIL_IN_USE("Det finns redan ett konto med den epostadressen"),
    SUCCESS("Du är nu registrerad!");
    private final String message;

    RegisterStatus(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
