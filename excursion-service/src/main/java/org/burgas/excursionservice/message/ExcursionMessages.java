package org.burgas.excursionservice.message;

public enum ExcursionMessages {

    EXCURSION_NOT_FOUND("Указанная вами экскурсия не найдена"),
    EXCURSION_DELETED("Указанная вами экскурсия была удалена"),
    IMAGE_UPLOADED("Изображение с идентификатором %s было успешно загружено"),
    IMAGE_UPLOADED_ASYNC("Изображение с идентификатором %s было успешно загружено async"),
    IMAGE_CHANGED("Изображение с идентификатором %s было успешно изменено"),
    IMAGE_CHANGED_ASYNC("Изображение с идентификатором %s было успешно изменено async"),
    IMAGE_DELETED("Изображение с идентификатором %s было успешно удалено"),
    IMAGE_DELETED_ASYNC("Изображение с идентификатором %s было успешно удалено async"),
    EXCURSION_IMAGE_NOT_FOUND("Изображение экскурсии не найдено");

    private final String message;

    ExcursionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
