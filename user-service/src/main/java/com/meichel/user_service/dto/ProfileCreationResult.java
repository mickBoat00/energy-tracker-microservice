package com.meichel.user_service.dto;

public record ProfileCreationResult(UserResponse profile, boolean created) {
}
