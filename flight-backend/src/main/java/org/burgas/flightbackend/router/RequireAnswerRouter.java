package org.burgas.flightbackend.router;

import org.burgas.flightbackend.dto.RequireAnswerRequest;
import org.burgas.flightbackend.filter.RequireAnswerFilterFunction;
import org.burgas.flightbackend.service.RequireAnswerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration
public class RequireAnswerRouter {

    @Bean
    public RouterFunction<ServerResponse> requireAnswerRoutes(final RequireAnswerService requireAnswerService) {
        return RouterFunctions.route()
                .filter(new RequireAnswerFilterFunction())
                .GET(
                        "/flight-service/require-answers/by-user", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(requireAnswerService.findByUserId(request.param("userId").orElse(null)))
                )
                .GET(
                        "/flight-service/require-answers/by-admin", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(requireAnswerService.findByAdminId(request.param("adminId").orElse(null)))
                )
                .POST(
                        "/flight-service/require-answers/send-answer-or-token", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(requireAnswerService.sendAnswerOrToken(request.body(RequireAnswerRequest.class)))
                )
                .build();
    }
}
