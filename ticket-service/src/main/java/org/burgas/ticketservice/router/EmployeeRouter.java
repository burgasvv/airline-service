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
                        "/employees/async", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(employeeService.findAllAsync())
                )
                .GET(
                        "/employees/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(employeeService.findById(request.param("employeeId").orElse(null)))
                )
                .GET(
                        "/employees/by-id/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        employeeService.findByIdAsync(request.param("employeeId")
                                                .orElseThrow()).get()
                                )
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
                .POST(
                        "/employees/create/async", request -> {
                            EmployeeResponse employeeResponse = employeeService.createEmployeeAsync(
                                            request.body(EmployeeRequest.class), request.param("token").orElse(null)
                                    )
                                    .get();
                            return ServerResponse
                                    .status(OK)
                                    .contentType(APPLICATION_JSON)
                                    .body(employeeResponse);
                        }
                )
                .PUT(
                        "/employees/update", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(employeeService.updateEmployee(request.body(EmployeeRequest.class)))
                )
                .PUT(
                        "/employees/update/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(employeeService.updateEmployeeAsync(request.body(EmployeeRequest.class)).get())
                )
                .DELETE(
                        "/employees/delete", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(employeeService.deleteById(request.param("employeeId").orElseThrow()))
                )
                .DELETE(
                        "/employees/delete/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        employeeService.deleteByIdAsync(request.param("employeeId")
                                                .orElseThrow()).get()
                                )
                )
                .build();
    }
}
