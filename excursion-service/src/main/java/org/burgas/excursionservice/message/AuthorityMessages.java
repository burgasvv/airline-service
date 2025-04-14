package org.burgas.excursionservice.message;

public enum AuthorityMessages {

    AUTHORITY_NOT_FOUND("Authority не найдена"),
    AUTHORITY_DELETED("Authority успешно удалена");

    private final String message;

    AuthorityMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
