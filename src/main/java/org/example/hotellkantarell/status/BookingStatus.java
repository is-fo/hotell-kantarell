package org.example.hotellkantarell.status;

public enum BookingStatus {
    NO_SUCH_BOOKING("Kunde inte hitta bokningen."),
    MALFORMED_BOOKING_USER("Något gick fel, bokningen har ingen användare."),
    MALFORMED_BOOKING_ROOM("Bokningen information om saknar rum."),
    EXPIRED_DATE("Datum är i dåtid"),
    REVERSE_DATE("Incheckningsdatum är efter utcheckningsdatum."),
    DOUBLE_BOOKED("Rummet är redan bokat."),
    ILLEGAL_ACCESS("Åtkomst nekad."),
    SUCCESS("Bokningen lyckades!");

    private final String message;

    BookingStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
