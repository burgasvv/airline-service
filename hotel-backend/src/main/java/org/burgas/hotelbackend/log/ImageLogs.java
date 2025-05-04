package org.burgas.hotelbackend.log;

public enum ImageLogs {

    IMAGE_FOUND_BY_ID("Image was found by id: {}"),
    IMAGE_FOUND_BY_ID_ASYNC("Image was found by id async: {}");

    private final String logMessage;

    ImageLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
