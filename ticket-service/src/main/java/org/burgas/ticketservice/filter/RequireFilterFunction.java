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

import static org.burgas.ticketservice.message.AuthMessages.NOT_AUTHENTICATED;
import static org.burgas.ticketservice.message.AuthMessages.NOT_AUTHORIZED;

@Component
public class RequireFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    public @NotNull ServerResponse filter(@NotNull ServerRequest request, @NotNull HandlerFunction<ServerResponse> next) throws Exception {
        if (request.path().equals("/requires/create-update")) {

            SecurityContext securityContext = SecurityContextHolder.getContext();
            String userIdParam = request.param("userId").orElse(null);
            Authentication authentication = securityContext.getAuthentication();
            Long userId = Long.parseLong(userIdParam == null || userIdParam.isBlank() ? "0" : userIdParam);

            if (authentication.isAuthenticated()) {
                IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();

                if (identityResponse.getId().equals(userId)) {
                    return next.handle(request);

                } else {
                    throw new IdentityNotAuthorizedException(NOT_AUTHORIZED.getMessage());
                }

            } else {
                throw new IdentityNotAuthenticatedException(NOT_AUTHENTICATED.getMessage());
            }

        } else {
            return next.handle(request);
        }
    }
}
