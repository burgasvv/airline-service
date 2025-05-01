package org.burgas.excursionbackend.exception;

public class ExcursionAlreadyExistsByIdentityException extends RuntimeException {

    public ExcursionAlreadyExistsByIdentityException(String message) {
        super(message);
    }
}
