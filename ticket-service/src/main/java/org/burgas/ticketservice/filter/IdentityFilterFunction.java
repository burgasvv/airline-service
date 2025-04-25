package org.burgas.ticketservice.filter;

import org.burgas.ticketservice.dto.IdentityResponse;
import org.burgas.ticketservice.exception.IdentityNotAuthenticatedException;
import org.burgas.ticketservice.exception.IdentityNotAuthorizedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.burgas.ticketservice.message.AuthMessage.NOT_AUTHENTICATED;
import static org.burgas.ticketservice.message.AuthMessage.NOT_AUTHORIZED;

@Component
public class IdentityFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    public @NotNull ServerResponse filter(@NotNull ServerRequest request, @NotNull HandlerFunction<ServerResponse> next) throws Exception {

        if (
                request.path().equals("/identities/by-id") || request.path().equals("/identities/update") ||
                request.path().equals("/identities/change-password") || request.path().equals("/identities/set-password") ||
                request.path().equals("/identities/upload-image") || request.path().equals("/identities/change-image") ||
                request.path().equals("/identities/delete-image")
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

        } else if (request.path().equals("/identities/by-username")) {

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
