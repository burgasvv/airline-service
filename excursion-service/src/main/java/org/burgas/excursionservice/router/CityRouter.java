package org.burgas.excursionservice.router;

import org.burgas.excursionservice.dto.CityRequest;
import org.burgas.excursionservice.dto.CityResponse;
import org.burgas.excursionservice.service.CityService;
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
public class CityRouter {

    @Bean
    public RouterFunction<ServerResponse> cityRoutes(final CityService cityService) {
        return route()
                .GET(
                        "/cities", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(cityService.findAll())
                )
                .GET(
                        "/cities/sse", _ -> ServerResponse
                                .sse(
                                        sseBuilder -> {
                                            cityService.findAll().forEach(
                                                    cityResponse -> {
                                                        try {
                                                            SECONDS.sleep(1);
                                                            sseBuilder.send(cityResponse);

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
                        "/cities/async", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(cityService.findAllAsync().get())
                )
                .GET(
                        "/cities/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(cityService.findById(request.param("cityId").orElse(null)))
                )
                .GET(
                        "/cities/by-id/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(cityService.findByIdAsync(request.param("cityId").orElse(null)).get())
                )
                .POST(
                        "/cities/create-update", request -> {
                            CityResponse cityResponse = cityService.createOrUpdate(request.body(CityRequest.class));
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(create("/cities/by-id?cityId=" + cityResponse.getId()))
                                    .body(cityResponse);
                        }
                )
                .POST(
                        "/cities/create-update/async", request -> {
                            CityResponse cityResponse = cityService.createOrUpdateAsync(request.body(CityRequest.class)).get();
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(create("/cities/by-id/async?cityId=" + cityResponse.getId()))
                                    .body(cityResponse);
                        }
                )
                .DELETE(
                        "/cities/delete", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(cityService.deleteById(request.param("cityId").orElse(null)))
                )
                .DELETE(
                        "/cities/delete/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(cityService.deleteByIdAsync(request.param("cityId").orElse(null)).get())
                )
                .POST(
                        "/cities/upload-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        cityService.uploadImage(
                                                request.param("cityId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .POST(
                        "/cities/upload-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        cityService.uploadImageAsync(
                                                request.param("cityId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .PUT(
                        "/cities/change-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        cityService.changeImage(
                                                request.param("cityId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .PUT(
                        "/cities/change-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        cityService.changeImageAsync(
                                                request.param("cityId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .DELETE(
                        "/cities/delete", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(cityService.deleteImage(request.param("cityId").orElse(null)))
                )
                .DELETE(
                        "/cities/delete/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(cityService.deleteImageAsync(request.param("cityId").orElse(null)))
                )
                .build();
    }
}
