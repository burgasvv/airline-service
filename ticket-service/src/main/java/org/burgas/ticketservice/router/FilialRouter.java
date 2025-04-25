package org.burgas.ticketservice.router;

import org.burgas.ticketservice.dto.FilialRequest;
import org.burgas.ticketservice.service.FilialService;
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
public class FilialRouter {

    @Bean
    public RouterFunction<ServerResponse> filialRoutes(final FilialService filialService) {
        return RouterFunctions
                .route(
                        GET("/filials"), _ ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(filialService.findAll())
                )
                .andRoute(
                        GET("/filials/by-country"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(filialService.findByCountryId(request.param("countryId").orElse(null)))
                )
                .andRoute(
                        GET("/filials/by-city"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(filialService.findByCityId(request.param("cityId").orElse(null)))
                )
                .andRoute(
                        POST("/filials/create-update"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(filialService.createOrUpdate(request.body(FilialRequest.class)))
                );
    }
}
