package org.burgas.ticketservice.router;

import org.burgas.ticketservice.dto.FilialRequest;
import org.burgas.ticketservice.dto.FilialResponse;
import org.burgas.ticketservice.service.FilialService;
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
public class FilialRouter {

    @Bean
    public RouterFunction<ServerResponse> filialRoutes(final FilialService filialService) {
        return RouterFunctions
                .route(
                        GET("/filials"), _ ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(filialService.findAll(), FilialResponse.class)
                )
                .andRoute(
                        GET("/filials/by-country"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(filialService.findByCountryId(request.queryParam("countryId").orElse(null)),
                                                FilialResponse.class)
                )
                .andRoute(
                        GET("/filials/by-city"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(filialService.findByCityId(request.queryParam("cityId").orElse(null)),
                                                FilialResponse.class)
                )
                .andRoute(
                        POST("/filials/create-update"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(filialService.createOrUpdate(request.bodyToMono(FilialRequest.class)),
                                                FilialResponse.class)
                );
    }
}
