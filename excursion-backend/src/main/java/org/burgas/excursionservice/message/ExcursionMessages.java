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
    EXCURSION_IMAGE_NOT_FOUND("Изображение экскурсии не найдено"),
    EXCURSION_ADDED_TO_SESSION("Экскурсия успешно добавлена в сессию"),
    EXCURSION_ADDED_TO_SESSION_ASYNC("Экскурсия успешно добавлена в сессию async"),
    EXCURSION_PASSED("Экскурсия была проведена"),
    EXCURSION_ALREADY_EXISTS_BY_IDENTITY("Данный пользователь уже добавил эту экскурсию"),
    EXCURSION_ADDED_BY_IDENTITY("Экскурсия была добавлена пользователем"),
    EXCURSION_ADDED_BY_IDENTITY_ASYNC("Экскурсия была добавлена пользователем async");

    private final String message;

    ExcursionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
