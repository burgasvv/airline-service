package org.burgas.flightbackend.router;

import org.burgas.flightbackend.dto.OrderedTicketRequest;
import org.burgas.flightbackend.dto.OrderedTicketResponse;
import org.burgas.flightbackend.filter.IdentityFilterFunction;
import org.burgas.flightbackend.service.OrderedTicketService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class OrderedTicketRouter {

    @Bean
    public RouterFunction<ServerResponse> orderTicketRoutes(final OrderedTicketService orderedTicketService) {
        return route()
                .filter(new IdentityFilterFunction())
                .GET(
                        "/flight-service/ordered-tickets", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(orderedTicketService.findAll())
                )
                .GET(
                        "/flight-service/ordered-tickets/by-identity", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(orderedTicketService.findAllByIdentityId(request.param("identityId").orElseThrow()))
                )
                .GET(
                        "/flight-service/ordered-tickets/in-session", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(orderedTicketService.findAllInSession(request.servletRequest()))
                )
                .GET(
                        "/flight-service/ordered-tickets/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(orderedTicketService.findById(request.param("orderedTicketId").orElseThrow()))
                )
                .POST(
                        "/flight-service/ordered-tickets/order-ticket-identity", request -> {
                            OrderedTicketRequest orderedTicketRequest = request.body(OrderedTicketRequest.class);
                            orderedTicketRequest.setIdentityId(Long.valueOf(request.param("identityId").orElseThrow()));
                            OrderedTicketResponse orderedTicketResponse = orderedTicketService.orderTicketByIdentity(orderedTicketRequest);
                            return ServerResponse
                                    .status(OK)
                                    .contentType(APPLICATION_JSON)
                                    .location(URI.create("/flight-service/ordered-tickets/by-id?orderedTicketId=" + orderedTicketResponse.getId()))
                                    .body(orderedTicketResponse);
                        }
                )
                .POST(
                        "/flight-service/ordered-tickets/order-ticket-session", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(orderedTicketService.orderTicketBySession(
                                        request.body(OrderedTicketRequest.class), request.servletRequest()
                                ))
                )
                .DELETE(
                        "/flight-service/ordered-tickets/cancel-ordered-ticket", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(orderedTicketService.cancelOrderedTicket(request.param("orderedTicketId").orElseThrow()))
                )
                .build();
    }
}
