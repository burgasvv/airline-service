package org.burgas.hotelbackend.message;

public enum AuthorityMessages {

    AUTHORITY_DELETED("Authority был успешно удален"),
    AUTHORITY_DELETED_ASYNC("Authority был успешно удален async"),
    AUTHORITY_NOT_CREATED("Authority не был создан"),
    AUTHORITY_NOT_FOUND("Authority не был найден");

    private final String message;

    AuthorityMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
