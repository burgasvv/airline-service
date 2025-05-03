package org.burgas.hotelbackend.message;

public enum ImageMessages {

    IMAGE_NOT_FOUND("Изображение не было найдено");

    private final String message;

    ImageMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
