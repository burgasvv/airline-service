package org.burgas.flightbackend.router;

import org.burgas.flightbackend.dto.AddressRequest;
import org.burgas.flightbackend.service.AddressService;
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
public class AddressRouter {

    @Bean
    public RouterFunction<ServerResponse> addressRoutes(final AddressService addressService) {
        return RouterFunctions
                .route(
                        GET("/addresses"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(addressService.findAll())
                )
                .andRoute(
                        GET("/addresses/async"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(addressService.findAllAsync().get())
                )
                .andRoute(
                        GET("/addresses/pages/{page}"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        addressService.findAllPages(
                                                Integer.valueOf(request.pathVariable("page")),
                                                Integer.valueOf(request.param("size").orElseThrow())
                                        )
                                                .getContent()
                                )
                )
                .andRoute(
                        POST("/addresses/create-update-secured"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(addressService.createOrUpdateSecured(request.body(AddressRequest.class)))
                )
                .andRoute(
                        POST("/addresses/create-update-secured/async"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(addressService.createOrUpdateSecuresAsync(request.body(AddressRequest.class)))
                )
                .andRoute(
                        DELETE("/addresses/delete"), request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(addressService.deleteById(request.param("addressId").orElseThrow()))
                )
                .andRoute(
                        DELETE("/addresses/delete/async"), request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(addressService.deleteByIdAsync(request.param("addressId").orElseThrow()))
                );
    }
}
