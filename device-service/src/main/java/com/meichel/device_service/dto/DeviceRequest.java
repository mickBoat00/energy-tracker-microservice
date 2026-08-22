package com.meichel.device_service.dto;

public record DeviceRequest(
        String name,
        String type,
        String location) {
}
