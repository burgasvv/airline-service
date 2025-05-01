package org.burgas.excursionbackend.router;

import org.burgas.excursionbackend.dto.CountryRequest;
import org.burgas.excursionbackend.dto.CountryResponse;
import org.burgas.excursionbackend.service.CountryService;
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
public class CountryRouter {

    @Bean
    public RouterFunction<ServerResponse> countryRoutes(final CountryService countryService) {
        return route()
                .GET(
                        "/countries", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(countryService.findAll())
                )
                .GET(
                        "/countries/sse", _ -> ServerResponse
                                .sse(
                                        sseBuilder -> {
                                            countryService.findAll().forEach(
                                                    countryResponse -> {
                                                        try {
                                                            SECONDS.sleep(1);
                                                            sseBuilder.send(countryResponse);

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
                        "/countries/async", _ -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(countryService.findAllAsync().get())
                )
                .GET(
                        "/countries/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(countryService.findById(request.param("countryId").orElse(null)))
                )
                .GET(
                        "/countries/by-id/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(countryService.findByIdAsync(request.param("countryId").orElse(null)).get())
                )
                .POST(
                        "/countries/create-update", request -> {
                            CountryResponse countryResponse = countryService.createOrUpdate(request.body(CountryRequest.class));
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(create("/countries/by-id?countryId=" + countryResponse.getId()))
                                    .body(countryResponse);
                        }
                )
                .POST(
                        "/countries/create-update/async", request -> {
                            CountryResponse countryResponse = countryService.createOrUpdateAsync(request.body(CountryRequest.class)).get();
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(create("/countries/by-id/async?countryId=" + countryResponse.getId()))
                                    .body(countryResponse);
                        }
                )
                .DELETE(
                        "/countries/delete", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(countryService.deleteById(request.param("countryId").orElse(null)))
                )
                .DELETE(
                        "/countries/delete/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(countryService.deleteByIdAsync(request.param("countryId").orElse(null)).get())
                )
                .POST(
                        "/countries/upload-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        countryService.uploadImage(
                                                request.param("countryId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .POST(
                        "/countries/upload-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        countryService.uploadImageAsync(
                                                request.param("countryId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                                .get()
                                )
                )
                .POST(
                        "/countries/change-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        countryService.changeImage(
                                                request.param("countryId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .POST(
                        "/countries/change-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        countryService.changeImageAsync(
                                                request.param("countryId").orElse(null),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                                .get()
                                )
                )
                .DELETE(
                        "/countries/delete-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(countryService.deleteImage(request.param("countryId").orElse(null)))
                )
                .DELETE(
                        "/countries/delete-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(countryService.deleteImageAsync(request.param("countryId").orElse(null)).get())
                )
                .build();
    }
}
