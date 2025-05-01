package org.burgas.flightbackend.router;

import org.burgas.flightbackend.dto.FilialRequest;
import org.burgas.flightbackend.service.FilialService;
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
                        GET("/filials/async"), _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(filialService.findAllAsync().get())
                )
                .andRoute(
                        GET("/filials/pages/{page}"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        filialService.findAllPages(
                                                Integer.valueOf(request.pathVariable("page")),
                                                Integer.valueOf(request.param("size").orElseThrow())
                                        )
                                                .getContent()
                                )
                )
                .andRoute(
                        GET("/filials/by-country"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(filialService.findByCountryId(request.param("countryId").orElseThrow()))
                )
                .andRoute(
                        GET("/filials/by-country"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(filialService.findByCountryIdAsync(request.param("countryId").orElseThrow()).get())
                )
                .andRoute(
                        GET("/filials/by-city"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(filialService.findByCityId(request.param("cityId").orElseThrow()))
                )
                .andRoute(
                        GET("/filials/by-city/async"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(filialService.findByCityIdAsync(request.param("cityId").orElseThrow()).get())
                )
                .andRoute(
                        POST("/filials/create-update"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(filialService.createOrUpdate(request.body(FilialRequest.class)))
                )
                .andRoute(
                        POST("/filials/create-update/async"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(filialService.createOrUpdateAsync(request.body(FilialRequest.class)).get())
                )
                .andRoute(
                        DELETE("/filials/delete"), request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(filialService.deleteById(request.param("filialId").orElseThrow()))
                )
                .andRoute(
                        DELETE("/filials/delete/async"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(filialService.deleteByIdAsync(request.param("filialId").orElseThrow()).get())
                );
    }
}
