package org.burgas.ticketservice.router;

import org.burgas.ticketservice.entity.Image;
import org.burgas.ticketservice.service.ImageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.io.ByteArrayInputStream;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class ImageRouter {

    @Bean
    public RouterFunction<ServerResponse> imageRoutes(final ImageService imageService) {
        return RouterFunctions
                .route(
                        GET("/images/by-id"), request ->
                                imageService.finById(request.queryParam("imageId").orElse(null))
                                        .flatMap(
                                                image -> ServerResponse
                                                        .status(OK)
                                                        .contentType(new MediaType(IMAGE_JPEG))
                                                        .contentType(new MediaType(IMAGE_PNG))
                                                        .body(fromValue(
                                                                new InputStreamResource(new ByteArrayInputStream(image.getData()))
                                                        ))
                                        )
                )
                .andRoute(
                        POST("/images/upload"), request ->
                                request.multipartData()
                                        .flatMap(
                                                valueMap -> {
                                                    FilePart file = (FilePart) valueMap.getFirst("file");
                                                    return ServerResponse
                                                            .status(OK)
                                                            .contentType(APPLICATION_JSON)
                                                            .body(imageService.uploadImage(requireNonNull(file)), Image.class);
                                                }
                                        )
                );
    }
}
