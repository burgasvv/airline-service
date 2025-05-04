package org.burgas.hotelbackend.message;

public enum ImageMessages {

    MULTIPART_FILE_EMPTY("Multipart file is empty"),
    IMAGE_DELETED("Изображение успешно удалено"),
    IMAGE_NOT_FOUND("Изображение не было найдено");

    private final String message;

    ImageMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
