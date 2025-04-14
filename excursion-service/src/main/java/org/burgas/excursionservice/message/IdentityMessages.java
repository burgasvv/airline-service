package org.burgas.excursionservice.message;

public enum IdentityMessages {

    IDENTITY_NOT_AUTHENTICATED("Пользователь не аутентифицирован"),
    IDENTITY_NOT_AUTHORIZED("Пользователь не авторизован"),
    IDENTITY_NOT_FOUND("Указанный вами пользователь не найден"),
    IDENTITY_DELETED("Пользователь успешно удален"),
    PHONE_NOT_MATCHES("Телефонный номер не подходит по шаблону"),
    IDENTITY_ENABLED("Аккаунт активирован"),
    IDENTITY_NOT_ENABLED("Аккаунт заблокирован"),
    IDENTITY_SELF_CONTROL("Вы не можете заблокировать собственный аккаунт");

    private final String message;

    IdentityMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
