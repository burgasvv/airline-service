package org.burgas.flightbackend.message;

public enum AirportMessages {

    AIRPORT_NOT_FOUND("Аэропорт не найден"),
    AIRPORT_DELETED("Аэропорт успешно удален"),
    AIRPORT_NOT_CREATED("Аэропорт не был создан");

    private final String message;

    AirportMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
