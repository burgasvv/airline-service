package org.burgas.ticketservice.message;

public enum AuthMessage {

    NOT_AUTHENTICATED("Пользователь не аутентифицирован"),
    NOT_AUTHORIZED("Пользователь не авторизован");

    private final String message;

    AuthMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
