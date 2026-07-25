package com.meichel.kafka_event;

import java.time.LocalDateTime;

public record EnergyConsumedEvent(
        Long deviceId,
        double energyConsumed,
        LocalDateTime timestamp) {

}
