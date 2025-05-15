package org.burgas.excursionbackend.router;

import org.burgas.excursionbackend.dto.CityRequest;
import org.burgas.excursionbackend.dto.CityResponse;
import org.burgas.excursionbackend.service.CityService;
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
                        "/cities", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(cityService.findAll())
                )
                .GET(
                        "/cities/sse", request -> ServerResponse
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
                        "/cities/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(cityService.findAllAsync().get())
                )
                .GET(
                        "/cities/pages/{page}", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        cityService.findAllPages(
                                                Integer.valueOf(request.pathVariable("page")),
                                                Integer.valueOf(request.param("size").orElseThrow())
                                        )
                                                .getContent()
                                )
                )
                .GET(
                        "/cities/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(cityService.findById(request.param("cityId").orElseThrow()))
                )
                .GET(
                        "/cities/by-id/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(cityService.findByIdAsync(request.param("cityId").orElseThrow()).get())
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
                                .body(cityService.deleteById(request.param("cityId").orElseThrow()))
                )
                .DELETE(
                        "/cities/delete/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(cityService.deleteByIdAsync(request.param("cityId").orElseThrow()).get())
                )
                .POST(
                        "/cities/upload-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        cityService.uploadImage(
                                                request.param("cityId").orElseThrow(),
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
                                                request.param("cityId").orElseThrow(),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                                .get()
                                )
                )
                .POST(
                        "/cities/change-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        cityService.changeImage(
                                                request.param("cityId").orElseThrow(),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .POST(
                        "/cities/change-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        cityService.changeImageAsync(
                                                request.param("cityId").orElseThrow(),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                                .get()
                                )
                )
                .DELETE(
                        "/cities/delete-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(cityService.deleteImage(request.param("cityId").orElseThrow()))
                )
                .DELETE(
                        "/cities/delete-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(cityService.deleteImageAsync(request.param("cityId").orElseThrow()).get())
                )
                .build();
    }
}
