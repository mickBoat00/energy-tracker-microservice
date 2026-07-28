package com.meichel.usage_service.service;

import java.time.Instant;
import java.time.ZoneOffset;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.influxdb.v3.client.InfluxDBClient;
import com.influxdb.v3.client.Point;
import com.meichel.kafka_event.EnergyConsumedEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsageService {

    InfluxDBClient influxDBClient;

    public UsageService(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    @KafkaListener(topics = "energy-consumption", groupId = "usage-service-group")
    public void consume(EnergyConsumedEvent event) {
        log.info("Consumed event from Kafka: {}", event);

        Point point = Point.measurement("consumption")
                .setTag("device_id", event.deviceId().toString())
                .setField("energy_consumed", event.energyConsumed())
                .setTimestamp(event.timestamp().toInstant(ZoneOffset.UTC));
        try {
            influxDBClient.writePoint(point);
            log.info("Data is written to the influxDB: ", event.toString());
        } catch (Exception e) {
            log.error("Failed to write data to the influxDB: ", event.toString());
        }

    }

}
