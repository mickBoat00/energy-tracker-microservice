package com.meichel.device_service.exception;

public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(Long id) {
        super("Device with id: " + id + " not found.");
    }

    public DeviceNotFoundException(String message) {
        super(message);
    }
}
