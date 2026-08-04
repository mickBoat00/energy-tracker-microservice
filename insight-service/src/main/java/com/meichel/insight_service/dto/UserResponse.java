package com.meichel.insight_service.dto;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String address,
        String email,
        boolean enableAlerting,
        int alertingThreshold) {

}
