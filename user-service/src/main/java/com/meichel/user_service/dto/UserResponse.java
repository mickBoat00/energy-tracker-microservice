package com.meichel.user_service.dto;

public record UserResponse(
        Long id,
        String sub,
        String name,
        String email,
        String address,
        boolean enableAlerting,
        int alertingThreshold) {
}
