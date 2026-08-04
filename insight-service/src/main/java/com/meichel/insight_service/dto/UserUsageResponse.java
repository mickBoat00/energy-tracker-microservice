package com.meichel.insight_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUsageResponse {
    private UserResponse user;
    private List<DeviceEnergyUsage> devices;
    private Double totalEnergyConsumed;
}
