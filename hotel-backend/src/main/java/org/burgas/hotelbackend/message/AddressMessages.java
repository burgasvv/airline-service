package org.burgas.hotelbackend.message;

public enum AddressMessages {

    ADDRESS_DELETED("Запись адреса была успешно удалена"),
    ADDRESS_DELETED_ASYNC("Запись адреса была успешно удалена async"),
    ADDRESS_NOT_FOUND("Запись адреса не была найдена"),
    ADDRESS_NOT_CREATED_OR_UPDATED("Запись адреса не была создана или обновлена");

    private final String message;

    AddressMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
