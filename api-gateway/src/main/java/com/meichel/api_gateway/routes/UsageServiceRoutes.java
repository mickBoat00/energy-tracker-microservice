package com.meichel.api_gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

import java.net.URI;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;

@Configuration
public class UsageServiceRoutes {

    @Value("${usage.service.base.url}")
    private String usageServiceBaseUrl;

    @Bean
    public RouterFunction<ServerResponse> usageRoutes() {

        return route("usage-service")
                .route(RequestPredicates.path("/api/v1/usage/**"), http())
                .before(uri(usageServiceBaseUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker(
                        "usageServiceCircuitBreaker", URI.create("forward:/usageFallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> usageFallbackRoute() {
        return route("usageFallbackRoute")
                .route(
                        RequestPredicates.path("/usageFallbackRoute"),
                        request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body("Usage service is down"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> usageServiceApiDocs() {
        return GatewayRouterFunctions.route("usage-service-api-docs")
                .route(RequestPredicates.path("/docs/usage-service/v3/api-docs"),
                        http())
                .before(uri(usageServiceBaseUrl))
                .filter(setPath("/v3/api-docs"))
                .build();
    }
}
