package org.burgas.flightbackend.router;

import org.burgas.flightbackend.dto.PlaneRequest;
import org.burgas.flightbackend.dto.PlaneResponse;
import org.burgas.flightbackend.service.PlaneService;
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
                        "/planes/async", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(planeService.findAllAsync().get())
                )
                .GET(
                        "/planes/pages/{page}", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        planeService.findAllPages(
                                                Integer.valueOf(request.pathVariable("page")),
                                                Integer.valueOf(request.param("size").orElseThrow())
                                        )
                                                .getContent()
                                )
                )
                .GET(
                        "/planes/by-free", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(planeService.findAllByFree(request.param("free").orElseThrow()))
                )
                .GET(
                        "/planes/by-free/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(planeService.findAllByFreeAsync(request.param("free").orElseThrow()).get())
                )
                .GET(
                        "/planes/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(planeService.findById(request.param("planeId").orElseThrow()))
                )
                .GET(
                        "/planes/by-id/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(planeService.findByIdAsync(request.param("planeId").orElseThrow()).get())
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
                .POST(
                        "/planes/create-update/async", request -> {
                            PlaneResponse planeResponse = planeService.createOrUpdateAsync(request.body(PlaneRequest.class)).get();
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(URI.create("/planes/by-id/async?planeId=" + planeResponse.getId()))
                                    .body(planeResponse);
                        }
                )
                .DELETE(
                        "/planes/delete", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(planeService.deleteById(request.param("planeId").orElseThrow()))
                )
                .DELETE(
                        "/planes/delete/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(planeService.deleteByIdAsync(request.param("planeId").orElseThrow()).get())
                )
                .build();
    }
}
