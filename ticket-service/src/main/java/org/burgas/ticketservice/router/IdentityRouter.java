package org.burgas.ticketservice.router;

import org.burgas.ticketservice.dto.IdentityRequest;
import org.burgas.ticketservice.dto.IdentityResponse;
import org.burgas.ticketservice.filter.IdentityFilterFunction;
import org.burgas.ticketservice.service.IdentityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@Configuration
public class IdentityRouter {

    @Bean
    public RouterFunction<ServerResponse> identityRoutes(final IdentityService identityService) {
        return RouterFunctions.route()
                .filter(new IdentityFilterFunction())
                .GET(
                        "/identities", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.findAll(), IdentityResponse.class)
                )
                .GET(
                        "/identities/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        identityService.findById(request.queryParam("identityId").orElse(null)),
                                        IdentityResponse.class
                                )
                )
                .GET(
                        "/identities/by-username", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        identityService.findByUsername(request.queryParam("username").orElse(null)),
                                        IdentityResponse.class
                                )
                )
                .POST(
                        "/identities/create", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        identityService.createOrUpdate(request.bodyToMono(IdentityRequest.class)),
                                        IdentityResponse.class
                                )
                )
                .POST(
                        "/identities/update", request -> request.bodyToMono(IdentityRequest.class)
                                .flatMap(
                                        identityRequest -> {
                                            identityRequest.setId(
                                                    Long.valueOf(requireNonNull(
                                                            request.queryParam("identityId").orElse(null))
                                                    )
                                            );
                                            return ServerResponse
                                                    .status(OK)
                                                    .contentType(APPLICATION_JSON)
                                                    .body(
                                                            identityService.createOrUpdate(Mono.fromCallable(() -> identityRequest)),
                                                            IdentityResponse.class
                                                    );
                                        }
                                )
                )
                .PUT(
                        "/identities/enable-disable", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        identityService.accountEnableOrDisable(
                                                request.queryParam("identityId").orElse(null),
                                                request.queryParam("enabled").orElse(null)
                                        ),
                                        String.class
                                )
                )
                .POST(
                        "/identities/change-password", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        identityService.changePassword(request.queryParam("identityId").orElse(null)),
                                        String.class
                                )
                )
                .PUT(
                        "/identities/set-password", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        identityService.setPassword(
                                                request.queryParam("identityId").orElse(null),
                                                request.queryParam("token").orElse(null),
                                                request.queryParam("password").orElse(null)
                                        ),
                                        IdentityResponse.class
                                )
                )
                .build();
    }
}
