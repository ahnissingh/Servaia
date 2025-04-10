package com.ahnis.servaia.analysis.service;

import com.ahnis.servaia.analysis.dto.MoodReportEmailResponse;
import com.ahnis.servaia.user.entity.Preferences;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public interface JournalAnalysisService {
    CompletableFuture<MoodReportEmailResponse> analyzeUserMood(String userId, String username, Preferences userPreferences, Instant startDate, Instant endDate);
}
