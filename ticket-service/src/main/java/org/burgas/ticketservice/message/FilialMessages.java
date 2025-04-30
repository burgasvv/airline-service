package org.burgas.ticketservice.message;

public enum FilialMessages {

    FILIAL_NOT_FOUND("Филиал не был найден"),
    FILIAL_DELETED("Филиал был удален"),
    FILIAL_NOT_CREATED("Филиал не был создан");

    private final String message;

    FilialMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
