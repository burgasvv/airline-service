package org.burgas.hotelbackend.handler;

import org.burgas.hotelbackend.exception.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorityNotCreatedException.class)
    public ResponseEntity<String> handleAuthorityNotCreatedException(final AuthorityNotCreatedException exception) {
        return ResponseEntity
                .status(NOT_ACCEPTABLE)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(exception.getMessage());
    }

    @ExceptionHandler(AuthorityNotFoundException.class)
    public ResponseEntity<String> handleAuthorityNotFoundException(final AuthorityNotFoundException exception) {
        return ResponseEntity
                .status(NOT_FOUND)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(exception.getMessage());
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<String> handleImageNotFoundException(final ImageNotFoundException exception) {
        return ResponseEntity
                .status(NOT_FOUND)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(exception.getMessage());
    }

    @ExceptionHandler(PhoneNotMatchesException.class)
    public ResponseEntity<String> handlePhoneNotMatchesException(final PhoneNotMatchesException exception) {
        return ResponseEntity
                .status(NOT_ACCEPTABLE)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(exception.getMessage());
    }

    @ExceptionHandler(IdentityNotAuthenticatedException.class)
    public ResponseEntity<String> handleIdentityNotAuthenticatedException(final IdentityNotAuthenticatedException exception) {
        return ResponseEntity
                .status(NOT_ACCEPTABLE)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(exception.getMessage());
    }

    @ExceptionHandler(IdentityNotAuthorizedException.class)
    public ResponseEntity<String> handleIdentityNotAuthorizedException(final IdentityNotAuthorizedException exception) {
        return ResponseEntity
                .status(NOT_ACCEPTABLE)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(exception.getMessage());
    }

    @ExceptionHandler(IdentityNotFoundException.class)
    public ResponseEntity<String> handleIdentityNotFoundException(final IdentityNotFoundException exception) {
        return ResponseEntity
                .status(NOT_FOUND)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(exception.getMessage());
    }
}
