package org.burgas.excursionservice.router;

import org.burgas.excursionservice.dto.ExcursionRequest;
import org.burgas.excursionservice.dto.ExcursionResponse;
import org.burgas.excursionservice.filter.IdentityFilterFunction;
import org.burgas.excursionservice.service.ExcursionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;

import static java.net.URI.create;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class ExcursionRouter {

    @Bean
    public RouterFunction<ServerResponse> excursionRoutes(final ExcursionService excursionService) {
        return route()
                .filter(new IdentityFilterFunction())
                .GET(
                        "/excursions", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findAll())
                )
                .GET(
                        "/excursions/sse", _ -> ServerResponse
                                .sse(
                                        sseBuilder -> {
                                            excursionService.findAll().forEach(
                                                    excursionResponse -> {
                                                        try {
                                                            SECONDS.sleep(1);
                                                            sseBuilder.send(excursionResponse);

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
                        "/excursions/async", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findAllAsync().get())
                )
                .GET(
                        "/excursions/by-guide", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findAllByGuideId(request.param("guideId").orElse(null)))
                )
                .GET(
                        "/excursions/by-guide/sse", request -> ServerResponse
                                .sse(
                                        sseBuilder -> {
                                            excursionService.findAllByGuideId(request.param("guideId").orElse(null))
                                                            .forEach(
                                                                    excursionResponse -> {
                                                                        try {
                                                                            SECONDS.sleep(1);
                                                                            sseBuilder.send(excursionResponse);

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
                        "/excursions/by-guide/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findAllByGuideIdAsync(request.param("guideId").orElse(null)).get())
                )
                .GET(
                        "/excursions/by-identity", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findAllByIdentityId(request.param("identityId").orElse(null)))
                )
                .GET(
                        "/excursions/by-identity/sse", request -> ServerResponse
                                .sse(
                                        sseBuilder -> {
                                            excursionService.findAllByIdentityId(request.param("identityId").orElse(null))
                                                            .forEach(
                                                                    excursionResponse -> {
                                                                        try {
                                                                            SECONDS.sleep(1);
                                                                            sseBuilder.send(excursionResponse);

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
                        "/excursions/by-identity/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findAllByIdentityIdAsync(request.param("identityId").orElse(null)).get())
                )
                .GET(
                        "/excursions/by-session", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findAllBySession(request.servletRequest()))
                )
                .GET(
                        "/excursions/by-session/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findAllBySessionAsync(request.servletRequest()).get())
                )
                .GET(
                        "/excursions/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findById(request.param("excursionId").orElse(null)))
                )
                .GET(
                        "/excursions/by-id/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findByIdAsync(request.param("excursionId").orElse(null)).get())
                )
                .POST(
                        "/excursions/add-by-identity", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.addExcursionByIdentityId(
                                        request.param("excursionId").orElse(null),
                                        request.param("identityId").orElse(null)
                                ))
                )
                .POST(
                        "/excursions/add-by-identity/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.addExcursionByIdentityIdAsync(
                                        request.param("excursionId").orElse(null),
                                        request.param("identityId").orElse(null)
                                ))
                )
                .POST(
                        "/excursions/add-to-session", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.addExcursionToSession(
                                        request.param("excursionId").orElse(null),
                                        request.servletRequest()
                                ))
                )
                .POST(
                        "/excursions/add-to-session/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.addExcursionToSessionAsync(
                                        request.param("excursionId").orElse(null),
                                        request.servletRequest()
                                ))
                )
                .POST(
                        "/excursions/create-update", request -> {
                            ExcursionResponse excursionResponse = excursionService.createOrUpdate(request.body(ExcursionRequest.class));
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(create("/excursions/by-id?excursionId=" + excursionResponse.getId()))
                                    .body(excursionResponse);
                        }
                )
                .POST(
                        "/excursions/create-update/async", request -> {
                            ExcursionResponse excursionResponse = excursionService.createOrUpdateAsync(request.body(ExcursionRequest.class)).get();
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(create("/excursions/by-id/async?excursionId=" + excursionResponse.getId()))
                                    .body(excursionResponse);
                        }
                )
                .DELETE(
                        "/excursions/delete", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(excursionService.deleteById(request.param("excursionId").orElse(null)))
                )
                .DELETE(
                        "/excursions/delete/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(excursionService.deleteByIdAsync(request.param("excursionId").orElse(null)).get())
                )
                .POST(
                        "/excursions/upload-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        excursionService.uploadImage(
                                                request.param("excursionId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .POST(
                        "/excursions/upload-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        excursionService.uploadImageAsync(
                                                request.param("excursionId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                                .get()
                                )
                )
                .PUT(
                        "/excursions/change-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        excursionService.changeImage(
                                                request.param("excursionId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .PUT(
                        "/excursions/change-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        excursionService.changeImageAsync(
                                                request.param("excursionId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                                .get()
                                )
                )
                .DELETE(
                        "/excursions/delete-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(excursionService.deleteImage(request.param("excursionId").orElse(null)))
                )
                .DELETE(
                        "/excursions/delete-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(excursionService.deleteImageAsync(request.param("excursionId").orElse(null)).get())
                )
                .build();
    }
}
