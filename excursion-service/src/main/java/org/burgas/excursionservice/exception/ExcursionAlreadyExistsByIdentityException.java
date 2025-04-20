package org.burgas.excursionservice.exception;

public class ExcursionAlreadyExistsByIdentityException extends RuntimeException {

    public ExcursionAlreadyExistsByIdentityException(String message) {
        super(message);
    }
}
