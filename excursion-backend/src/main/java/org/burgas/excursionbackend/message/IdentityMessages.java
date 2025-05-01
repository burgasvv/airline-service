package org.burgas.excursionbackend.message;

public enum IdentityMessages {

    IDENTITY_NOT_AUTHENTICATED("Пользователь не аутентифицирован"),
    IDENTITY_NOT_AUTHORIZED("Пользователь не авторизован"),
    IDENTITY_NOT_FOUND("Указанный вами пользователь не найден"),
    IDENTITY_DELETED("Пользователь успешно удален"),
    PHONE_NOT_MATCHES("Телефонный номер не подходит по шаблону"),
    IDENTITY_ENABLED("Аккаунт активирован"),
    IDENTITY_NOT_ENABLED("Аккаунт заблокирован"),
    IDENTITY_SELF_CONTROL("Вы не можете заблокировать собственный аккаунт"),
    IMAGE_UPLOADED("Изображение с идентификатором %s успешно загружено"),
    IMAGE_UPLOADED_ASYNC("Изображение с идентификатором %s успешно загружено async"),
    IMAGE_CHANGED("Изображение с идентификатором %s успешно изменено"),
    IMAGE_CHANGED_ASYNC("Изображение с идентификатором %s успешно изменено async"),
    IMAGE_DELETED("Изображение с идентификатором %s успешно удалено"),
    IMAGE_DELETED_ASYNC("Изображение с идентификатором %s успешно удалено async"),
    IDENTITY_IMAGE_NOT_FOUND("Изображение пользователя не найдено"),
    IDENTITY_IMAGE_NOT_FOUND_ASYNC("Изображение пользователя не найдено async");

    private final String message;

    IdentityMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
