package com.meichel.alert_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DeviceEnergyUsage {
    private Long DeviceId;
    private String name;
    private String type;
    private Double EnergyConsumed;
    private Long userId;
}
