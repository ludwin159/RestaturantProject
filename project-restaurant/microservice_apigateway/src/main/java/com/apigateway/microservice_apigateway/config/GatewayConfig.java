package com.apigateway.microservice_apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("microservice-productos", r -> r.path("/products/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://MICROSERVICE-PRODUCTOS"))
                .route("microservice-restaurants", r -> r.path("/restaurants/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://MICROSERVICE-RESTAURANTS"))
                .build();
    }
}
