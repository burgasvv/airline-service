package org.burgas.hotelbackend.log;

public enum PaymentLogs {

    PAYMENT_FOUND_ALL("Payment was found in list of all: {}"),
    PAYMENT_FOUND_ALL_ASYNC("Payment was found in list of all async: {}");

    private final String log;

    PaymentLogs(String log) {
        this.log = log;
    }

    public String getLog() {
        return log;
    }
}
