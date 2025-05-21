package org.burgas.gatewayserver.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(
                        "flight-backend", predicateSpec -> predicateSpec
                                .path(
                                        "/flight-service/addresses/**", "/flight-service/airports/**", "/flight-service/authentication/**",
                                        "/flight-service/authorities/**", "/flight-service/departments/**", "/flight-service/employees/**",
                                        "/flight-service/filial-departments/**", "/flight-service/filials/**", "/flight-service/flights/**",
                                        "/flight-service/flight-seats/**", "/flight-service/identities/**", "/flight-service/images/**",
                                        "/flight-service/ordered-tickets/**", "/flight-service/planes/**", "/flight-service/positions/**",
                                        "/flight-service/require-answers/**", "/flight-service/requires/**", "/flight-service/tickets/**"
                                )
                                .uri("http://localhost:9000")
                )
                .route(
                        "excursion-backend", predicateSpec -> predicateSpec
                                .path(
                                        "/excursion-service/authentication/**", "/excursion-service/authorities/**", "/excursion-service/cities/**",
                                        "/excursion-service/countries/**", "/excursion-service/excursions/**", "/excursion-service/guides/**",
                                        "/excursion-service/identities/**", "/excursion-service/images/**", "/excursion-service/payments/**",
                                        "/excursion-service/sights/**"
                                )
                                .uri("http://localhost:9010")
                )
                .route(
                        "hotel-backend", predicateSpec -> predicateSpec
                                .path(
                                        "/hotel-service/addresses/**", "/hotel-service/authentication/**", "/hotel-service/authorities/**",
                                        "/hotel-service/cities/**", "/hotel-service/clients/**", "/hotel-service/countries/**",
                                        "/hotel-service/departments/**", "/hotel-service/employees/**", "/hotel-service/filials/**",
                                        "/hotel-service/hotels/**", "/hotel-service/identities/**", "/hotel-service/images/**",
                                        "/hotel-service/payments/**", "/hotel-service/positions/**", "/hotel-service/rooms/**"
                                )
                                .uri("http://localhost:9020")
                )
                .build();
    }
}
