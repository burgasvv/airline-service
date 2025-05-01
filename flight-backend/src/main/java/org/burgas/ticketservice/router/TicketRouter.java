package org.burgas.ticketservice.router;

import org.burgas.ticketservice.dto.TicketRequest;
import org.burgas.ticketservice.dto.TicketResponse;
import org.burgas.ticketservice.service.TicketService;
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
                        "/tickets", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(ticketService.findAll())
                )
                .GET(
                        "/tickets/by-flight", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(ticketService.findAllByFlightId(request.param("flightId").orElseThrow()))
                )
                .GET(
                        "/tickets/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(ticketService.findById(request.param("ticketId").orElseThrow()))
                )
                .GET(
                        "/tickets/create-update", request -> {
                            TicketResponse ticketResponse = ticketService.createOrUpdate(request.body(TicketRequest.class));
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(URI.create("/tickets/by-id?ticketId=" + ticketResponse.getId()))
                                    .body(ticketResponse);
                        }
                )
                .build();
    }
}
