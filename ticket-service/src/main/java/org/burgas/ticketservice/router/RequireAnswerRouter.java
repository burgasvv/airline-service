package org.burgas.ticketservice.router;

import org.burgas.ticketservice.dto.RequireAnswerRequest;
import org.burgas.ticketservice.dto.RequireAnswerResponse;
import org.burgas.ticketservice.filter.RequireAnswerFilterFunction;
import org.burgas.ticketservice.service.RequireAnswerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration
public class RequireAnswerRouter {

    @Bean
    public RouterFunction<ServerResponse> requireAnswerRoutes(final RequireAnswerService requireAnswerService) {
        return RouterFunctions.route()
                .filter(new RequireAnswerFilterFunction())
                .GET(
                        "/require-answers/by-user", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(requireAnswerService.findByUserId(request.queryParam("userId").orElse(null)),
                                        RequireAnswerResponse.class)
                )
                .GET(
                        "/require-answers/by-admin", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(requireAnswerService.findByAdminId(request.queryParam("adminId").orElse(null)),
                                        RequireAnswerResponse.class)
                )
                .POST(
                        "/require-answers/send-answer-or-token", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        requireAnswerService.sendAnswerOrToken(request.bodyToMono(RequireAnswerRequest.class)),
                                        Object.class
                                )
                )
                .build();
    }
}
