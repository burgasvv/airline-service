package org.burgas.ticketservice.router;

import org.burgas.ticketservice.dto.AuthorityRequest;
import org.burgas.ticketservice.service.AuthorityService;
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
                        "/authorities", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(authorityService.findAll())
                )
                .GET(
                        "/authorities/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(authorityService.findById(request.param("authorityId").orElse(null)))
                )
                .POST(
                        "/authorities/create-update", request -> {
                            Long authorityId = authorityService.createOrUpdate(request.body(AuthorityRequest.class));
                            return ServerResponse
                                    .status(FOUND)
                                    .location(create("/authorities/by-id?authorityId=" + authorityId))
                                    .body(authorityId);
                        }
                )
                .DELETE(
                        "/authorities/delete", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(authorityService.deleteById(request.param("authorityId").orElse(null)))
                )
                .build();
    }
}
