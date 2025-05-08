package org.burgas.hotelbackend.message;

public enum HotelMessages {

    HOTEL_IMAGE_UPLOADED("Изображение отеля с идентификатором %s было успешно загружено"),
    HOTEL_IMAGE_CHANGED("Изображение отеля с идентификатором %s было успешно изменено"),
    HOTEL_IMAGE_DELETED("Изображение отеля с идентификатором %s было успешно удалено"),
    HOTEL_DELETED("Запись об отеле была удалена"),
    HOTEL_NOT_FOUND("Запись об отеле не найдена");

    private final String message;

    HotelMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
