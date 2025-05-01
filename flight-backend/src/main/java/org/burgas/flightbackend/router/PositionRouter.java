package org.burgas.flightbackend.router;

import org.burgas.flightbackend.dto.PositionRequest;
import org.burgas.flightbackend.dto.PositionResponse;
import org.burgas.flightbackend.service.PositionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static java.net.URI.create;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.servlet.function.RequestPredicates.*;

@Configuration
public class PositionRouter {

    @Bean
    public RouterFunction<ServerResponse> positionRoutes(final PositionService positionService) {
        return RouterFunctions
                .route(
                        GET("/positions"), _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(positionService.findAll())
                )
                .andRoute(
                        GET("/positions/by-id"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(positionService.findById(request.param("positionId").orElse(null)))
                )
                .andRoute(
                        POST("/positions/create-update"), request -> {
                            PositionResponse positionResponse = positionService.createOrUpdate(request.body(PositionRequest.class));
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(create("/positions/by-id?positionId=" + positionResponse.getId()))
                                    .body(positionResponse);
                        }
                )
                .andRoute(
                        DELETE("/positions/delete"), request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(positionService.deleteById(request.param("positionId").orElse(null)))
                );
    }
}
