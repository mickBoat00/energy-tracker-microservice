package com.meichel.user_service.dto;

public record UserDto(
        String address,
        boolean enableAlerting,
        int alertingThreshold) {
}
