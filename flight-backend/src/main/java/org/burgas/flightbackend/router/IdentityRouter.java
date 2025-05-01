package org.burgas.flightbackend.router;

import org.burgas.flightbackend.dto.IdentityRequest;
import org.burgas.flightbackend.filter.IdentityFilterFunction;
import org.burgas.flightbackend.service.IdentityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class IdentityRouter {

    @Bean
    public RouterFunction<ServerResponse> identityRoutes(final IdentityService identityService) {
        return route()
                .filter(new IdentityFilterFunction())
                .GET(
                        "/identities", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.findAll())
                )
                .GET(
                        "/identities/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.findById(request.param("identityId").orElse(null)))
                )
                .GET(
                        "/identities/by-username", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.findByUsername(request.param("username").orElse(null)))
                )
                .POST(
                        "/identities/create", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.createOrUpdate(request.body(IdentityRequest.class)))
                )
                .POST(
                        "/identities/update", request -> {
                            IdentityRequest identityRequest = request.body(IdentityRequest.class);
                            identityRequest.setId(
                                    Long.valueOf(requireNonNull(
                                            request.param("identityId").orElseThrow())
                                    )
                            );
                            return ServerResponse
                                    .status(OK)
                                    .contentType(APPLICATION_JSON)
                                    .body(identityService.createOrUpdate(identityRequest));
                        }
                )
                .PUT(
                        "/identities/enable-disable", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        identityService.accountEnableOrDisable(
                                                request.param("identityId").orElseThrow(),
                                                request.param("enabled").orElseThrow()
                                        )
                                )
                )
                .POST(
                        "/identities/change-password", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(identityService.changePassword(request.param("identityId").orElseThrow()))
                )
                .PUT(
                        "/identities/set-password", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        identityService.setPassword(
                                                request.param("identityId").orElseThrow(),
                                                request.param("token").orElseThrow(),
                                                request.param("password").orElseThrow()
                                        )
                                )
                )
                .POST(
                        "/identities/upload-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        identityService.uploadImage(
                                                request.param("identityId").orElseThrow(),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .POST(
                        "/identities/change-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        identityService.changeImage(
                                                request.param("identityId").orElseThrow(),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .DELETE(
                        "/identities/delete-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(identityService.deleteImage(request.param("identityId").orElseThrow()))
                )
                .build();
    }
}
