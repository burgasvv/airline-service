package org.burgas.flightbackend.exception;

public class IdentityNotAuthorizedException extends RuntimeException {

  public IdentityNotAuthorizedException(String message) {
    super(message);
  }
}
