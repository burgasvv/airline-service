package org.burgas.flightbackend.router;

import org.burgas.flightbackend.dto.EmployeeRequest;
import org.burgas.flightbackend.dto.EmployeeResponse;
import org.burgas.flightbackend.service.EmployeeService;
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
                        "/flight-service/employees", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(employeeService.finaAll())
                )
                .GET(
                        "/flight-service/employees/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(employeeService.findAllAsync())
                )
                .GET(
                        "/flight-service/employees/pages/{page}", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        employeeService.findAllPages(
                                                Integer.valueOf(request.pathVariable("page")),
                                                Integer.valueOf(request.param("size").orElseThrow())
                                        )
                                                .getContent()
                                )
                )
                .GET(
                        "/flight-service/employees/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(employeeService.findById(request.param("employeeId").orElse(null)))
                )
                .GET(
                        "/flight-service/employees/by-id/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        employeeService.findByIdAsync(request.param("employeeId")
                                                .orElseThrow()).get()
                                )
                )
                .POST(
                        "/flight-service/employees/create", request -> {
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
                        "/flight-service/employees/create/async", request -> {
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
                        "/flight-service/employees/update", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(employeeService.updateEmployee(request.body(EmployeeRequest.class)))
                )
                .PUT(
                        "/flight-service/employees/update/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(employeeService.updateEmployeeAsync(request.body(EmployeeRequest.class)).get())
                )
                .DELETE(
                        "/flight-service/employees/delete", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(employeeService.deleteById(request.param("employeeId").orElseThrow()))
                )
                .DELETE(
                        "/flight-service/employees/delete/async", request -> ServerResponse
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
