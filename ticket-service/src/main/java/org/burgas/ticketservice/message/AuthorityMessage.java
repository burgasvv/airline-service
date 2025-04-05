package org.burgas.ticketservice.message;

public enum AuthorityMessage {

    AUTHORITY_DELETED("Authority успешно удалена"),
    AUTHORITY_NOT_FOUND("Authority не найдена");

    private final String message;

    AuthorityMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
