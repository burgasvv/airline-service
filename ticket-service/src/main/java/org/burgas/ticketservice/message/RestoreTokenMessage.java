package org.burgas.ticketservice.message;

public enum RestoreTokenMessage {

    WRONG_RESTORE_TOKEN("Неверный токен идентификации пароля");

    private final String message;

    RestoreTokenMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
