package org.burgas.ticketservice.router;

import org.burgas.ticketservice.dto.FlightRequest;
import org.burgas.ticketservice.dto.FlightResponse;
import org.burgas.ticketservice.service.FlightService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class FlightRouter {

    @Bean
    public RouterFunction<ServerResponse> flightRoutes(final FlightService flightService) {
        return route()
                .GET(
                        "/flights", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(flightService.findAll())
                )
                .GET(
                        "/flights/by-departure-city-arrival-city-by-departure-date", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        flightService.findAllByDepartureCityAndArrivalCityAndDepartureDate(
                                                request.param("departureCityId").orElseThrow(),
                                                request.param("arrivalCityId").orElseThrow(),
                                                request.param("departureDate").orElse(null)
                                        )
                                )
                )
                .GET(
                        "/flights/by-departure-city-by-arrival-city", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        flightService.findAllByDepartureCityAndArrivalCity(
                                                request.param("departureCityId").orElseThrow(),
                                                request.param("arrivalCityId").orElseThrow()
                                        )
                                )
                )
                .GET(
                        "/flights/by-departure-city-by-arrival-city-back", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        flightService.findAllByDepartureCityAndArrivalCityBack(request.param("flightId").orElseThrow())
                                )
                )
                .GET(
                        "/flights/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(flightService.findById(request.param("flightId").orElseThrow()))
                )
                .POST(
                        "/flights/create-update", request -> {
                            FlightResponse flightResponse = flightService.createOrUpdate(request.body(FlightRequest.class));
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(URI.create("/flights/by-id?flightId=" + flightResponse.getId()))
                                    .body(flightResponse);
                        }
                )
                .POST(
                        "/flights/add-employee", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        flightService.addEmployeeToFlight(
                                                request.param("flightId").orElseThrow(),
                                                request.param("employeeId").orElseThrow()
                                        )
                                )
                )
                .DELETE(
                        "/flights/remove-employee", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        flightService.removeEmployeeFromFlight(
                                                request.param("flightId").orElseThrow(),
                                                request.param("employeeId").orElseThrow()
                                        )
                                )
                )
                .PUT(
                        "/flights/start-flight", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(flightService.startFlight(request.param("flightId").orElseThrow()))
                )
                .PUT(
                        "/flights/complete-flight", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(flightService.completeFlight(request.param("flightId").orElseThrow()))
                )
                .build();
    }
}
