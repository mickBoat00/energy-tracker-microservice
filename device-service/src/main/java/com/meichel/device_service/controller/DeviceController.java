package com.meichel.device_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meichel.device_service.dto.DeviceRequest;
import com.meichel.device_service.entity.Device;
import com.meichel.device_service.service.DeviceService;

@RestController
@RequestMapping("/api/v1/devices")
public class DeviceController {

    private DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping("/")
    public ResponseEntity<Device> createDevice(
            @RequestHeader(value = "X-User-Sub", required = false) String sub,
            @RequestBody DeviceRequest request) {
        if (sub == null || sub.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Device created = deviceService.createDevice(request, sub);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}/")
    public ResponseEntity<Device> getDeviceById(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.getDeviceById(id));
    }

    @PutMapping("/{id}/")
    public ResponseEntity<Device> updateDevice(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Sub", required = false) String sub,
            @RequestBody DeviceRequest request) {
        if (sub == null || sub.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(deviceService.updateDevice(id, request, sub));
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> deleteDevice(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Sub", required = false) String sub) {
        if (sub == null || sub.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        deviceService.deleteDevice(id, sub);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/")
    public ResponseEntity<List<Device>> getDevicesForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(deviceService.userDevices(userId));
    }

}
