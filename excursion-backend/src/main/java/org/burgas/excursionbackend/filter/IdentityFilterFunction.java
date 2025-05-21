package org.burgas.excursionbackend.filter;

import org.burgas.excursionbackend.dto.IdentityResponse;
import org.burgas.excursionbackend.exception.IdentityNotAuthenticatedException;
import org.burgas.excursionbackend.exception.IdentityNotAuthorizedException;
import org.burgas.excursionbackend.exception.IdentitySelfControlException;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.burgas.excursionbackend.message.IdentityMessages.*;

@Component
public class IdentityFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    public @NotNull ServerResponse filter(@NotNull ServerRequest request, @NotNull HandlerFunction<ServerResponse> next) throws Exception {
        if (
                request.path().equals("/excursion-service/identities/by-id") || request.path().equals("/excursion-service/identities/by-id/async") ||
                request.path().equals("/excursion-service/identities/update") || request.path().equals("/excursion-service/identities/update/async") ||
                request.path().equals("/excursion-service/identities/delete") || request.path().equals("/excursion-service/identities/delete/async") ||
                request.path().equals("/excursion-service/identities/upload-image") || request.path().equals("/excursion-service/identities/upload-image/async") ||
                request.path().equals("/excursion-service/identities/change-image") || request.path().equals("/excursion-service/identities/change-image/async") ||
                request.path().equals("/excursion-service/identities/delete-image") || request.path().equals("/excursion-service/identities/delete-image/async") ||
                request.path().equals("/excursion-service/excursions/by-identity") || request.path().equals("/excursion-service/excursions/by-identity/sse") ||
                request.path().equals("/excursion-service/excursions/by-identity/async") ||
                request.path().equals("/excursion-service/excursions/add-by-identity") || request.path().equals("/excursion-service/excursions/add-by-identity/async") ||
                request.path().equals("/excursion-service/payments/make-identity-payment") || request.path().equals("/excursion-service/payments/make-identity-payment-id") ||
                request.path().equals("/excursion-service/payments/by-identity") || request.path().equals("/excursion-service/payments/by-identity/async")
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

        } else if (request.path().equals("/excursion-service/identities/control") || request.path().equals("/excursion-service/identities/control/async")) {

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
