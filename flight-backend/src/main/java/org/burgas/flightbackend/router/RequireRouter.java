package org.burgas.flightbackend.router;

import org.burgas.flightbackend.dto.RequireRequest;
import org.burgas.flightbackend.filter.RequireFilterFunction;
import org.burgas.flightbackend.service.RequireService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration
public class RequireRouter {

    @Bean
    public RouterFunction<ServerResponse> requireRoutes(final RequireService requireService) {
        return RouterFunctions
                .route()
                .filter(new RequireFilterFunction())
                .GET(
                        "/requires/by-closed", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(requireService.findAllByClosed(request.param("closed").orElse(null)))
                )
                .GET(
                        "/requires/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(requireService.findById(request.param("requireId").orElse(null)))
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
                .build();
    }
}
