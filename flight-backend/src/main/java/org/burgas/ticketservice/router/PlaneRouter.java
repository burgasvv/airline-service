package org.burgas.ticketservice.router;

import org.burgas.ticketservice.dto.PlaneRequest;
import org.burgas.ticketservice.dto.PlaneResponse;
import org.burgas.ticketservice.service.PlaneService;
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
public class PlaneRouter {

    @Bean
    public RouterFunction<ServerResponse> planeRoutes(final PlaneService planeService) {
        return route()
                .GET(
                        "/planes", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(planeService.findAll())
                )
                .GET(
                        "/planes/by-free", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(planeService.findAllByFree(request.param("free").orElseThrow()))
                )
                .GET(
                        "/planes/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(planeService.findById(request.param("planeId").orElseThrow()))
                )
                .POST(
                        "/planes/create-update", request -> {
                            PlaneResponse planeResponse = planeService.createOrUpdate(request.body(PlaneRequest.class));
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(URI.create("/planes/by-id?planeId=" + planeResponse.getId()))
                                    .body(planeResponse);
                        }
                )
                .build();
    }
}
