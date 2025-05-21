package org.burgas.hotelbackend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.burgas.hotelbackend.dto.IdentityResponse;
import org.burgas.hotelbackend.exception.IdentityNotAuthenticatedException;
import org.burgas.hotelbackend.exception.IdentityNotAuthorizedException;
import org.burgas.hotelbackend.exception.WrongActionWithIdentityException;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.burgas.hotelbackend.message.IdentityMessages.*;

@WebFilter(
        urlPatterns = {
                "/hotel-service/identities/by-id", "/hotel-service/identities/by-id/async",
                "/hotel-service/identities/update", "/hotel-service/identities/update/async",
                "/hotel-service/identities/upload-image", "/hotel-service/identities/upload-image/async",
                "/hotel-service/identities/change-image", "/hotel-service/identities/change-image/async",
                "/hotel-service/identities/delete-image", "/hotel-service/identities/delete-image/async",
                "/hotel-service/identities/activate-deactivate", "/hotel-service/identities/activate-deactivate/async"
        }
)
public class IdentityWebFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        if (
                request.getRequestURI().equals("/hotel-service/identities/by-id") || request.getRequestURI().equals("/hotel-service/identities/by-id/async") ||
                request.getRequestURI().equals("/hotel-service/identities/update") || request.getRequestURI().equals("/hotel-service/identities/update/async") ||
                request.getRequestURI().equals("/hotel-service/identities/upload-image") || request.getRequestURI().equals("/hotel-service/identities/upload-image/async") ||
                request.getRequestURI().equals("/hotel-service/identities/change-image") || request.getRequestURI().equals("/hotel-service/identities/change-image/async") ||
                request.getRequestURI().equals("/hotel-service/identities/delete-image") || request.getRequestURI().equals("/hotel-service/identities/delete-image/async")
        ) {

            String identityIdParam = request.getParameter("identityId");
            Authentication authentication = (Authentication) request.getUserPrincipal();

            if (authentication.isAuthenticated()) {
                Long identityId = Long.parseLong(identityIdParam == null || identityIdParam.isBlank() ? "0" : identityIdParam);
                IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();

                if (identityResponse.getId().equals(identityId)) {
                    filterChain.doFilter(request, response);

                } else {
                    throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
                }

            } else {
                throw new IdentityNotAuthenticatedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
            }

        } else if (
                request.getRequestURI().equals("/hotel-service/identities/activate-deactivate") ||
                request.getRequestURI().equals("/hotel-service/identities/activate-deactivate/async")
        ) {

            String identityIdParam = request.getParameter("identityId");
            Authentication authentication = (Authentication) request.getUserPrincipal();

            if (authentication.isAuthenticated()) {
                Long identityId = Long.parseLong(identityIdParam == null || identityIdParam.isBlank() ? "0" : identityIdParam);
                IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();

                if (!identityResponse.getId().equals(identityId)) {
                    filterChain.doFilter(request, response);

                } else {
                    throw new WrongActionWithIdentityException(WRONG_ACTION_WITH_THIS_IDENTITY.getMessage());
                }

            } else {
                throw new IdentityNotAuthenticatedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
            }
        }
    }
}
