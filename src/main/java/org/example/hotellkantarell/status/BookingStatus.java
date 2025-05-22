package org.example.hotellkantarell.status;

public enum BookingStatus {
    NO_SUCH_BOOKING("KAFFE I SERVERN FINNS INGEN SÅN BOKNING KALLA PÅ ALLA DATABASTECKNINKER"),
    MALFORMED_BOOKING_USER("The booking has no user attached."),
    MALFORMED_BOOKING_ROOM("The room has no room attached."),
    EXPIRED_DATE("The date is in the past."),
    REVERSE_DATE("The start date is after the end date."),
    DOUBLE_BOOKED("The room is already booked."),
    ILLEGAL_ACCESS("yea we calling the fkn cops on u"),
    SUCCESS("yaaaaaaaaahhooooooooo");

    private final String message;

    BookingStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
