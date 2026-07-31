package com.meichel.usage_service.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.influxdb.v3.client.InfluxDBClient;
import com.influxdb.v3.client.Point;
import com.meichel.kafka_event.EnergyConsumedEvent;
import com.meichel.kafka_event.UsageAlertEvent;
import com.meichel.usage_service.client.DeviceClient;
import com.meichel.usage_service.client.UserClient;
import com.meichel.usage_service.dto.DeviceEnergyUsage;
import com.meichel.usage_service.dto.DeviceResponse;
import com.meichel.usage_service.dto.UserResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsageService {

    private InfluxDBClient influxDBClient;

    private DeviceClient deviceClient;
    private UserClient userClient;

    private final KafkaTemplate<String, UsageAlertEvent> kafkaTemplate;

    private String kafkaTopic = "usage-alerts";

    public UsageService(
        InfluxDBClient influxDBClient, 
        DeviceClient deviceClient, 
        UserClient userClient,
        KafkaTemplate<String, UsageAlertEvent> kafkaTemplate
    ) {
        this.influxDBClient = influxDBClient;
        this.deviceClient = deviceClient;
        this.userClient = userClient;
        this.kafkaTemplate = kafkaTemplate;
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
            log.info("Data is written to the influxDB: {}", event);
        } catch (Exception e) {
            log.error("Failed to write data to the influxDB: {}", event);
        }

    }

    @Scheduled(cron = "0 * * * * *")
    public void aggregateDeviceEnergyUsage() {

            ArrayList<DeviceEnergyUsage> devicesEnergies = new ArrayList<DeviceEnergyUsage>();

            String sql = """
                        SELECT device_id, SUM(energy_consumed) as total
                        FROM consumption
                        WHERE time >= now() - INTERVAL '1 hour'
                        GROUP BY device_id
                        """;
            try (Stream<Object[]> stream = influxDBClient.query(sql)) {
                log.info("energy usage aggregation recieved.");
                stream.forEach(row -> {
                    devicesEnergies.add(
                        DeviceEnergyUsage
                        .builder()
                        .DeviceId(Long.parseLong((String) row[0]))
                        .EnergyConsumed((Double) row[1])
                        .build()
                    );
                });
            }
            catch (Exception e) {
                log.error("Failed to query data from the database: ");
                e.printStackTrace();
                return;
            }

            for (DeviceEnergyUsage device: devicesEnergies) {
                try {
                    DeviceResponse recievedDevice =  deviceClient.getDeviceById(device.getDeviceId());
                    device.setName(recievedDevice.getName());
                    device.setType(recievedDevice.getType());
                    device.setUserId(recievedDevice.getUserId());
                } catch (Exception e) {
                    log.warn("Failed to fetch device {}: {}", device.getDeviceId(), e.getMessage());
                    continue;
                }
                
            }

            Map<Long, List<DeviceEnergyUsage>> devicesByUser = devicesEnergies.stream()
            .filter(device -> device.getUserId() != null)
            .collect(Collectors.groupingBy(DeviceEnergyUsage::getUserId));

            Map<Long, Double> totalByUser = devicesByUser.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().stream()
                                    .mapToDouble(DeviceEnergyUsage::getEnergyConsumed)
                                    .sum()
                    ));

            for (Map.Entry<Long, Double> entry : totalByUser.entrySet()) {
                Long userId = entry.getKey();
                Double total = entry.getValue();
            
                UserResponse user;
                try {
                    user = userClient.getUserById(userId);
                } catch (Exception e) {
                    log.warn("Failed to fetch user {}: {}", userId, e.getMessage());
                    continue;
                }
            
                if (user.enableAlerting() && total > user.alertingThreshold()) {
                    UsageAlertEvent event = UsageAlertEvent.builder()
                            .userId(userId)
                            .email(user.email())
                            .totalConsumed(total)
                            .threshold(user.alertingThreshold())
                            .devices(devicesByUser.get(userId))
                            .timestamp(LocalDateTime.now())
                            .build();

                    try {
                        kafkaTemplate.send(kafkaTopic, event);
                        log.info("Event {} sent to topic: {}", event, kafkaTopic);
                    } catch (Exception e) {
                        log.error("Failed to send event to topic {}: {}", event, kafkaTopic);
                        continue;
                    }
    
                    
                }
            }

    } 

}
