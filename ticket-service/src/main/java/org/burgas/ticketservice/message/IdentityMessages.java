package org.burgas.ticketservice.message;

public enum IdentityMessages {

    IDENTITY_NOT_CREATED("Аккаунт пользователя не был создан"),
    IDENTITY_IMAGE_UPLOADED("Изображение пользователя с идентификатором %s успешно загружено"),
    IDENTITY_IMAGE_CHANGED("Изображение пользователя с идентификатором %s успешно изменено"),
    IDENTITY_IMAGE_DELETED("Изображение пользователя с идентификатором %s успешно удалено"),
    IDENTITY_TURNED_OFF("Пользователь отключен"),
    IDENTITY_TURNED_ON("Пользователь восстановлен"),
    IDENTITY_NOT_FOUND("Пользователь не найден"),
    IDENTITY_SAME_STATUS("Данный статус аккаунта уже установлен"),
    IDENTITY_TOKEN_WAS_SEND("Письмо с кодом было отправлено на вашу почту"),
    PHONE_NOT_MATCHES("Введенная строка не может быть телефоном"),
    IDENTITY_WRONG_ID("Неверный идентификатор пользователя"),
    IDENTITY_WRONG_TOKEN("Указанный вами токен не существует");

    private final String message;

    IdentityMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
