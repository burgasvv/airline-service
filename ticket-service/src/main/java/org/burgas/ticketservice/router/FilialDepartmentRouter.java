package org.burgas.ticketservice.router;

import org.burgas.ticketservice.dto.FilialDepartmentRequest;
import org.burgas.ticketservice.dto.FilialDepartmentResponse;
import org.burgas.ticketservice.service.FilialDepartmentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static java.net.URI.create;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class FilialDepartmentRouter {

    @Bean
    public RouterFunction<ServerResponse> filialDepartmentRoutes(final FilialDepartmentService filialDepartmentService) {
        return RouterFunctions
                .route(
                        GET("/filial-departments"), _ ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(filialDepartmentService.findAll(), FilialDepartmentResponse.class)
                )
                .andRoute(
                        GET("/filial-departments/by-filial-department"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(
                                                filialDepartmentService.findByFilialIdAndDepartmentId(
                                                        request.queryParam("filialId").orElse(null),
                                                        request.queryParam("departmentId").orElse(null)
                                                ),
                                                FilialDepartmentResponse.class
                                        )
                )
                .andRoute(
                        POST("/filial-departments/create-update"), request ->
                                filialDepartmentService.createOrUpdate(request.bodyToMono(FilialDepartmentRequest.class))
                                        .flatMap(
                                                filialDepartmentResponse ->
                                                        ServerResponse
                                                                .status(FOUND)
                                                                .contentType(APPLICATION_JSON)
                                                                .location(create("/filial-departments/by-filial-department?filialId="
                                                                                 + filialDepartmentResponse.getFilial().getId() + "&departmentId="
                                                                                 + filialDepartmentResponse.getDepartment().getId())
                                                                )
                                                                .body(BodyInserters.fromValue(filialDepartmentResponse))
                                        )
                )
                .andRoute(
                        DELETE("/filial-departments/delete"),request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                        .body(filialDepartmentService.deleteFilialDepartment(
                                                request.queryParam("filialId").orElse(null),
                                                request.queryParam("departmentId").orElse(null)),
                                                String.class
                                        )
                );
    }
}
