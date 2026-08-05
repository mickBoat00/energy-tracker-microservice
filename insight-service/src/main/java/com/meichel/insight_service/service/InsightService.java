package com.meichel.insight_service.service;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.meichel.insight_service.client.UsageClient;
import com.meichel.insight_service.dto.SavingTipsResponse;
import com.meichel.insight_service.dto.UserUsageResponse;
import com.meichel.insight_service.tools.InsightAITool;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InsightService {

    private final UsageClient usageClient;
    private final InsightAITool insightAITool;
    private final ChatClient chatClient;

    public InsightService(
        UsageClient usageClient,
        GoogleGenAiChatModel chatModel,
        InsightAITool insightAITool
    ) {
        this.usageClient = usageClient;
        this.insightAITool = insightAITool;
        this.chatClient = ChatClient.create(chatModel);
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

    public List<SavingTipsResponse> userSavingTips(long userId) {
        String prompt = """
        Looking at user %s's energy consumption across multiple devices,
        write a short conversational summary of their recent usage
        (mention the total or notable figures), then say something like
        "here are some energy saving tips" before listing the tips.
        """.formatted(userId);

        try {
            return chatClient
                .prompt(prompt)
                .tools(insightAITool)
                .call()
                .entity(new ParameterizedTypeReference<List<SavingTipsResponse>>() {});
        } catch (Exception e) {
            log.error("failed to generate saving tips for user {}", userId, e);
            throw e;
        }
    }
}