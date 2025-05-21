package org.burgas.flightbackend.router;

import org.burgas.flightbackend.dto.AuthorityRequest;
import org.burgas.flightbackend.service.AuthorityService;
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

@Configuration
public class AuthorityRouter {

    @Bean
    public RouterFunction<ServerResponse> authorityRoutes(final AuthorityService authorityService) {
        return RouterFunctions.route()
                .GET(
                        "/flight-service/authorities", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(authorityService.findAll())
                )
                .GET(
                        "/flight-service/authorities/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(authorityService.findAllAsync())
                )
                .GET(
                        "/flight-service/authorities/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(authorityService.findById(request.param("authorityId").orElseThrow()))
                )
                .GET(
                        "/flight-service/authorities/by-id/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        authorityService.findByIdAsync(request.param("authorityId")
                                                .orElseThrow()).get()
                                )
                )
                .POST(
                        "/flight-service/authorities/create-update", request -> {
                            Long authorityId = authorityService.createOrUpdate(request.body(AuthorityRequest.class));
                            return ServerResponse
                                    .status(FOUND)
                                    .location(create("/flight-service/authorities/by-id?authorityId=" + authorityId))
                                    .body(authorityId);
                        }
                )
                .POST(
                        "/flight-service/authorities/create-update/async", request -> {
                            Long authorityId = authorityService.createOrUpdateAsync(request.body(AuthorityRequest.class)).get();
                            return ServerResponse
                                    .status(FOUND)
                                    .location(create("/flight-service/authorities/by-id/async?authorityId=" + authorityId))
                                    .body(authorityId);
                        }
                )
                .DELETE(
                        "/flight-service/authorities/delete", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(authorityService.deleteById(request.param("authorityId").orElseThrow()))
                )
                .DELETE(
                        "/flight-service/authorities/delete/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        authorityService.deleteByIdAsync(request.param("authorityId")
                                                .orElseThrow()).get()
                                )
                )
                .build();
    }
}
