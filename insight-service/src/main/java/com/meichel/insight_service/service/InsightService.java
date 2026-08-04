package com.meichel.insight_service.service;

import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.meichel.insight_service.client.UsageClient;
import com.meichel.insight_service.dto.UserUsageResponse;

@Service
public class InsightService {

    private static final Logger log = LoggerFactory.getLogger(InsightService.class);

    UsageClient usageClient;

    public InsightService(UsageClient usageClient) {
        this.usageClient = usageClient;
    }

    public UserUsageResponse userUsageInsightOverview(Long userId, long interval, ChronoUnit intervalUnit) {
        log.info("received request for user {} over {} {}", userId, interval, intervalUnit);
        try {
            return usageClient.getUserOverview(userId, interval, intervalUnit);
        } catch (Exception e) {
            log.error("failed to fetch usage overview for user {}", userId, e);
            throw e;
        }
    }

}
