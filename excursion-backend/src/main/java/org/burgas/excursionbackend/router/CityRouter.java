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
                        "/excursion-service/cities", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(cityService.findAll())
                )
                .GET(
                        "/excursion-service/cities/sse", request -> ServerResponse
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
                        "/excursion-service/cities/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(cityService.findAllAsync().get())
                )
                .GET(
                        "/excursion-service/cities/pages/{page}", request -> ServerResponse
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
                        "/excursion-service/cities/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(cityService.findById(request.param("cityId").orElseThrow()))
                )
                .GET(
                        "/excursion-service/cities/by-id/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(cityService.findByIdAsync(request.param("cityId").orElseThrow()).get())
                )
                .POST(
                        "/excursion-service/cities/create-update", request -> {
                            CityResponse cityResponse = cityService.createOrUpdate(request.body(CityRequest.class));
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(create("/excursion-service/cities/by-id?cityId=" + cityResponse.getId()))
                                    .body(cityResponse);
                        }
                )
                .POST(
                        "/excursion-service/cities/create-update/async", request -> {
                            CityResponse cityResponse = cityService.createOrUpdateAsync(request.body(CityRequest.class)).get();
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(create("/excursion-service/cities/by-id/async?cityId=" + cityResponse.getId()))
                                    .body(cityResponse);
                        }
                )
                .DELETE(
                        "/excursion-service/cities/delete", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(cityService.deleteById(request.param("cityId").orElseThrow()))
                )
                .DELETE(
                        "/excursion-service/cities/delete/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(cityService.deleteByIdAsync(request.param("cityId").orElseThrow()).get())
                )
                .POST(
                        "/excursion-service/cities/upload-image", request -> ServerResponse
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
                        "/excursion-service/cities/upload-image/async", request -> ServerResponse
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
                        "/excursion-service/cities/change-image", request -> ServerResponse
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
                        "/excursion-service/cities/change-image/async", request -> ServerResponse
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
                        "/excursion-service/cities/delete-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(cityService.deleteImage(request.param("cityId").orElseThrow()))
                )
                .DELETE(
                        "/excursion-service/cities/delete-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(cityService.deleteImageAsync(request.param("cityId").orElseThrow()).get())
                )
                .build();
    }
}
