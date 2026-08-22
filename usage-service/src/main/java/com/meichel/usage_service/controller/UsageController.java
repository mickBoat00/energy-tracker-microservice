package com.meichel.usage_service.controller;

import java.time.temporal.ChronoUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<UserUsageResponse> getUserUsage(
            @RequestHeader(value = "X-User-Sub", required = false) String userSub,
            @RequestParam long timeInterval,
            @RequestParam String timeUnit) {

            if (userSub == null || userSub.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        
        ChronoUnit unit = ChronoUnit.valueOf(timeUnit.trim().toUpperCase());
        return ResponseEntity.status(HttpStatus.OK).body(usageService.getUserDevicesUsage(userSub, timeInterval, unit));
    }

}
