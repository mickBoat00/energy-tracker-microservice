package com.meichel.ingestion_service.dto;

import java.time.LocalDateTime;

public record EnergyConsumedDto(Long deviceId, double energyConsumed, LocalDateTime timestamp) {

}
