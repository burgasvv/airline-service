package org.burgas.ticketservice.message;

public enum RequireMessage {

    REQUIRE_CLOSED("Запрос на утверждение аккаунта сотрудника уже закрыт"),
    REQUIRE_NOT_FOUND("Зарос на утверждение аккаунта сотрудника не найден");

    private final String message;

    RequireMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
