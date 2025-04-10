package org.burgas.ticketservice.router;

import org.burgas.ticketservice.dto.PositionRequest;
import org.burgas.ticketservice.dto.PositionResponse;
import org.burgas.ticketservice.service.PositionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static java.net.URI.create;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class PositionRouter {

    @Bean
    public RouterFunction<ServerResponse> positionRoutes(final PositionService positionService) {
        return RouterFunctions
                .route(
                        GET("/positions"), _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(positionService.findAll(), PositionResponse.class)
                )
                .andRoute(
                        GET("/positions/by-id"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(positionService.findById(request.queryParam("positionId").orElse(null)), PositionResponse.class)
                )
                .andRoute(
                        POST("/positions/create-update"), request -> positionService
                                .createOrUpdate(request.bodyToMono(PositionRequest.class))
                                .flatMap(
                                        positionResponse -> ServerResponse
                                                .status(FOUND)
                                                .contentType(APPLICATION_JSON)
                                                .location(create("/positions/by-id?positionId=" + positionResponse.getId()))
                                                .body(fromValue(positionResponse))
                                )
                )
                .andRoute(
                        DELETE("/positions/delete"), request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(positionService.deleteById(request.queryParam("positionId").orElse(null)), String.class)
                );
    }
}
