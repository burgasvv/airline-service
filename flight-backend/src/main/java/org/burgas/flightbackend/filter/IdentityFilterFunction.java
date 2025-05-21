package org.burgas.flightbackend.filter;

import org.burgas.flightbackend.dto.IdentityResponse;
import org.burgas.flightbackend.exception.IdentityNotAuthenticatedException;
import org.burgas.flightbackend.exception.IdentityNotAuthorizedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.burgas.flightbackend.message.AuthMessages.NOT_AUTHENTICATED;
import static org.burgas.flightbackend.message.AuthMessages.NOT_AUTHORIZED;

@Component
public class IdentityFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    public @NotNull ServerResponse filter(@NotNull ServerRequest request, @NotNull HandlerFunction<ServerResponse> next) throws Exception {

        if (
                request.path().equals("/flight-service/identities/by-id") || request.path().equals("/flight-service/identities/update") ||
                request.path().equals("/flight-service/identities/change-password") || request.path().equals("/flight-service/identities/set-password") ||
                request.path().equals("/flight-service/identities/upload-image") || request.path().equals("/flight-service/identities/change-image") ||
                request.path().equals("/flight-service/identities/delete-image") ||
                request.path().equals("/flight-service/ordered-tickets/by-identity") || request.path().equals("/flight-service/ordered-tickets/order-ticket-identity")
        ) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            String identityIdParam = request.param("identityId").orElse(null);
            Authentication authentication = securityContext.getAuthentication();

            if (authentication.isAuthenticated()) {

                IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();
                Long identityId = Long.parseLong(identityIdParam == null || identityIdParam.isBlank() ? "0" : identityIdParam);

                if (identityResponse.getId().equals(identityId)) {
                    return next.handle(request);

                } else {
                    throw new IdentityNotAuthorizedException(NOT_AUTHORIZED.getMessage());
                }

            } else {
                throw new IdentityNotAuthenticatedException(NOT_AUTHENTICATED.getMessage());
            }

        } else if (request.path().equals("/flight-service/identities/by-username")) {

            SecurityContext securityContext = SecurityContextHolder.getContext();
            String username = request.param("username").orElse(null);
            Authentication authentication = securityContext.getAuthentication();

            if (authentication.isAuthenticated()) {
                IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();

                if (identityResponse.getUsernameNotDetails().equals(username)) {
                    return next.handle(request);

                } else {
                    throw new IdentityNotAuthorizedException(NOT_AUTHORIZED.getMessage());
                }

            } else {
                throw new IdentityNotAuthenticatedException(NOT_AUTHENTICATED.getMessage());
            }
        }

        return next.handle(request);
    }
}
