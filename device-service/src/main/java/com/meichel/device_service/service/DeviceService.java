package com.meichel.device_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meichel.device_service.dto.DeviceRequest;
import com.meichel.device_service.entity.Device;
import com.meichel.device_service.exception.DeviceForbiddenException;
import com.meichel.device_service.exception.DeviceNotFoundException;
import com.meichel.device_service.repository.DeviceRepository;

@Service
public class DeviceService {

    private DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Device createDevice(DeviceRequest request, String userSub) {
        Device device = Device.builder()
                .name(request.name())
                .type(request.type())
                .location(request.location())
                .userSub(resolveUserSub(userSub))
                .build();
        return deviceRepository.save(device);
    }

    public Device getDeviceById(Long id) {
        return deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));
    }

    public Device updateDevice(Long id, DeviceRequest request, String userSub) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
        verifyOwnership(device, userSub);
        device.setName(request.name());
        device.setType(request.type());
        device.setLocation(request.location());
        return deviceRepository.save(device);
    }

    public void deleteDevice(Long id, String userSub) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
        verifyOwnership(device, userSub);
        deviceRepository.delete(device);
    }

    public List<Device> userDevices(Long userId) {
        return deviceRepository.findByUserId(userId);
    }

    private String resolveUserSub(String userSub) {
        if (userSub == null || userSub.isBlank()) {
            return null;
        }
        return userSub;
    }

    private void verifyOwnership(Device device, String userSub) {
        if (userSub == null || userSub.isBlank()) {
            throw new DeviceForbiddenException(device.getId());
        }
        if (device.getUserSub() == null || !device.getUserSub().equals(userSub)) {
            throw new DeviceForbiddenException(device.getId());
        }
    }
}
