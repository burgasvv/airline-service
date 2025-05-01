package org.burgas.flightbackend.message;

public enum AuthMessages {

    NOT_AUTHENTICATED("Пользователь не аутентифицирован"),
    NOT_AUTHORIZED("Пользователь не авторизован");

    private final String message;

    AuthMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
