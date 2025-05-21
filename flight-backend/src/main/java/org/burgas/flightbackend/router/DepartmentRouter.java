package org.burgas.flightbackend.router;

import org.burgas.flightbackend.dto.DepartmentRequest;
import org.burgas.flightbackend.dto.DepartmentResponse;
import org.burgas.flightbackend.service.DepartmentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

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
                        GET("/flight-service/departments"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(departmentService.findAll())
                )
                .andRoute(
                        GET("/flight-service/departments/async"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(departmentService.findAllAsync())
                )
                .andRoute(
                        GET("/flight-service/departments/pages/{page}"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        departmentService.findAllPages(
                                                Integer.valueOf(request.pathVariable("page")),
                                                Integer.valueOf(request.param("size").orElseThrow())
                                        )
                                                .getContent()
                                )
                )
                .andRoute(
                        GET("/flight-service/departments/by-id"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(departmentService.findById(request.param("departmentId").orElseThrow()))
                )
                .andRoute(
                        GET("/flight-service/departments/by-id/async"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        departmentService.findByIdAsync(request.param("departmentId")
                                                .orElseThrow()).get()
                                )
                )
                .andRoute(
                        POST("/flight-service/departments/create-update"), request -> {
                            DepartmentResponse departmentResponse = departmentService.createOrUpdate(request.body(DepartmentRequest.class));
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(create("/flight-service/departments/by-id?departmentId=" + departmentResponse.getId()))
                                    .body(departmentResponse);
                        }
                )
                .andRoute(
                        POST("/flight-service/departments/create-update/async"), request -> {
                            DepartmentResponse departmentResponse = departmentService.createOrUpdateAsync(request.body(DepartmentRequest.class)).get();
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(URI.create("/flight-service/departments/by-id/async?departmentId=" + departmentResponse.getId()))
                                    .body(departmentResponse);
                        }
                )
                .andRoute(
                        DELETE("/flight-service/departments/delete"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                        .body(departmentService.deleteById(request.param("departmentId").orElseThrow()))
                )
                .andRoute(
                        DELETE("/flight-service/departments/delete/async"), request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        departmentService.deleteByIdAsync(request.param("departmentId")
                                                .orElseThrow()).get()
                                )
                );
    }
}
