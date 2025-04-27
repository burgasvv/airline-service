package org.burgas.ticketservice.message;

public enum AirportMessages {

    AIRPORT_NOT_CREATED("Аэропорт не был создан");

    private final String message;

    AirportMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
