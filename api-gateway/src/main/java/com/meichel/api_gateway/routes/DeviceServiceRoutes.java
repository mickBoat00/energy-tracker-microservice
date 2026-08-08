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
public class DeviceServiceRoutes {
    @Bean
    public RouterFunction<ServerResponse> deviceRoutes() {

        return route("device-service")
                .route(RequestPredicates.path("/api/v1/devices/**"), http())
                .before(uri("http://localhost:8082"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker(
                        "deviceServiceCircuitBreaker", URI.create("forward:/deviceFallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> deviceFallbackRoute() {
        return route("deviceFallbackRoute")
                .route(
                        RequestPredicates.path("/deviceFallbackRoute"),
                        request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body("Device service is down"))
                .build();
    }
}
