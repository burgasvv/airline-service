package org.burgas.flightbackend.router;

import org.burgas.flightbackend.dto.AirportRequest;
import org.burgas.flightbackend.service.AirportService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.servlet.function.RequestPredicates.*;

@Configuration
public class AirportRouter {

    @Bean
    public RouterFunction<ServerResponse> airportRoutes(final AirportService airportService) {
        return RouterFunctions
                .route(
                        GET("/flight-service/airports"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(airportService.findAll())
                )
                .andRoute(
                        GET("/flight-service/airports/async"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(airportService.findAllAsync().get())
                )
                .andRoute(
                        GET("/flight-service/airports/pages/{page}"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        airportService.findAllPages(
                                                Integer.valueOf(request.pathVariable("page")),
                                                Integer.valueOf(request.param("size").orElseThrow())
                                        )
                                                .getContent()
                                )
                )
                .andRoute(
                        GET("/flight-service/airports/by-country"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(airportService.findByCountryId(request.param("countryId").orElseThrow()))
                )
                .andRoute(
                        GET("/flight-service/airports/by-country/async"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        airportService.findByCountryIdAsync(request.param("countryId")
                                                .orElseThrow()).get()
                                )
                )
                .andRoute(
                        GET("/flight-service/airports/by-city"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(airportService.findByCityId(request.param("cityId").orElseThrow()))
                )
                .andRoute(
                        GET("/flight-service/airports/by-city/async"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        airportService.findByCityIdAsync(request.param("cityId")
                                                .orElseThrow()).get()
                                )
                )
                .andRoute(
                        POST("/flight-service/airports/create-update"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(airportService.createOrUpdate(request.body(AirportRequest.class)))
                )
                .andRoute(
                        POST("/flight-service/airports/create-update/async"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(airportService.createOrUpdateAsync(request.body(AirportRequest.class)).get())
                )
                .andRoute(
                        DELETE("/flight-service/airports/delete"), request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(airportService.deleteById(request.param("airportId").orElseThrow()))
                )
                .andRoute(
                        DELETE("/flight-service/airports/delete/async"), request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(airportService.deleteByIdAsync(request.param("airportId").orElseThrow()).get())
                );
    }
}
