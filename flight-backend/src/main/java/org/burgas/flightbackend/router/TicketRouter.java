package org.burgas.flightbackend.router;

import org.burgas.flightbackend.dto.TicketRequest;
import org.burgas.flightbackend.dto.TicketResponse;
import org.burgas.flightbackend.service.TicketService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class TicketRouter {

    @Bean
    public RouterFunction<ServerResponse> ticketRoutes(final TicketService ticketService) {
        return route()
                .GET(
                        "/flight-service/tickets", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(ticketService.findAll())
                )
                .GET(
                        "/flight-service/tickets/by-flight", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(ticketService.findAllByFlightId(request.param("flightId").orElseThrow()))
                )
                .GET(
                        "/flight-service/tickets/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(ticketService.findById(request.param("ticketId").orElseThrow()))
                )
                .GET(
                        "/flight-service/tickets/create-update", request -> {
                            TicketResponse ticketResponse = ticketService.createOrUpdate(request.body(TicketRequest.class));
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(URI.create("/flight-service/tickets/by-id?ticketId=" + ticketResponse.getId()))
                                    .body(ticketResponse);
                        }
                )
                .build();
    }
}
