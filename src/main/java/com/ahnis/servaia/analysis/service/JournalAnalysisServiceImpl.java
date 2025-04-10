package com.ahnis.servaia.analysis.service;

import com.ahnis.servaia.analysis.dto.MoodReportEmailResponse;
import com.ahnis.servaia.user.entity.Preferences;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class JournalAnalysisServiceImpl implements JournalAnalysisService {
    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    @Async
    @Override
    public CompletableFuture<MoodReportEmailResponse> analyzeUserMood(String userId, String username, Preferences userPreferences, Instant startDate, Instant endDate) {
        List<Document> documents = Optional.ofNullable(vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query("mood")
                        .topK(3)
                        .filterExpression("userId == '" + userId + "' && createdAt >= '" + startDate + "' && createdAt <= '" + endDate + "'")
                        .build()
        )).orElse(Collections.emptyList());

        // Step 2: Extract the text content from the documents
        List<String> contentList = documents.stream()
                .map(Document::getText)
                .toList();

        // Step 3: Combine the content into a single string for the prompt
        var combinedContent = String.join("\n", contentList);

        // Step 4: Create a prompt to analyze the mood
        String promptText = generatePromptForUser(username, userPreferences, combinedContent);

        // Step 5: Send the prompt to the language model (e.g., OpenAI GPT)
        var response = chatModel.call(new Prompt(promptText));

        // Step 6: Parse the response into a MoodReport object
        BeanOutputConverter<MoodReportEmailResponse> outputConverter = new BeanOutputConverter<>(MoodReportEmailResponse.class);
        MoodReportEmailResponse moodReportEmailResponse = outputConverter.convert(response.getResult().getOutput().getText());

        // Step 7: Return completed future
        return CompletableFuture.completedFuture(moodReportEmailResponse);
    }

    private static String generatePromptForUser(String username, Preferences userPreferences, String combinedContent) {
        String promptTemplate = """
                Analyze the mood of the following journal entries and provide a summary.
                DO NOT JUDGE ANY OTHER EMOTIONS OTHER THAN ONLY ALLOWED EMOTIONS are happiness, sadness, anger, fear, surprise, and disgust.
                Include key emotions (as percentages as text), contextual insights, and recommendations.
                Your response should be in JSON format.
                The data structure for the JSON should match this Java class: %s
                Do not include any explanations, only provide a RFC8259 compliant JSON response following this format without deviation.
                Keep the language of the report %s and keep the tone for any text %s
                Do not address the user as 'user' but address them with their username %s use this name STRICTLY ONCE  in the report ONLY at the BEGINNING.
                Entries:
                %s
                """;

        String format = new BeanOutputConverter<>(MoodReportEmailResponse.class).getFormat();
        return String.format(promptTemplate,
                MoodReportEmailResponse.class.getName(),
                userPreferences.getLanguage().name(),
                userPreferences.getSupportStyle().name(),
                username,
                combinedContent
        ) + "\n" + format;

    }
}
