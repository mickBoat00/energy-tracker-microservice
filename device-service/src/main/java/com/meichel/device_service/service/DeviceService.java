package com.meichel.device_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meichel.device_service.entity.Device;
import com.meichel.device_service.exception.DeviceNotFoundException;
import com.meichel.device_service.repository.DeviceRepository;

@Service
public class DeviceService {

    private DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Device createDevice(Device input) {
        return deviceRepository.save(input);
    }

    public Device getDeviceById(Long id) {
        return deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));
    }

    public Device updateDevice(Long id, Device request) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));

        device.setName(request.getName());
        device.setType(request.getType());
        device.setLocation(request.getLocation());
        device.setUserId(request.getUserId());

        return deviceRepository.save(device);
    }

    public void deleteDevice(Long id) {
        if (!deviceRepository.existsById(id)) {
            throw new DeviceNotFoundException(id);
        }
        deviceRepository.deleteById(id);
    }

    public List<Device> userDevices(Long userId) {
        return deviceRepository.findByUserId(userId);
    }

}
