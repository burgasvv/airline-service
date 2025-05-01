package org.burgas.excursionservice.message;

public enum ImageMessages {

    IMAGE_NOT_FOUND("Выбранное вами изображение не найдено"),
    IMAGE_DELETED("Выбранное вами изображение успешно удалено");

    private final String message;

    ImageMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
