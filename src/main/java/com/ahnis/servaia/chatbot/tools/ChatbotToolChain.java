package com.ahnis.servaia.chatbot.tools;

import com.ahnis.servaia.notification.service.NotificationService;
import com.ahnis.servaia.user.entity.User;
import com.ahnis.servaia.user.exception.UserNotFoundException;
import com.ahnis.servaia.user.repository.TherapistRepository;
import com.ahnis.servaia.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class ChatbotToolChain {

    private final ChatClient chatClient;
    private final TherapistRepository therapistRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public ChatbotToolChain(ChatClient.Builder chatClient, TherapistRepository therapistRepository, NotificationService notificationService, UserRepository userRepository) {
        this.chatClient = chatClient.defaultOptions(ChatOptions.builder()
                .model("gpt-4o-mini").build()).build();
        this.therapistRepository = therapistRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    public record SuicideDetectionResult(boolean suicidalTendenciesDetected) {
    }

    @Tool(name = "AssessSuicideRisk", description = "Evaluates messages for suicide risk and triggers notifications when detected", returnDirect = true)
    public String handlePotentialSuicideRisk(
            @ToolParam() String userMessage,
            @AuthenticationPrincipal User user) {

        SuicideDetectionResult result = detectRisk(userMessage);
        log.info("Chain Result {}", result);
        if (!result.suicidalTendenciesDetected()) {
            return "I'm here to support you. Would you like to talk more about how you're feeling?";
        }
        log.info("User therapist when tool call {} ", user.getTherapistId());

        try {
            log.info(user.getUsername());
            notificationService.sendSuicidalAlert(user.getUsername(), user.getTherapistId(), "Sample concerning Concern");
            return "I'm very concerned about what you're sharing. Your therapist has been notified and will reach out to you shortly.";
        } catch (Exception e) {
            log.error("Failed to process suicide risk alert", e);
            return "I'm very concerned about what you're sharing. Please stay safe while we look into connecting you with support.";
        }
    }

    private SuicideDetectionResult detectRisk(String userMessage) {
        BeanOutputConverter<SuicideDetectionResult> outputConverter =
                new BeanOutputConverter<>(SuicideDetectionResult.class);

        String promptTemplate = """
                Analyze this message strictly for suicidal tendencies.
                Respond ONLY with RFC8259 compliant JSON
                Rules:
                1. No other fields or explanations
                2. Must match this exact structure
                3. Default to true if any risk exists
                4. Never include schema references
                Message: {message}
                Format: {format}
                """;

        Prompt prompt = new PromptTemplate(promptTemplate)
                .create(Map.of(
                        "message", userMessage,
                        "format", outputConverter.getFormat()
                ));

        String jsonResponse = chatClient.prompt(prompt).call().content();

        try {
            return outputConverter.convert(jsonResponse);
        } catch (Exception e) {
            log.error("Failed to parse suicide risk response: {}", jsonResponse, e);
            return new SuicideDetectionResult(true); // Fail-safe
        }
    }
}
