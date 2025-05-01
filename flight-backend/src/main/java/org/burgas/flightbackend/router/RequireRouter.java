package org.burgas.flightbackend.router;

import org.burgas.flightbackend.dto.RequireRequest;
import org.burgas.flightbackend.filter.RequireFilterFunction;
import org.burgas.flightbackend.service.RequireService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class RequireRouter {

    @Bean
    public RouterFunction<ServerResponse> requireRoutes(final RequireService requireService) {
        return route()
                .filter(new RequireFilterFunction())
                .GET(
                        "/requires/by-closed", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(requireService.findAllByClosed(request.param("closed").orElseThrow()))
                )
                .GET(
                        "/requires/by-user", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(requireService.findByUserId(request.param("userId").orElseThrow()))
                )
                .GET(
                        "/requires/by-admin", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(requireService.findByAdminId(request.param("adminId").orElseThrow()))
                )
                .GET(
                        "/requires/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(requireService.findById(request.param("requireId").orElseThrow()))
                )
                .POST(
                        "/requires/create-update", request -> {
                            RequireRequest requireRequest = request.body(RequireRequest.class);
                            requireRequest.setUserId(Long.valueOf(request.param("userId").orElseThrow()));
                            return ServerResponse
                                    .status(OK)
                                    .contentType(APPLICATION_JSON)
                                    .body(requireService.createOrUpdate(requireRequest));
                        }
                )
                .DELETE(
                        "/requires/delete/by-user", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(requireService.deleteById(request.param("requireId").orElseThrow()))
                )
                .DELETE(
                        "/requires/delete/by-admin", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(requireService.deleteById(request.param("requireId").orElseThrow()))
                )
                .build();
    }
}
