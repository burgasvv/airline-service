package org.burgas.hotelbackend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.burgas.hotelbackend.dto.IdentityResponse;
import org.burgas.hotelbackend.exception.IdentityNotAuthenticatedException;
import org.burgas.hotelbackend.exception.IdentityNotAuthorizedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.burgas.hotelbackend.message.IdentityMessages.IDENTITY_NOT_AUTHENTICATED;
import static org.burgas.hotelbackend.message.IdentityMessages.IDENTITY_NOT_AUTHORIZED;

@WebFilter(
        urlPatterns = {
                "/identities/by-id", "/identities/by-id/async",
                "/identities/update", "/identities/update/async"
        }
)
public class IdentityWebFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

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
    }
}
