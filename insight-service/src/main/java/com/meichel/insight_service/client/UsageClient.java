package com.meichel.insight_service.client;

import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.meichel.insight_service.dto.UserUsageResponse;

@Component
public class UsageClient {

    protected final RestClient restClient;

    protected UsageClient(@Value("${usage.service.base.url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public UserUsageResponse getUserOverview(Long userId, long interval, ChronoUnit intervalUnit) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{userId}")
                        .queryParam("timeInterval", interval)
                        .queryParam("timeUnit", intervalUnit.name())
                        .build(userId))
                .retrieve()
                .body(UserUsageResponse.class);
    }
}
