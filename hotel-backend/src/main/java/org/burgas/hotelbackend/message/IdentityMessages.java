package org.burgas.hotelbackend.message;

public enum IdentityMessages {

    IDENTITY_IMAGE_UPLOADED("Изображение с идентификатором %s было успешно загружено для пользователя с идентификатором %s"),
    IDENTITY_IMAGE_UPLOADED_ASYNC("Изображение с идентификатором %s было успешно загружено для пользователя с идентификатором %s async"),
    IDENTITY_IMAGE_CHANGED("Изображение с идентификатором %s было успешно изменено для пользователя с идентификатором %s"),
    IDENTITY_IMAGE_CHANGED_ASYNC("Изображение с идентификатором %s было успешно изменено для пользователя с идентификатором %s async"),
    IDENTITY_IMAGE_DELETED("Изображение было успешно удалено пользователем с идентификатором %s"),
    IDENTITY_IMAGE_DELETED_ASYNC("Изображение было успешно удалено пользователем с идентификатором %s async"),
    IDENTITY_STATUS_ALREADY_SET("Статус пользователя уже установлен эквивалентен предложенному"),
    IDENTITY_ACTIVATED("Пользователь активирован"),
    IDENTITY_DEACTIVATED("Пользователь деактивирован"),
    IDENTITY_NOT_FOUND("Пользователь не найден"),
    IDENTITY_NOT_CREATED("Пользователь не создан"),
    IDENTITY_NOT_AUTHENTICATED("Пользователь не аутентифицирован"),
    IDENTITY_NOT_AUTHORIZED("Пользователь не авторизован"),
    PHONE_NOT_MATCHES("Указанная строка не может быть телефоном");

    private final String message;

    IdentityMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
