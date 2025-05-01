package org.burgas.excursionbackend.router;

import jakarta.servlet.http.Part;
import org.burgas.excursionbackend.entity.Image;
import org.burgas.excursionbackend.service.ImageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.ByteArrayInputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class ImageRouter {

    @Bean
    public RouterFunction<ServerResponse> imageRoutes(final ImageService imageService) {
        return route()
                .GET(
                        "/images/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(imageService.findById(request.param("imageId").orElse(null)))
                )
                .GET(
                        "/images/by-id/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(imageService.findByIdAsync(request.param("imageId").orElse(null)).get())
                )
                .GET(
                        "/images/by-id/data", request -> {
                            Image image = imageService.findImageDataById(request.param("imageId").orElse(null));
                            return ServerResponse
                                    .status(OK)
                                    .contentType(parseMediaType(image.getContentType()))
                                    .body(new InputStreamResource(
                                            new ByteArrayInputStream(image.getData())
                                    ));
                        }
                )
                .GET(
                        "/images/by-id/data/async", request -> {
                            Image image = imageService.findImageDataByIdAsync(request.param("imageId").orElse(null)).get();
                            return ServerResponse
                                    .status(OK)
                                    .contentType(parseMediaType(image.getContentType()))
                                    .body(new InputStreamResource(
                                            new ByteArrayInputStream(image.getData())
                                    ));
                        }
                )
                .POST(
                        "/images/upload", request -> {
                            Part part = request.multipartData().asSingleValueMap().get("file");
                            return ServerResponse
                                    .status(OK)
                                    .contentType(APPLICATION_JSON)
                                    .body(imageService.uploadImage(part));
                        }
                )
                .POST(
                        "/images/upload/async", request -> {
                            Part part = request.multipartData().asSingleValueMap().get("file");
                            return ServerResponse
                                    .status(OK)
                                    .contentType(APPLICATION_JSON)
                                    .body(imageService.uploadImageAsync(part).get());
                        }
                )
                .PUT(
                        "/images/change", request -> {
                            Part part = request.multipartData().asSingleValueMap().get("file");
                            return ServerResponse
                                    .status(OK)
                                    .contentType(APPLICATION_JSON)
                                    .body(imageService.changeImage(request.param("imageId").orElse(null), part));
                        }
                )
                .PUT(
                        "/images/change/async", request -> {
                            Part part = request.multipartData().asSingleValueMap().get("file");
                            return ServerResponse
                                    .status(OK)
                                    .contentType(APPLICATION_JSON)
                                    .body(imageService.changeImageAsync(request.param("imageId").orElse(null), part).get());
                        }
                )
                .DELETE(
                        "/images/delete", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(imageService.deleteImage(request.param("imageId").orElse(null)))
                )
                .DELETE(
                        "/images/delete/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(imageService.deleteImageAsync(request.param("imageId").orElse(null)).get())
                )
                .build();
    }
}
