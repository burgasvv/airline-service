package org.burgas.excursionbackend.router;

import org.burgas.excursionbackend.dto.ExcursionRequest;
import org.burgas.excursionbackend.dto.ExcursionResponse;
import org.burgas.excursionbackend.filter.IdentityFilterFunction;
import org.burgas.excursionbackend.service.ExcursionService;
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
                        "/excursion-service/excursions", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findAll())
                )
                .GET(
                        "/excursion-service/excursions/sse", request -> ServerResponse
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
                        "/excursion-service/excursions/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findAllAsync().get())
                )
                .GET(
                        "/excursion-service/excursions/pages/{page}", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        excursionService.findAllPages(
                                                Integer.valueOf(request.pathVariable("page")),
                                                Integer.valueOf(request.param("size").orElseThrow())
                                        )
                                                .getContent()
                                )
                )
                .GET(
                        "/excursion-service/excursions/by-guide", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findAllByGuideId(request.param("guideId").orElseThrow()))
                )
                .GET(
                        "/excursion-service/excursions/by-guide/sse", request -> ServerResponse
                                .sse(
                                        sseBuilder -> {
                                            excursionService.findAllByGuideId(request.param("guideId").orElseThrow())
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
                        "/excursion-service/excursions/by-guide/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findAllByGuideIdAsync(request.param("guideId").orElseThrow()).get())
                )
                .GET(
                        "/excursion-service/excursions/by-identity", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findAllByIdentityId(request.param("identityId").orElseThrow()))
                )
                .GET(
                        "/excursion-service/excursions/by-identity/sse", request -> ServerResponse
                                .sse(
                                        sseBuilder -> {
                                            excursionService.findAllByIdentityId(request.param("identityId").orElseThrow())
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
                        "/excursion-service/excursions/by-identity/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findAllByIdentityIdAsync(request.param("identityId").orElseThrow()).get())
                )
                .GET(
                        "/excursion-service/excursions/by-session", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findAllBySession(request.servletRequest()))
                )
                .GET(
                        "/excursion-service/excursions/by-session/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findAllBySessionAsync(request.servletRequest()).get())
                )
                .GET(
                        "/excursion-service/excursions/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findById(request.param("excursionId").orElseThrow()))
                )
                .GET(
                        "/excursion-service/excursions/by-id/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.findByIdAsync(request.param("excursionId").orElseThrow()).get())
                )
                .POST(
                        "/excursion-service/excursions/add-by-identity", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        excursionService.addExcursionByIdentityId(
                                                request.param("excursionId").orElseThrow(),
                                                request.param("identityId").orElseThrow()
                                        )
                                )
                )
                .POST(
                        "/excursion-service/excursions/add-by-identity/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        excursionService.addExcursionByIdentityIdAsync(
                                                request.param("excursionId").orElseThrow(),
                                                request.param("identityId").orElseThrow()
                                        )
                                                .get()
                                )
                )
                .POST(
                        "/excursion-service/excursions/add-to-session", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(excursionService.addExcursionToSession(
                                        request.param("excursionId").orElseThrow(),
                                        request.servletRequest()
                                ))
                )
                .POST(
                        "/excursion-service/excursions/add-to-session/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        excursionService.addExcursionToSessionAsync(
                                                request.param("excursionId").orElseThrow(),
                                                request.servletRequest()
                                        )
                                                .get()
                                )
                )
                .POST(
                        "/excursion-service/excursions/create-update", request -> {
                            ExcursionResponse excursionResponse = excursionService.createOrUpdate(request.body(ExcursionRequest.class));
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(create("/excursion-service/excursions/by-id?excursionId=" + excursionResponse.getId()))
                                    .body(excursionResponse);
                        }
                )
                .DELETE(
                        "/excursion-service/excursions/delete", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(excursionService.deleteById(request.param("excursionId").orElseThrow()))
                )
                .DELETE(
                        "/excursion-service/excursions/delete/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(excursionService.deleteByIdAsync(request.param("excursionId").orElseThrow()).get())
                )
                .POST(
                        "/excursion-service/excursions/upload-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        excursionService.uploadImage(
                                                request.param("excursionId").orElseThrow(),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .POST(
                        "/excursion-service/excursions/upload-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        excursionService.uploadImageAsync(
                                                request.param("excursionId").orElseThrow(),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                                .get()
                                )
                )
                .POST(
                        "/excursion-service/excursions/change-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        excursionService.changeImage(
                                                request.param("excursionId").orElseThrow(),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .POST(
                        "/excursion-service/excursions/change-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        excursionService.changeImageAsync(
                                                request.param("excursionId").orElseThrow(),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                                .get()
                                )
                )
                .DELETE(
                        "/excursion-service/excursions/delete-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(excursionService.deleteImage(request.param("excursionId").orElseThrow()))
                )
                .DELETE(
                        "/excursion-service/excursions/delete-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(excursionService.deleteImageAsync(request.param("excursionId").orElseThrow()).get())
                )
                .build();
    }
}
