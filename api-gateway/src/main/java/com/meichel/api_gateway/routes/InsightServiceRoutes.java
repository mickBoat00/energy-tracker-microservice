package com.meichel.api_gateway.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

import java.net.URI;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;

@Configuration
public class InsightServiceRoutes {
    @Bean
    public RouterFunction<ServerResponse> insightRoutes() {

        return route("insight-service")
                .route(RequestPredicates.path("/api/v1/insights/**"), http())
                .before(uri("http://localhost:8086"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker(
                        "insightServiceCircuitBreaker", URI.create("forward:/insightFallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> insightFallbackRoute() {
        return route("insightFallbackRoute")
                .route(
                        RequestPredicates.path("/insightFallbackRoute"),
                        request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body("Insight service is down"))
                .build();
    }
}
