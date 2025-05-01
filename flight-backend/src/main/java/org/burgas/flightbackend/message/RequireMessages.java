package org.burgas.flightbackend.message;

public enum RequireMessages {

    REQUIRE_NOT_CREATED("Require not created"),
    REQUIRE_CLOSED("Запрос на утверждение аккаунта сотрудника уже закрыт"),
    REQUIRE_DELETED("Запрос на утверждение аккаунта сотрудника удален"),
    REQUIRE_NOT_FOUND("Зарос на утверждение аккаунта сотрудника не найден");

    private final String message;

    RequireMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
