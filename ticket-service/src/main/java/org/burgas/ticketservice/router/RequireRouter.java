package org.burgas.ticketservice.router;

import org.burgas.ticketservice.dto.RequireRequest;
import org.burgas.ticketservice.dto.RequireResponse;
import org.burgas.ticketservice.filter.RequireFilterFunction;
import org.burgas.ticketservice.service.RequireService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration
public class RequireRouter {

    @Bean
    public RouterFunction<ServerResponse> requireRoutes(final RequireService requireService) {
        return RouterFunctions
                .route()
                .filter(new RequireFilterFunction())
                .GET(
                        "/requires/by-closed", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(requireService.findAllByClosed(request.queryParam("closed").orElse(null)), RequireResponse.class)
                )
                .GET(
                        "/requires/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(requireService.findById(request.queryParam("requireId").orElse(null)), RequireResponse.class)
                )
                .POST(
                        "/requires/create-update", request -> request.bodyToMono(RequireRequest.class)
                                .flatMap(
                                        requireRequest -> {
                                            requireRequest.setUserId(Long.valueOf(
                                                    requireNonNull(request.queryParam("userId").orElse(null)))
                                            );
                                            return ServerResponse
                                                    .status(OK)
                                                    .contentType(APPLICATION_JSON)
                                                    .body(requireService.createOrUpdate(Mono.fromCallable(() -> requireRequest)), RequireResponse.class);
                                        }
                                )
                )
                .build();
    }
}
