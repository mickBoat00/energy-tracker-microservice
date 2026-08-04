package com.meichel.device_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meichel.device_service.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByUserId(Long userId);

}
