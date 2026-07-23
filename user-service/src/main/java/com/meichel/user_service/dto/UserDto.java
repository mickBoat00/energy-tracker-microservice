package com.meichel.user_service.dto;

public record UserDto(
                Long id,
                String firstName,
                String lastName,
                String address,
                String email,
                boolean enableAlerting,
                int alertingThreshold) {
}
