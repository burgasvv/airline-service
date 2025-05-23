package org.burgas.flightbackend.router;

import org.burgas.flightbackend.dto.FilialDepartmentRequest;
import org.burgas.flightbackend.dto.FilialDepartmentResponse;
import org.burgas.flightbackend.service.FilialDepartmentService;
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
public class FilialDepartmentRouter {

    @Bean
    public RouterFunction<ServerResponse> filialDepartmentRoutes(final FilialDepartmentService filialDepartmentService) {
        return RouterFunctions
                .route(
                        GET("/flight-service/filial-departments"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(filialDepartmentService.findAll())
                )
                .andRoute(
                        GET("/flight-service/filial-departments/async"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(filialDepartmentService.findAllAsync())
                )
                .andRoute(
                        GET("/flight-service/filial-departments/pages/{page}"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        filialDepartmentService.findAllPages(
                                                Integer.valueOf(request.pathVariable("page")),
                                                Integer.valueOf(request.param("size").orElseThrow())
                                        )
                                                .getContent()
                                )
                )
                .andRoute(
                        GET("/flight-service/filial-departments/by-filial-department"), request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(APPLICATION_JSON)
                                        .body(
                                                filialDepartmentService.findByFilialIdAndDepartmentId(
                                                        request.param("filialId").orElseThrow(),
                                                        request.param("departmentId").orElseThrow()
                                                )
                                        )
                )
                .andRoute(
                        GET("/flight-service/filial-departments/by-filial-department/async"), request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        filialDepartmentService.findByFilialIdAndDepartmentIdAsync(
                                                request.param("filialId").orElseThrow(),
                                                request.param("departmentId").orElseThrow()
                                        )
                                )
                )
                .andRoute(
                        POST("/flight-service/filial-departments/create-update"), request -> {
                            FilialDepartmentResponse filialDepartmentResponse = filialDepartmentService
                                    .createOrUpdate(request.body(FilialDepartmentRequest.class));
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(create("/flight-service/filial-departments/by-filial-department?filialId="
                                                     + filialDepartmentResponse.getFilial().getId() + "&departmentId="
                                                     + filialDepartmentResponse.getDepartment().getId())
                                    )
                                    .body(filialDepartmentResponse);

                        }
                )
                .andRoute(
                        POST("/flight-service/filial-departments/create-update/async"), request -> {
                            FilialDepartmentResponse filialDepartmentResponse = filialDepartmentService
                                    .createOrUpdateAsync(request.body(FilialDepartmentRequest.class)).get();
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(create(
                                            "/flight-service/filial-departments/by-filial-department/async?filialId=" +
                                            filialDepartmentResponse.getFilial().getId() +
                                            "&departmentId=" + filialDepartmentResponse.getDepartment().getId()
                                    ))
                                    .body(filialDepartmentResponse);
                        }
                )
                .andRoute(
                        DELETE("/flight-service/filial-departments/delete"),request ->
                                ServerResponse
                                        .status(OK)
                                        .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                        .body(filialDepartmentService.deleteFilialDepartment(
                                                request.param("filialId").orElseThrow(),
                                                request.param("departmentId").orElseThrow())
                                        )
                )
                .andRoute(
                        DELETE("/flight-service/filial-departments/delete/async"), request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        filialDepartmentService.deleteFilialDepartmentAsync(
                                                request.param("filialId").orElseThrow(),
                                                request.param("departmentId").orElseThrow()
                                        )
                                )
                );
    }
}
