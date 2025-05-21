package org.burgas.excursionbackend.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class AuthenticationRouter {

    @Bean
    public RouterFunction<ServerResponse> authenticationRoutes() {
        return route()
                .GET(
                        "/excursion-service/authentication/csrf-token", request -> ServerResponse
                                .status(OK)
                                .contentType(APPLICATION_JSON)
                                .body(request.attribute("_csrf").orElseGet(CsrfTokenRequestAttributeHandler::new))
                )
                .build();
    }
}
