package org.burgas.flightbackend.message;

public enum ImageMessages {

    WRONG_FILE_FORMAT("Неверный формат файла загрузки"),
    FILE_IS_EMPTY("Загружаемый файл пуст"),
    IDENTITY_IMAGE_UPLOADED("Изображение пользователя загружено"),
    IDENTITY_IMAGE_CHANGED("Изображение пользователя изменено"),
    IMAGE_DELETED("Изображение пользователя удалено"),
    IMAGE_NOT_FOUND("Изображение не найдено");

    private final String message;

    ImageMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
