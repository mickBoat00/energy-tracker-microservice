package com.meichel.ingestion_service.simulation;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.meichel.ingestion_service.dto.EnergyConsumedDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataSimulator implements CommandLineRunner {

    private RestClient restClient = RestClient.create();
    private final Random random = new Random();

    @Value("${request.per.interval}")
    int requestPerInterval;

    @Value("${ingestion.service.base.url}")
    String ingestionServiceBaseUrl;

    @Value("${schedule.interval.perSeconds}")
    int intervalInSeconds;

    @Override
    public void run(String... args) {
        log.info("DataSimulator started. Will send {} requests every {}s to {}",
                requestPerInterval, intervalInSeconds, ingestionServiceBaseUrl);
    }

    @Scheduled(fixedRateString = "${schedule.interval.perSeconds}000")
    private void ingestData() {

        for (int i = 0; i < requestPerInterval; i++) {
            EnergyConsumedDto payload = randomPayload();

            try {
                EnergyConsumedDto response = restClient.post()
                        .uri(ingestionServiceBaseUrl + "/api/v1/ingestion/")
                        .body(payload)
                        .retrieve()
                        .body(EnergyConsumedDto.class);

                log.debug("Ingested: {}", response);
            } catch (Exception e) {
                log.error("Failed to ingest data: {}", e.getMessage());
            }
        }
    }

    private EnergyConsumedDto randomPayload() {
        long deviceId = random.nextInt(6) + 1;      
        double kwhConsumed = 0.00 + (0.50 * random.nextDouble());

        return new EnergyConsumedDto(
                deviceId,
                kwhConsumed,
                LocalDateTime.now()
        );
    }
}