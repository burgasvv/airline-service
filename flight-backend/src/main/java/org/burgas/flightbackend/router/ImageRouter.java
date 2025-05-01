package org.burgas.flightbackend.router;

import org.burgas.flightbackend.entity.Image;
import org.burgas.flightbackend.service.ImageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.ByteArrayInputStream;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.servlet.function.RequestPredicates.GET;

@Configuration
public class ImageRouter {

    @Bean
    public RouterFunction<ServerResponse> imageRoutes(final ImageService imageService) {
        return RouterFunctions
                .route(
                        GET("/images/by-id"), request -> {
                            Image image = imageService.findById(request.param("imageId").orElseThrow());
                            return ServerResponse
                                    .status(OK)
                                    .contentType(MediaType.parseMediaType(image.getContentType()))
                                    .body(new InputStreamResource(
                                            new ByteArrayInputStream(image.getData()))
                                    );
                        }
                );
    }
}
