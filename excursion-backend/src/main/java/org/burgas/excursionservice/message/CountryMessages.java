package org.burgas.excursionservice.message;

public enum CountryMessages {

    COUNTRY_DELETED("Страна была успешно удалена"),
    COUNTRY_NOT_FOUND("Страна не найдена"),
    IMAGE_UPLOADED("Изображение с идентификатором %s успешно загружено"),
    IMAGE_UPLOADED_ASYNC("Изображение с идентификатором %s успешно загружено async"),
    COUNTRY_IMAGE_NOT_FOUND("Изображение страны не найдено"),
    IMAGE_CHANGED("Изображение с идентификатором %s успешно изменено"),
    IMAGE_CHANGED_ASYNC("Изображение с идентификатором %s успешно изменено async"),
    IMAGE_DELETED("Изображение с идентификатором %s успешно удалено"),
    IMAGE_DELETED_ASYNC("Изображение с идентификатором %s успешно удалено async");

    private final String message;

    CountryMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
