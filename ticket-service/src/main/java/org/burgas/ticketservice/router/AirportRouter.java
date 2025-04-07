package org.burgas.ticketservice.router;

import org.burgas.ticketservice.dto.AirportRequest;
import org.burgas.ticketservice.dto.AirportResponse;
import org.burgas.ticketservice.service.AirportService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class AirportRouter {

    @Bean
    public RouterFunction<ServerResponse> airportRoutes(final AirportService airportService) {
        return RouterFunctions
                .route(
                        GET("/airports"), _ ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(airportService.findAll(), AirportResponse.class)
                )
                .andRoute(
                        GET("/airports/by-city"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(airportService.findByCityId(request.queryParam("cityId").orElse(null)),
                                                AirportResponse.class
                                        )
                )
                .andRoute(
                        POST("/airports/create-update"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(airportService.createOrUpdate(request.bodyToMono(AirportRequest.class)),
                                                AirportResponse.class
                                        )
                );
    }
}
