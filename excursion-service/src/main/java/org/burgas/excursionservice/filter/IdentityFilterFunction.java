package org.burgas.excursionservice.filter;

import org.burgas.excursionservice.dto.IdentityResponse;
import org.burgas.excursionservice.exception.IdentityNotAuthenticatedException;
import org.burgas.excursionservice.exception.IdentityNotAuthorizedException;
import org.burgas.excursionservice.exception.IdentitySelfControlException;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.burgas.excursionservice.message.IdentityMessages.*;

@Component
public class IdentityFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    public @NotNull ServerResponse filter(@NotNull ServerRequest request, @NotNull HandlerFunction<ServerResponse> next) throws Exception {
        if (
                request.path().equals("/identities/by-id") || request.path().equals("/identities/by-id/async") ||
                request.path().equals("/identities/update") || request.path().equals("/identities/update/async") ||
                request.path().equals("/identities/delete") || request.path().equals("/identities/delete/async") ||
                request.path().equals("/identities/upload-image") || request.path().equals("/identities/upload-image/async") ||
                request.path().equals("/identities/change-image") || request.path().equals("/identities/change-image/async") ||
                request.path().equals("/identities/delete-image") || request.path().equals("/identities/delete-image/async") ||
                request.path().equals("/excursions/by-identity") || request.path().equals("/excursions/by-identity/sse") ||
                request.path().equals("/excursions/by-identity/async") ||
                request.path().equals("/excursions/add-by-identity") || request.path().equals("/excursions/add-by-identity/async") ||
                request.path().equals("/payments/make-identity-payment") || request.path().equals("/payments/make-identity-payment-id") ||
                request.path().equals("/payments/by-identity") || request.path().equals("/payments/by-identity/async")
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

        } else if (request.path().equals("/identities/control") || request.path().equals("/identities/control/async")) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String identityIdParam = request.param("identityId").orElse(null);

            if (authentication.isAuthenticated()) {
                Long identityId = Long.parseLong(identityIdParam == null || identityIdParam.isBlank() ? "0" : identityIdParam);
                IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();

                if (!identityResponse.getId().equals(identityId)) {
                    return next.handle(request);

                } else {
                    throw new IdentitySelfControlException(IDENTITY_SELF_CONTROL.getMessage());
                }

            } else {
                throw new IdentityNotAuthenticatedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
            }
        }
        return next.handle(request);
    }
}
