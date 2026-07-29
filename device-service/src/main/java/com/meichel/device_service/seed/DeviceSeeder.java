package com.meichel.device_service.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.meichel.device_service.entity.Device;
import com.meichel.device_service.repository.DeviceRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DeviceSeeder implements CommandLineRunner {

    private final DeviceRepository deviceRepository;

    public DeviceSeeder(DeviceRepository deviceRepository){
        this.deviceRepository = deviceRepository;
    }

    @Override
    public void run(String... args) {
        if (deviceRepository.count() > 0) {
            log.info("Devices already seeded, skipping.");
            return;
        }

        deviceRepository.save(Device.builder()
                .name("Living Room Meter")
                .type("SMART_METER")
                .location("Living Room")
                .userId((long) 1)
                .build());

        deviceRepository.save(Device.builder()
                .name("Solar Inverter")
                .type("INVERTER")
                .location("Rooftop")
                .userId((long) 2)
                .build());

        deviceRepository.save(Device.builder()
                .name("Kitchen Meter")
                .type("SMART_METER")
                .location("Kitchen")
                .userId((long) 2)
                .build());

        deviceRepository.save(Device.builder()
                .name("Bedroom Sensor")
                .type("TEMP_SENSOR")
                .location("Bedroom")
                .userId((long) 2)
                .build());

        deviceRepository.save(Device.builder()
                .name("Garage EV Charger")
                .type("EV_CHARGER")
                .location("Garage")
                .userId((long) 3)
                .build());

        log.info("Seeded {} devices.", deviceRepository.count());
    }
}