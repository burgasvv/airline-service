package org.burgas.ticketservice.filter;

import org.burgas.ticketservice.dto.IdentityResponse;
import org.burgas.ticketservice.exception.IdentityNotAuthenticatedException;
import org.burgas.ticketservice.exception.IdentityNotAuthorizedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.burgas.ticketservice.message.AuthMessage.NOT_AUTHENTICATED;
import static org.burgas.ticketservice.message.AuthMessage.NOT_AUTHORIZED;

@Component
public class IdentityFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    public @NotNull Mono<ServerResponse> filter(@NotNull ServerRequest request, @NotNull HandlerFunction<ServerResponse> next) {

        if (
                request.path().equals("/identities/by-id") || request.path().equals("/identities/update") ||
                request.path().equals("/identities/change-password") || request.path().equals("/identities/set-password")
        ) {
            return ReactiveSecurityContextHolder.getContext()
                    .flatMap(
                            securityContext -> {
                                String identityIdParam = request.queryParam("identityId").orElse(null);
                                Authentication authentication = securityContext.getAuthentication();

                                if (authentication.isAuthenticated()) {
                                    IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();
                                    Long identityId = Long.parseLong(identityIdParam == null || identityIdParam.isBlank() ? "0" : identityIdParam);

                                    if (identityResponse.getId().equals(identityId)) {
                                        return next.handle(request);

                                    } else {
                                        return Mono.error(new IdentityNotAuthorizedException(NOT_AUTHORIZED.getMessage()));
                                    }

                                } else {
                                    return Mono.error(new IdentityNotAuthenticatedException(NOT_AUTHENTICATED.getMessage()));
                                }
                            }
                    );

        } else if (request.path().equals("/identities/by-username")) {
            return ReactiveSecurityContextHolder.getContext()
                    .flatMap(
                            securityContext -> {
                                String username = request.queryParam("username").orElse(null);
                                Authentication authentication = securityContext.getAuthentication();

                                if (authentication.isAuthenticated()) {
                                    IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();

                                    if (identityResponse.getUsernameNotDetails().equals(username)) {
                                        return next.handle(request);

                                    } else {
                                        return Mono.error(new IdentityNotAuthorizedException(NOT_AUTHORIZED.getMessage()));
                                    }

                                } else {
                                    return Mono.error(new IdentityNotAuthenticatedException(NOT_AUTHENTICATED.getMessage()));
                                }
                            }
                    );

        }

        return next.handle(request);
    }
}
