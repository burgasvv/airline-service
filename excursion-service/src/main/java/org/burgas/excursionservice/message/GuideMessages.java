package org.burgas.excursionservice.message;

public enum GuideMessages {

    GUIDE_NOT_FOUND("Указанный вами гид не найден"),
    GUIDE_IMAGE_NOT_FOUND("Изображение гида не найдено"),
    GUIDE_DELETED("Гид с идентификатором %s успешно удален"),
    IMAGE_UPLOADED("Изображение с идентификатором %s успешно загружено"),
    IMAGE_UPLOADED_ASYNC("Изображение с идентификатором %s успешно загружено async"),
    IMAGE_CHANGED("Изображение с идентификатором %s успешно изменено"),
    IMAGE_CHANGED_ASYNC("Изображение с идентификатором %s успешно загружено async"),
    IMAGE_DELETED("Изображение с идентификатором %s успешно удалено"),
    IMAGE_DELETED_ASYNC("Изображение с идентификатором %s успешно удалено async");

    private final String message;

    GuideMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
