package com.meichel.usage_service.controller;

import java.time.temporal.ChronoUnit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.meichel.usage_service.dto.UserUsageResponse;
import com.meichel.usage_service.service.UsageService;

@RestController
@RequestMapping("/api/v1/usage")
public class UsageController {

    UsageService usageService;

    public UsageController(UsageService usageService) {
        this.usageService = usageService;
    }

    @GetMapping("/{userId}")
    public UserUsageResponse getUserUsage(
            @PathVariable Long userId,
            @RequestParam long timeInterval,
            @RequestParam String timeUnit) {
        ChronoUnit unit = ChronoUnit.valueOf(timeUnit.trim().toUpperCase());
        return usageService.getUserDevicesUsage(userId, timeInterval, unit);
    }

}
