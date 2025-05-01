package org.burgas.excursionbackend.message;

public enum SightMessages {

    SIGHT_SAVE_OR_TRANSFORM("Невозможно сохранить или преобразовать объект достопримечательности"),
    SIGHT_NOT_FOUND("Выбранная достопримечательность не найдена"),
    SIGHT_DELETED("Выбранная достопримечательность успешно удалена"),
    SIGHT_DELETED_ASYNC("Выбранная достопримечательность успешно удалена async"),
    IMAGE_UPLOADED("Изображение с идентификатором %s успешно загружено"),
    IMAGE_UPLOADED_ASYNC("Изображение с идентификатором %s успешно загружено async"),
    IMAGE_CHANGED("Изображение с идентификатором %s успешно изменено"),
    IMAGE_CHANGED_ASYNC("Изображение с идентификатором %s успешно изменено async"),
    IMAGE_DELETED("Изображение с идентификатором %s успешно удалено"),
    IMAGE_DELETED_ASYNC("Изображение с идентификатором %s успешно удалено async"),
    SIGHT_IMAGE_NOT_FOUND("Изображение достопримечательности не найдено");

    private final String message;

    SightMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
