package org.burgas.excursionbackend.log;

public enum ImageLogs {

    IMAGE_WAS_CREATED("Image was created"),
    IMAGE_WAS_CREATED_ASYNC("Image was created async");

    private final String logMessage;

    ImageLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
