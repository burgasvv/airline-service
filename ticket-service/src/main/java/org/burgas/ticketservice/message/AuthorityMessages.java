package org.burgas.ticketservice.message;

public enum AuthorityMessages {

    AUTHORITY_NOT_CREATED("Authority не была создана"),
    AUTHORITY_DELETED("Authority успешно удалена"),
    AUTHORITY_NOT_FOUND("Authority не найдена"),
    AUTHORITY_NOT_FOUND_ASYNC("Authority не найдена async");

    private final String message;

    AuthorityMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
