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
                                .path("/flight-service/**")
                                .uri("http://flight-backend:9000")
                )
                .route(
                        "excursion-backend", predicateSpec -> predicateSpec
                                .path("/excursion-service/**")
                                .uri("http://excursion-backend:9010")
                )
                .route(
                        "hotel-backend", predicateSpec -> predicateSpec
                                .path("/hotel-service/**")
                                .uri("http://hotel-backend:9020")
                )
                .build();
    }
}
