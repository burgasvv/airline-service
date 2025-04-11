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
public class RequireAnswerFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    public @NotNull Mono<ServerResponse> filter(@NotNull ServerRequest request, @NotNull HandlerFunction<ServerResponse> next) {
        if (
                request.path().equals("/require-answers/send-answer-or-token") ||
                request.path().equals("/require-answers/by-admin")
        ) {
            return ReactiveSecurityContextHolder.getContext()
                    .flatMap(
                            securityContext -> {
                                String adminIdParam = request.queryParam("adminId").orElse(null);
                                Authentication authentication = securityContext.getAuthentication();

                                if (authentication.isAuthenticated()) {
                                    Long adminId = Long.parseLong(adminIdParam == null || adminIdParam.isBlank() ? "0" : adminIdParam);
                                    IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();

                                    if (identityResponse.getId().equals(adminId)) {
                                        return next.handle(request);

                                    } else {
                                        return Mono.error(new IdentityNotAuthorizedException(NOT_AUTHORIZED.getMessage()));
                                    }

                                } else {
                                    return Mono.error(new IdentityNotAuthenticatedException(NOT_AUTHENTICATED.getMessage()));
                                }
                            }
                    );
        } else if (request.path().equals("/require-answers/by-user")) {
            return ReactiveSecurityContextHolder.getContext()
                    .flatMap(
                            securityContext -> {
                                String userIdParam = request.queryParam("userId").orElse(null);
                                Authentication authentication = securityContext.getAuthentication();

                                if (authentication.isAuthenticated()) {
                                    Long userId = Long.parseLong(userIdParam == null || userIdParam.isBlank() ? "0" : userIdParam);
                                    IdentityResponse identityResponse = (IdentityResponse) authentication.getPrincipal();

                                    if (identityResponse.getId().equals(userId)) {
                                        return next.handle(request);

                                    } else {
                                        return Mono.error(new IdentityNotAuthorizedException(NOT_AUTHORIZED.getMessage()));
                                    }

                                } else {
                                    return Mono.error(new IdentityNotAuthenticatedException(NOT_AUTHENTICATED.getMessage()));
                                }
                            }
                    );

        } else {
            return next.handle(request);
        }
    }
}
