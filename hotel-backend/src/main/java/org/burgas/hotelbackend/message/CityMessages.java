package org.burgas.hotelbackend.message;

public enum CityMessages {

    CITY_DELETED("Запись о городе успешно удалена"),
    CITY_NOT_FOUND("Запись о городе не была найдена"),
    CITY_NOT_CREATED_OR_UPDATED("Запись о городе не была создана или обновлена");

    private final String message;

    CityMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
