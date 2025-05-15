package org.burgas.hotelbackend.log;

public enum EmployeeLogs {

    EMPLOYEE_FOUND_BEFORE_UPLOAD_IMAGE("Employee was found before upload image: {}"),
    EMPLOYEE_FOUND_BEFORE_UPLOAD_IMAGE_ASYNC("Employee was found before upload image async: {}"),
    EMPLOYEE_FOUND_BEFORE_CHANGE_IMAGE("Employee was found before change image: {}"),
    EMPLOYEE_FOUND_BEFORE_CHANGE_IMAGE_ASYNC("Employee was found before change image async: {}"),
    EMPLOYEE_FOUND_BEFORE_DELETE_IMAGE("Employee was found before upload image: {}"),
    EMPLOYEE_FOUND_BEFORE_DELETE_IMAGE_ASYNC("Employee was found before upload image async: {}"),
    EMPLOYEE_FOUND_ALL("Employee was found in list of all: {}"),
    EMPLOYEE_FOUND_ALL_ASYNC("Employee was found in list of all async: {}"),
    EMPLOYEE_FOUND_BY_ID("Employee was found by id: {}"),
    EMPLOYEE_FOUND_BY_ID_ASYNC("Employee was found by id async: {}"),
    EMPLOYEE_CREATED_OR_UPDATED("Employee was created or updated: {}"),
    EMPLOYEE_CREATED_OR_UPDATED_ASYNC("Employee was created or updated async: {}"),
    EMPLOYEE_FOUND_BEFORE_DELETE("Employee was found before delete: {}"),
    EMPLOYEE_FOUND_BEFORE_DELETE_ASYNC("Employee was found before delete async: {}");

    private final String logMessage;

    EmployeeLogs(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
