package com.meichel.ingestion_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.meichel.ingestion_service.dto.EnergyConsumedDto;
import com.meichel.kafka_event.EnergyConsumedEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IngestionService {

    private final KafkaTemplate<String, EnergyConsumedEvent> kafkaTemplate;

    private String kafkaTopic = "energy-consumption";

    public IngestionService(KafkaTemplate<String, EnergyConsumedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public EnergyConsumedDto ingestEnergyConsumption(EnergyConsumedDto input) {
        log.info("Received consumption data: {}", input);
        EnergyConsumedEvent event = new EnergyConsumedEvent(input.deviceId(), input.energyConsumed(),
                input.timestamp());
        kafkaTemplate.send(kafkaTopic, event);
        log.info("Message {} sent to topic: {}", event, kafkaTopic);
        return input;
    }
}
