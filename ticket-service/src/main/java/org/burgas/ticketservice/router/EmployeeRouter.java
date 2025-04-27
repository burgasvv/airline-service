package org.burgas.ticketservice.router;

import org.burgas.ticketservice.dto.EmployeeRequest;
import org.burgas.ticketservice.dto.EmployeeResponse;
import org.burgas.ticketservice.service.EmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

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
                                .body(employeeService.finaAll())
                )
                .GET(
                        "/employees/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(employeeService.findById(request.param("employeeId").orElse(null)))
                )
                .POST(
                        "/employees/create", request -> {
                            EmployeeResponse employeeResponse = employeeService.createEmployee(
                                    request.body(EmployeeRequest.class), request.param("token").orElse(null)
                            );
                            return ServerResponse
                                    .status(OK)
                                    .contentType(APPLICATION_JSON)
                                    .body(employeeResponse);
                        }
                )
                .build();
    }
}
