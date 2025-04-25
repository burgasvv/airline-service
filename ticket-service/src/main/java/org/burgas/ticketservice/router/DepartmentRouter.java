package org.burgas.ticketservice.router;

import org.burgas.ticketservice.dto.DepartmentRequest;
import org.burgas.ticketservice.dto.DepartmentResponse;
import org.burgas.ticketservice.service.DepartmentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static java.net.URI.create;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.servlet.function.RequestPredicates.*;

@Configuration
public class DepartmentRouter {

    @Bean
    public RouterFunction<ServerResponse> departmentRoutes(final DepartmentService departmentService) {
        return RouterFunctions
                .route(
                        GET("/departments"), _ ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(departmentService.findAll())
                )
                .andRoute(
                        GET("/departments/by-id"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(departmentService.findById(request.param("departmentId").orElse(null)))
                )
                .andRoute(
                        POST("/departments/create-update"), request -> {
                            DepartmentResponse departmentResponse = departmentService.createOrUpdate(request.body(DepartmentRequest.class));
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(create("/departments/by-id?departmentId=" + departmentResponse.getId()))
                                    .body(departmentResponse);
                        }
                )
                .andRoute(
                        DELETE("/departments/delete"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                        .body(departmentService.deleteById(request.param("departmentId").orElse(null)))
                );
    }
}
