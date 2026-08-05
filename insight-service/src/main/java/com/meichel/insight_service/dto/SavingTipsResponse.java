package com.meichel.insight_service.dto;

import java.util.List;

record SavingTip(String title, String description) {}

public record SavingTipsResponse(String summary, List<SavingTip> tips) {}