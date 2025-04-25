package org.burgas.ticketservice.router;

import org.burgas.ticketservice.dto.AirportRequest;
import org.burgas.ticketservice.service.AirportService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.servlet.function.RequestPredicates.GET;
import static org.springframework.web.servlet.function.RequestPredicates.POST;

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
                                        .body(airportService.findAll())
                )
                .andRoute(
                        GET("/airports/by-country"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(airportService.findByCountryId(request.param("countryId").orElse(null)))
                )
                .andRoute(
                        GET("/airports/by-city"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(airportService.findByCityId(request.param("cityId").orElse(null)))
                )
                .andRoute(
                        POST("/airports/create-update"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(airportService.createOrUpdate(request.body(AirportRequest.class)))
                );
    }
}
