package org.burgas.hotelbackend.message;

public enum PaymentMessages {

    EARLY_PAYMENT_AND_RETURN_RESERVATION("Ранняя оплата и ранняя сдача брони прошла успешно"),
    PAYMENT_CANCELLED("Бронь номера была отменена"),
    PAYMENT_CLOSED_AND_PAYED("Платеж был оплачен и закрыт"),
    PAYMENT_NOT_FOUND("Запись о платеже не была найдена");

    private final String message;

    PaymentMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
