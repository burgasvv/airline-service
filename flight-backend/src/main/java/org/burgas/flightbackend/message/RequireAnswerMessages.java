package org.burgas.flightbackend.message;

public enum RequireAnswerMessages {

    REQUIRE_ANSWER_NOT_TRANSFORMED("Require answer not transformed");

    private final String logMessages;

    RequireAnswerMessages(String logMessages) {
        this.logMessages = logMessages;
    }

    public String getLogMessages() {
        return logMessages;
    }
}
