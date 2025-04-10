package com.ahnis.servaia.analysis.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * This represents a user's report which will exposed via the rest api
 * to the ui client.
 *
 * @param reportDate
 * @param moodSummary
 * @param keyEmotions
 * @param insights
 * @param recommendations
 * @param quote
 * @param createdAt
 */
public record MoodReportApiResponse(
        String reportId,
        Instant reportDate,
        String moodSummary,
        Map<String, String> keyEmotions,
        List<String> insights,
        List<String> recommendations,
        String quote,

        Instant createdAt
) {
}
