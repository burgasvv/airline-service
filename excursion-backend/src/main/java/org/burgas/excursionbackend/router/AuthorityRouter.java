package org.burgas.excursionbackend.router;

import org.burgas.excursionbackend.dto.AuthorityRequest;
import org.burgas.excursionbackend.dto.AuthorityResponse;
import org.burgas.excursionbackend.service.AuthorityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.net.URI;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class AuthorityRouter {

    @Bean
    public RouterFunction<ServerResponse> authorityRoutes(final AuthorityService authorityService) {
        return route()
                .GET(
                        "/excursion-service/authorities", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(authorityService.findAll())
                )
                .GET(
                        "/excursion-service/authorities/sse", request -> ServerResponse
                                .sse(
                                        sseBuilder -> {
                                            authorityService.findAll()
                                                    .forEach(
                                                            authorityResponse ->
                                                            {
                                                                try {
                                                                    SECONDS.sleep(1);
                                                                    sseBuilder.send(authorityResponse);

                                                                } catch (IOException | InterruptedException e) {
                                                                    throw new RuntimeException(e);
                                                                }
                                                            }
                                                    );
                                            sseBuilder.complete();
                                        }
                                )

                )
                .GET(
                        "/excursion-service/authorities/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(authorityService.findAllAsync().get())
                )
                .GET(
                        "/excursion-service/authorities/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(authorityService.findById(request.param("authorityId").orElse(null)))
                )
                .GET(
                        "/excursion-service/authorities/by-id/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(authorityService.findByIdAsync(request.param("authorityId").orElse(null)).get())
                )
                .POST(
                        "/excursion-service/authorities/create-update", request -> {
                            AuthorityResponse authorityResponse = authorityService.createOrUpdate(request.body(AuthorityRequest.class));
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(URI.create("/excursion-service/authorities/by-id?authorityId=" + authorityResponse.getId()))
                                    .body(authorityResponse);
                        }
                )
                .POST(
                        "/excursion-service/authorities/create-update/async", request -> {
                            AuthorityResponse authorityResponse = authorityService.createOrUpdateAsync(request.body(AuthorityRequest.class)).get();
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .body(authorityResponse);
                        }
                )
                .DELETE(
                        "/excursion-service/authorities/delete", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(authorityService.deleteById(request.param("authorityId").orElse(null)))
                )
                .DELETE(
                        "/excursion-service/authorities/delete/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(authorityService.deleteByIdAsync(request.param("authorityId").orElse(null)).get())
                )
                .build();
    }
}
