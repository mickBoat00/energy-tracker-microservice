package com.meichel.kafka_event;

import java.time.LocalDateTime;
import java.util.List;

import com.meichel.alert_service.dto.DeviceEnergyUsage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageAlertEvent {
    private Long userId;
    private String email;
    private Double totalConsumed;
    private Integer threshold;
    private List<DeviceEnergyUsage> devices;
    private LocalDateTime timestamp;
    
}
