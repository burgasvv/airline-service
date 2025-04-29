package org.burgas.ticketservice.router;

import org.burgas.ticketservice.dto.AddressRequest;
import org.burgas.ticketservice.service.AddressService;
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
public class AddressRouter {

    @Bean
    public RouterFunction<ServerResponse> addressRoutes(final AddressService addressService) {
        return RouterFunctions
                .route(
                        GET("/addresses"), _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(addressService.findAll())
                )
                .andRoute(
                        GET("/addresses/async"), _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(addressService.findAllAsync().get())
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
                );
    }
}
