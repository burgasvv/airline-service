package org.burgas.ticketservice.message;

public enum ImageMessage {

    WRONG_FILE_FORMAT("Неверный формат файла загрузки"),
    FILE_IS_EMPTY("Загружаемый файл пуст"),
    IDENTITY_IMAGE_UPLOADED("Изображение пользователя загружено"),
    IDENTITY_IMAGE_CHANGED("Изображение пользователя изменено"),
    IMAGE_DELETED("Изображение пользователя удалено"),
    IMAGE_NOT_FOUND("Изображение не найдено");

    private final String message;

    ImageMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
