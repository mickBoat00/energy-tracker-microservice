package com.meichel.usage_service.dto;

public record UserResponse(Long id,
    String email,
    boolean enableAlerting,
    int alertingThreshold) {
    
}
