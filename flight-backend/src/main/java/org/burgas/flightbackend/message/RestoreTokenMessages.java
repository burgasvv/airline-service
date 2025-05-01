package org.burgas.flightbackend.message;

public enum RestoreTokenMessages {

    WRONG_RESTORE_TOKEN("Неверный токен идентификации пароля");

    private final String message;

    RestoreTokenMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
