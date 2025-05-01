package org.burgas.flightbackend.log;

public enum ImageLogs {

    IMAGE_FOUND_BY_ID("Image was found by id: {}");

    private final String logMessage;

    ImageLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
