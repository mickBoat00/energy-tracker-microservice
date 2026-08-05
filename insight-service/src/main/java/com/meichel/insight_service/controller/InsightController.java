package com.meichel.insight_service.controller;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.meichel.insight_service.dto.SavingTipsResponse;
import com.meichel.insight_service.dto.UserUsageResponse;
import com.meichel.insight_service.service.InsightService;

@RestController
@RequestMapping("/api/v1/insights")
public class InsightController {

    InsightService insightService;

    public InsightController(InsightService insightService) {
        this.insightService = insightService;
    }

    @GetMapping("/overview/{userId}/")
    public UserUsageResponse userUsageOverview(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "7") long timeInterval,
            @RequestParam(defaultValue = "DAYS") String timeUnit) {
        ChronoUnit unit = ChronoUnit.valueOf(timeUnit.trim().toUpperCase());
        return insightService.userUsageInsightOverview(userId, timeInterval, unit);
    }

    @GetMapping("/saving-tips/{userId}/")
    public List<SavingTipsResponse> savingTipsUser(@PathVariable Long userId){
        return insightService.userSavingTips(userId);
    }

}
