package org.burgas.ticketservice.router;

import org.burgas.ticketservice.dto.EmployeeRequest;
import org.burgas.ticketservice.dto.EmployeeResponse;
import org.burgas.ticketservice.service.EmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static java.net.URI.create;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Configuration
public class EmployeeRouter {

    @Bean
    public RouterFunction<ServerResponse> employeeRoutes(final EmployeeService employeeService) {
        return RouterFunctions
                .route()
                .GET(
                        "/employees", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(employeeService.finaAll(), EmployeeResponse.class)
                )
                .GET(
                        "/employees/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(employeeService.findById(request.queryParam("employeeId").orElse(null)), EmployeeResponse.class)
                )
                .POST(
                        "/employees/create", request -> employeeService
                                .createEmployee(request.bodyToMono(EmployeeRequest.class), request.queryParam("token").orElse(null))
                                .flatMap(
                                        employeeResponse -> ServerResponse
                                                .status(FOUND)
                                                .contentType(APPLICATION_JSON)
                                                .location(create("/employees/by-id?employeeId=" + employeeResponse.getId()))
                                                .body(fromValue(employeeResponse))
                                )
                )
                .build();
    }
}
