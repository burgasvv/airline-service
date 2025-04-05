package org.burgas.ticketservice.message;

public enum IdentityMessage {

    IDENTITY_TURNED_OFF("Пользователь отключен"),
    IDENTITY_TURNED_ON("Пользователь восстановлен"),
    IDENTITY_NOT_FOUND("Пользователь не найден"),
    IDENTITY_SAME_STATUS("Данный статус аккаунта уже установлен"),
    IDENTITY_TOKEN_WAS_SEND("Письмо с кодом было отправлено на вашу почту");

    private final String message;

    IdentityMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
