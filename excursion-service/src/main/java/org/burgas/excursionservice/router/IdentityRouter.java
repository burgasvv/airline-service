package org.burgas.excursionservice.router;

import org.burgas.excursionservice.dto.IdentityRequest;
import org.burgas.excursionservice.dto.IdentityResponse;
import org.burgas.excursionservice.filter.IdentityFilterFunction;
import org.burgas.excursionservice.service.IdentityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;

import static java.net.URI.create;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration
public class IdentityRouter {

    @Bean
    public RouterFunction<ServerResponse> identityRoutes(final IdentityService identityService) {
        return RouterFunctions.route()
                .filter(new IdentityFilterFunction())
                .GET(
                        "/identities", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.findAll())
                )
                .GET(
                        "/identities/sse", _ -> ServerResponse
                                .sse(
                                        sseBuilder -> {
                                            identityService.findAll().forEach(
                                                    identityResponse -> {
                                                        try {
                                                            SECONDS.sleep(1);
                                                            sseBuilder.send(identityResponse);

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
                        "/identities/async", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.findAllAsync().get())
                )
                .GET(
                        "/identities/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.findById(request.param("identityId").orElse(null)))
                )
                .GET(
                        "/identities/by-id/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.findByIdAsync(request.param("identityId").orElse(null)).get())
                )
                .POST(
                        "/identities/create", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.createOrUpdate(request.body(IdentityRequest.class)))
                )
                .POST(
                        "/identities/create/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.createOrUpdateAsync(request.body(IdentityRequest.class)).get())
                )
                .POST(
                        "/identities/update", request -> {
                            IdentityRequest identityRequest = request.body(IdentityRequest.class);
                            String identityId = request.param("identityId").orElse(null);
                            identityRequest.setId(Long.valueOf(requireNonNull(identityId)));
                            IdentityResponse identityResponse = identityService.createOrUpdate(identityRequest);
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(create("/identities/by-id?identityId=" + identityResponse.getId()))
                                    .body(identityResponse);
                        }
                )
                .POST(
                        "/identities/update/async", request -> {
                            IdentityRequest identityRequest = request.body(IdentityRequest.class);
                            String identityId = request.param("identityId").orElse(null);
                            identityRequest.setId(Long.valueOf(requireNonNull(identityId)));
                            IdentityResponse identityResponse = identityService.createOrUpdateAsync(identityRequest).get();
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(create("/identities/by-id/async?identityId=" + identityResponse.getId()))
                                    .body(identityResponse);
                        }
                )
                .DELETE(
                        "/identities/delete", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.deleteById(request.param("identityId").orElse(null)))
                )
                .DELETE(
                        "/identities/delete/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.deleteByIdAsync(request.param("identityId").orElse(null)).get())
                )
                .build();
    }
}
