package org.burgas.excursionbackend.router;

import org.burgas.excursionbackend.dto.GuideRequest;
import org.burgas.excursionbackend.dto.GuideResponse;
import org.burgas.excursionbackend.service.GuideService;
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
public class GuideRouter {

    @Bean
    public RouterFunction<ServerResponse> guideRoutes(final GuideService guideService) {
        return route()
                .GET(
                        "/excursion-service/guides", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(guideService.findAll())
                )
                .GET(
                        "/excursion-service/guides/sse", request -> ServerResponse
                                .sse(
                                        sseBuilder -> {
                                            guideService.findAll().forEach(
                                                    guideResponse -> {
                                                        try {
                                                            SECONDS.sleep(1);
                                                            sseBuilder.data(guideResponse);

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
                        "/excursion-service/guides/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(guideService.findAllAsync().get())
                )
                .GET(
                        "/excursion-service/guides/pages/{page}", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(
                                        guideService.findAllPages(
                                                Integer.valueOf(request.pathVariable("page")),
                                                Integer.valueOf(request.param("size").orElseThrow())
                                        )
                                                .getContent()
                                )
                )
                .GET(
                        "/excursion-service/guides/by-id", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(guideService.findById(request.param("guideId").orElseThrow()))
                )
                .GET(
                        "/excursion-service/guides/by-id/async", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(guideService.findByIdAsync(request.param("guideId").orElseThrow()).get())
                )
                .POST(
                        "/excursion-service/guides/create-update", request -> {
                            GuideResponse guideResponse = guideService.createOrUpdate(request.body(GuideRequest.class));
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(create("/excursion-service/guides/by-id?guideId=" + guideResponse.getId()))
                                    .body(guideResponse);
                        }
                )
                .POST(
                        "/excursion-service/guides/create-update/async", request -> {
                            GuideResponse guideResponse = guideService.createOrUpdateAsync(request.body(GuideRequest.class)).get();
                            return ServerResponse
                                    .status(FOUND)
                                    .contentType(APPLICATION_JSON)
                                    .location(create("/excursion-service/guides/by-id/async?guideId=" + guideResponse.getId()))
                                    .body(guideResponse);
                        }
                )
                .DELETE(
                        "/excursion-service/guides/delete", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(guideService.deleteById(request.param("guideId").orElseThrow()))
                )
                .DELETE(
                        "/excursion-service/guides/delete/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(guideService.deleteByIdAsync(request.param("guideId").orElseThrow()).get())
                )
                .POST(
                        "/excursion-service/guides/upload-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        guideService.uploadImage(
                                                request.param("guideId").orElseThrow(),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .POST(
                        "/excursion-service/guides/upload-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        guideService.uploadImageAsync(
                                                request.param("guideId").orElseThrow(),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                                .get()
                                )
                )
                .POST(
                        "/excursion-service/guides/change-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        guideService.changeImage(
                                                request.param("guideId").orElseThrow(),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                )
                )
                .POST(
                        "/excursion-service/guides/change-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(
                                        guideService.changeImageAsync(
                                                request.param("guideId").orElseThrow(),
                                                request.multipartData().asSingleValueMap().get("file")
                                        )
                                                .get()
                                )
                )
                .DELETE(
                        "/excursion-service/guides/delete-image", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(guideService.deleteImage(request.param("guideId").orElseThrow()))
                )
                .DELETE(
                        "/excursion-service/guides/delete-image/async", request -> ServerResponse
                                .status(OK)
                                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                                .body(guideService.deleteImageAsync(request.param("guideId").orElseThrow()).get())
                )
                .build();
    }
}
