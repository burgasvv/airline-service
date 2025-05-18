package org.burgas.hotelbackend.message;

public enum ClientMessages {

    ROOM_WAS_RENTED_PAYMENT_CREATED("Номер в отеле был арендован платеж был создан"),
    ROOM_WAS_RENTED_PAYMENT_UPDATED("Номер в отеле был арендован платеж был обновлен"),
    CLIENT_NOT_FOUND("Запись клиента не была найдена"),
    CLIENT_NOT_CREATED_OR_UPDATED("Запись клиента не была создана или обновлена");

    private final String message;

    ClientMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
