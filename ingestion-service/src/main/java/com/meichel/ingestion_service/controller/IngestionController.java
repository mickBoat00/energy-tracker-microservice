package com.meichel.ingestion_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meichel.ingestion_service.dto.EnergyConsumedDto;
import com.meichel.ingestion_service.service.IngestionService;

@RestController
@RequestMapping("/api/v1/ingestion")
public class IngestionController {

    private IngestionService ingestionService;

    public IngestionController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/")
    public EnergyConsumedDto ingestEnergyConsumption(@RequestBody EnergyConsumedDto request) {
        return ingestionService.ingestEnergyConsumption(request);
    }

}
