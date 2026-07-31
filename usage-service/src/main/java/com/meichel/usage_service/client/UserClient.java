package com.meichel.usage_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.meichel.usage_service.dto.UserResponse;

@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(@Value("${user.service.base.url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public UserResponse getUserById(Long id) {
        return restClient.get()
                .uri("/api/v1/users/{id}/", id)
                .retrieve()
                .body(UserResponse.class);
    }
}