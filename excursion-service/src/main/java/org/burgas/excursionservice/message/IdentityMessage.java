package org.burgas.excursionservice.message;

public enum IdentityMessage {

    IDENTITY_NOT_AUTHENTICATED("Пользователь не аутентифицирован"),
    IDENTITY_NOT_AUTHORIZED("Пользователь не авторизован"),
    IDENTITY_NOT_FOUND("Указанный вами пользователь не найден"),
    IDENTITY_DELETED("Пользователь успешно удален");

    private final String message;

    IdentityMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
