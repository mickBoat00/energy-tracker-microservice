package com.meichel.insight_service.tools;

import java.time.temporal.ChronoUnit;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.meichel.insight_service.client.UsageClient;
import com.meichel.insight_service.dto.UserUsageResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InsightAITool {

    UsageClient usageClient;

    public InsightAITool(UsageClient usageClient){
        this.usageClient = usageClient;
    }

    @Tool(description = "Get user devices energy consumption for the past 7 days")
    public UserUsageResponse userDeviceEnergyConsumption(@ToolParam(description= "User id") Long userId) {
        try {
            ChronoUnit unit = ChronoUnit.valueOf("days".trim().toUpperCase());
            return usageClient.getUserOverview(userId,  7, unit);
        } catch (Exception e) {
            log.error("failed to fetch usage overview for user {}", userId, e);
            throw e;
        }
    }
}
