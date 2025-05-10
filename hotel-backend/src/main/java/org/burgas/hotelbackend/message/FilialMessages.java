package org.burgas.hotelbackend.message;

public enum FilialMessages {

    FILIAL_IMAGE_UPLOADED("Изображение филиала с идентификатором %s успешно загружено"),
    FILIAL_IMAGE_CHANGED("Изображение филиала с идентификатором %s успешно изменено"),
    FILIAL_IMAGE_DELETED("Изображение филиала с идентификатором %s успешно удалено"),
    FILIAL_DELETED("Запись о филиале была успешно удалена"),
    FILIAL_NOT_FOUND("Запись о филиале не найдена");

    private final String message;

    FilialMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
