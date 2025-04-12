package org.burgas.excursionservice.filter;

import org.burgas.excursionservice.dto.IdentityResponse;
import org.burgas.excursionservice.exception.IdentityNotAuthenticatedException;
import org.burgas.excursionservice.exception.IdentityNotAuthorizedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.burgas.excursionservice.message.IdentityMessage.IDENTITY_NOT_AUTHENTICATED;
import static org.burgas.excursionservice.message.IdentityMessage.IDENTITY_NOT_AUTHORIZED;

@Component
public class IdentityFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    public @NotNull ServerResponse filter(@NotNull ServerRequest request, @NotNull HandlerFunction<ServerResponse> next) throws Exception {
        if (
                request.path().equals("/identities/by-id") || request.path().equals("/identities/by-id/async") ||
                request.path().equals("/identities/update") || request.path().equals("/identities/update/async") ||
                request.path().equals("/identities/delete") || request.path().equals("/identities/delete/async")
        ) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String identityIdParam = request.param("identityId").orElse(null);

            if (authentication.isAuthenticated()) {
                Long identityId = Long.parseLong(identityIdParam == null || identityIdParam.isBlank() ? "0" : identityIdParam);
                IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();

                if (identityResponse.getId().equals(identityId)) {
                    return next.handle(request);

                } else {
                    throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
                }

            } else {
                throw new IdentityNotAuthenticatedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
            }

        } else {
            return next.handle(request);
        }
    }
}
