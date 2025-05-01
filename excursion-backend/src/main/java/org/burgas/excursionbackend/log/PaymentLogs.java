package org.burgas.excursionbackend.log;

public enum PaymentLogs {

    PAYMENT_FOUND_BY_IDENTITY_ID("Payment was found by identity id: {}"),
    PAYMENT_FOUND_BY_ID("Payment was found by id: {}");

    private final String logMessage;

    PaymentLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
