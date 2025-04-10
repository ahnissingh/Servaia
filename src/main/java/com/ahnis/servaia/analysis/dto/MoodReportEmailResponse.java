package com.ahnis.servaia.analysis.dto;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.List;
import java.util.Map;

public record MoodReportEmailResponse(
        @JsonPropertyDescription("Summary of the user's mood based on journal entries, highlighting the dominant emotional tone.")
        String moodSummary,

        @JsonPropertyDescription("Key emotions detected in the journal entries, along with their corresponding percentages.")
        Map<String, String> keyEmotions,

        @JsonPropertyDescription("Insights derived from the analysis of the journal entries, providing context on the user's emotional state.")
        List<String> insights,

        @JsonPropertyDescription("Actionable recommendations based on the emotional analysis, offering suggestions for mood improvement or emotional management.")
        List<String> recommendations,

        @JsonPropertyDescription("A relevant literature quote that aligns with the user's emotional state, offering support or reflection.")
        String quote
) {
}
