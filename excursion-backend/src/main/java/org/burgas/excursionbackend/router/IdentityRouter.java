package org.burgas.excursionbackend.router;

import org.burgas.excursionbackend.dto.IdentityRequest;
import org.burgas.excursionbackend.dto.IdentityResponse;
import org.burgas.excursionbackend.filter.IdentityFilterFunction;
import org.burgas.excursionbackend.service.IdentityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.SECONDS;
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
                        "/excursion-service/identities", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.findAll())
                )
                .GET(
                        "/excursion-service/identities/sse", request -> ServerResponse
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
                        "/excursion-service/identities/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.findAllAsync().get())
                )
                .GET(
                        "/excursion-service/identities/pages/{page}", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        identityService.findAllPages(
                                                Integer.valueOf(request.pathVariable("page")),
                                                Integer.valueOf(request.param("size").orElseThrow())
                                        )
                                                .getContent()
                                )
                )
                .GET(
                        "/excursion-service/identities/by-excursion", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.findAllByExcursionId(request.param("excursionId").orElse(null)))
                )
                .GET(
                        "/excursion-service/identities/by-excursion/sse", request -> ServerResponse
                                .sse(
                                        sseBuilder -> {
                                            identityService.findAllByExcursionId(request.param("excursionId").orElse(null))
                                                            .forEach(
                                                                    identityResponse -> {
                                                                        try {
                                                                            SECONDS.sleep(1);
                                                                            sseBuilder.send(identityResponse);

                                                                        } catch (InterruptedException | IOException e) {
                                                                            throw new RuntimeException(e);
                                                                        }
                                                                    }
                                                            );
                                            sseBuilder.complete();
                                        }
                                )
                )
                .GET(
                        "/excursion-service/identities/by-excursion/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.findAllByExcursionIdAsync(request.param("excursionId").orElse(null)).get())
                )
                .GET(
                        "/excursion-service/identities/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.findById(request.param("identityId").orElse(null)))
                )
                .GET(
                        "/excursion-service/identities/by-id/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.findByIdAsync(request.param("identityId").orElse(null)).get())
                )
                .POST(
                        "/excursion-service/identities/create", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.createOrUpdate(request.body(IdentityRequest.class)))
                )
                .POST(
                        "/excursion-service/identities/create/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.createOrUpdateAsync(request.body(IdentityRequest.class)).get())
                )
                .POST(
                        "/excursion-service/identities/update", request -> {
                            IdentityRequest identityRequest = request.body(IdentityRequest.class);
                            String identityId = request.param("identityId").orElseThrow();
                            identityRequest.setId(Long.valueOf(requireNonNull(identityId)));
                            IdentityResponse identityResponse = identityService.createOrUpdate(identityRequest);
                            return ServerResponse
                                    .status(OK)
                                    .contentType(APPLICATION_JSON)
                                    .body(identityResponse);
                        }
                )
                .POST(
                        "/excursion-service/identities/update/async", request -> {
                            IdentityRequest identityRequest = request.body(IdentityRequest.class);
                            String identityId = request.param("identityId").orElse(null);
                            identityRequest.setId(Long.valueOf(requireNonNull(identityId)));
                            IdentityResponse identityResponse = identityService.createOrUpdateAsync(identityRequest).get();
                            return ServerResponse
                                    .status(OK)
                                    .contentType(APPLICATION_JSON)
                                    .body(identityResponse);
                        }
                )
                .DELETE(
                        "/excursion-service/identities/delete", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.deleteById(request.param("identityId").orElse(null)))
                )
                .DELETE(
                        "/excursion-service/identities/delete/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(identityService.deleteByIdAsync(request.param("identityId").orElse(null)).get())
                )
                .PUT(
                        "/excursion-service/identities/control", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(identityService.accountControl(
                                        request.param("identityId").orElse(null),
                                        request.param("enable").orElse(null)
                                ))
                )
                .PUT(
                        "/excursion-service/identities/control/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(identityService.accountControlAsync(
                                        request.param("identityId").orElse(null),
                                        request.param("enable").orElse(null)
                                ))
                )
                .POST(
                        "/excursion-service/identities/upload-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        identityService.uploadImage(
                                                request.param("identityId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .POST(
                        "/excursion-service/identities/upload-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        identityService.uploadImageAsync(
                                                request.param("identityId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                                .get()
                                )
                )
                .PUT(
                        "/excursion-service/identities/change-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        identityService.changeImage(
                                                request.param("identityId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .PUT(
                        "/excursion-service/identities/change-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        identityService.changeImageAsync(
                                                request.param("identityId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                                .get()
                                )
                )
                .DELETE(
                        "/excursion-service/identities/delete-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(identityService.deleteImage(request.param("identityId").orElse(null)))
                )
                .DELETE(
                        "/excursion-service/identities/delete-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(identityService.deleteImageAsync(request.param("identityId").orElse(null)).get())
                )
                .build();
    }
}
