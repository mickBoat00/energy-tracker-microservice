package com.meichel.device_service.exception;

public class DeviceForbiddenException extends RuntimeException {

    public DeviceForbiddenException(Long deviceId) {
        super("You do not have permission to modify device: " + deviceId);
    }
}
