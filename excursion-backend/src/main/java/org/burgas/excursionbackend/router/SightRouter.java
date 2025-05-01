package org.burgas.excursionbackend.router;

import org.burgas.excursionbackend.dto.SightRequest;
import org.burgas.excursionbackend.dto.SightResponse;
import org.burgas.excursionbackend.service.SightService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class SightRouter {

    @Bean
    public RouterFunction<ServerResponse> sightRoutes(final SightService sightService) {
        return route()
                .GET(
                        "/sights", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(sightService.findAll())
                )
                .GET(
                        "/sights/sse", _ -> ServerResponse
                                .sse(
                                        sseBuilder -> {
                                            sightService.findAll().forEach(
                                                    sightResponse -> {
                                                        try {
                                                            SECONDS.sleep(1);
                                                            sseBuilder.send(sightResponse);

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
                        "/sights/async", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(sightService.findAllAsync().get())
                )
                .GET(
                        "/sights/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(sightService.findById(request.param("sightId").orElse(null)))
                )
                .GET(
                        "/sights/by-id/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(sightService.findByIdAsync(request.param("sightId").orElse(null)).get())
                )
                .POST(
                        "/sights/create-update", request -> {
                            SightResponse sightResponse = sightService.createOrUpdate(request.body(SightRequest.class));
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(URI.create("/sights/by-id?sightId=" + sightResponse.getId()))
                                    .body(sightResponse);
                        }
                )
                .POST(
                        "/sights/create-update/async", request -> {
                            SightResponse sightResponse = sightService.createOrUpdateAsync(request.body(SightRequest.class)).get();
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(URI.create("/sights/by-id/async?sightId=" + sightResponse.getId()))
                                    .body(sightResponse);
                        }
                )
                .DELETE(
                        "/sights/delete", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(sightService.deleteById(request.param("sightId").orElse(null)))
                )
                .DELETE(
                        "/sights/delete/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(sightService.deleteByIdAsync(request.param("sightId").orElse(null)).get())
                )
                .POST(
                        "/sights/upload-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, StandardCharsets.UTF_8))
                                .body(
                                        sightService.uploadImage(
                                                request.param("sightId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .POST(
                        "/sights/upload-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, StandardCharsets.UTF_8))
                                .body(
                                        sightService.uploadImageAsync(
                                                request.param("sightId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                                .get()
                                )
                )
                .PUT(
                        "/sights/change-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, StandardCharsets.UTF_8))
                                .body(
                                        sightService.changeImage(
                                                request.param("sightId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .PUT(
                        "/sights/change-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, StandardCharsets.UTF_8))
                                .body(
                                        sightService.changeImageAsync(
                                                request.param("sightId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                                .get()
                                )
                )
                .DELETE(
                        "/sights/delete-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, StandardCharsets.UTF_8))
                                .body(sightService.deleteImage(request.param("sightId").orElse(null)))
                )
                .DELETE(
                        "/sights/delete-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, StandardCharsets.UTF_8))
                                .body(sightService.deleteImageAsync(request.param("sightId").orElse(null)).get())
                )
                .build();
    }
}
