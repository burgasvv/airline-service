package org.burgas.ticketservice.message;

public enum AddressMessages {

    ADDRESS_NOT_FOUND("Адрес не был найден"),
    ADDRESS_DELETED("Адрес был успешно удален"),
    ADDRESS_NOT_CREATED("Адрес не был создан");

    private final String message;

    AddressMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
