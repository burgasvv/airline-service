package org.burgas.hotelbackend.message;

public enum RoomMessages {

    WRONG_RENTED_DATE("Данный номер уже занят в этот срок"),
    ROOM_ALREADY_RENTED("Данный номер уже занят"),
    ROOM_BY_NUMBER_AND_FILIAL_ALREADY_EXISTS("Данный номер в данном отеле уже существует"),
    ROOM_IMAGES_UPLOADED("Изображения номера успешно загружены"),
    ROOM_IMAGES_CHANGED("Изображения номера успешно изменены"),
    ROOM_IMAGES_DELETED("Изображения номера успешно удалены"),
    ROOM_DELETED("Номер в отеле был успешно удален"),
    ROOM_NOT_FOUND("Номер в отеле не был найден"),
    ROOM_NOT_CREATED_OR_UPDATED("Номер в отеле не был создан или обновлен");

    private final String message;

    RoomMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
