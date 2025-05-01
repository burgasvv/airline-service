package org.burgas.flightbackend.router;

import org.burgas.flightbackend.service.FlightSeatService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class FlightSeatRouter {

    @Bean
    public RouterFunction<ServerResponse> flightSeatRoutes(final FlightSeatService flightSeatService) {
        return route()
                .GET(
                        "/flight-seats/by-flight-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(flightSeatService.findAllByFlightId(request.param("flightId").orElseThrow()))
                )
                .build();
    }
}
