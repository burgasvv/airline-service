package org.burgas.ticketservice.exception;

public class IdentityNotAuthorizedException extends RuntimeException {

  public IdentityNotAuthorizedException(String message) {
    super(message);
  }
}
