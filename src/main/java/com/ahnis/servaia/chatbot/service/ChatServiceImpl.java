package com.ahnis.servaia.chatbot.service;

import com.ahnis.servaia.chatbot.dto.ChatRequest;
import com.ahnis.servaia.chatbot.dto.ChatResponse;
import com.ahnis.servaia.chatbot.dto.ChatStreamRequest;
import com.ahnis.servaia.chatbot.tools.ChatbotTools;
import com.ahnis.servaia.user.entity.User;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * The primary implementation of the {@link ChatService} interface.
 * This service handles synchronous and asynchronous (streaming) chat interactions
 * with the user, leveraging various advisors to provide personalized and context-aware responses.
 * <p>
 * The service uses the following advisors:
 * <ul>
 *   <li>{@link QuestionAnswerAdvisor}: Provides responses based on the user's journal entries and preferences.</li>
 *   <li>{@link MessageChatMemoryAdvisor}: Manages short-term conversation memory for context-aware interactions.</li>
 * </ul>
 * </p>
 * <p>
 * The service also uses predefined prompt templates for system messages and user interactions,
 * which are loaded from external resources.
 * </p>
 *
 * <p>
 * Written by: Ahnis Singh
 * </p>
 */
@Service
public class ChatServiceImpl implements ChatService {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("classpath:/templates/chatbot/system-template.st")
    private Resource systemMessageResource;
    @Value("classpath:templates/chatbot/chatbot-template.st")
    private Resource userChatbotPromptTemplateResource;

    /**
     * Constructs a new instance of {@link ChatServiceImpl}.
     *
     * @param chatClient  The {@link ChatClient.Builder} used to build the chat client.
     * @param chatMemory  The {@link ChatMemory} used for short-term conversation memory.
     * @param vectorStore The {@link VectorStore} used for long-term conversation memory.
     */

    public ChatServiceImpl(ChatClient.Builder chatClient, ChatMemory chatMemory, VectorStore vectorStore, ChatbotTools chatbotTools) {
        this.chatClient = chatClient.defaultAdvisors(List.of(
                        new MessageChatMemoryAdvisor(chatMemory)
                ))
                .defaultTools(chatbotTools)
                .build();
        this.vectorStore = vectorStore;
    }

    /**
     * Handles synchronous chat interactions with the user.
     *
     * @param user        The {@link User} initiating the chat.
     * @param chatRequest The {@link ChatRequest} containing the user's message and conversation ID.
     * @return A {@link ChatResponse} containing the chatbot's response and the conversation ID.
     * @throws SecurityException If the conversation ID is invalid for the given user.
     */
    public ChatResponse chatSync(User user, ChatRequest chatRequest) {
        var userId = user.getId();
        var conversationId = chatRequest.conversationId();
        if (conversationId == null || conversationId.isEmpty()) conversationId = createConversation(userId);
        else if (!isValidConversation(userId, conversationId)) throw new SecurityException("Invalid user id");

        Prompt userChatbotPrompt = createUserChatBotPrompt(user, chatRequest.message());

        var response = chatClient
                .prompt(userChatbotPrompt)
                .system(systemMessageResource)
                .advisors(advisorSpecification(userId, chatRequest.message(), conversationId))
                .call()
                .content();
        return new ChatResponse(conversationId, response);
    }

    /**
     * Handles asynchronous (streaming) chat interactions with the user.
     *
     * @param chatRequest The {@link ChatStreamRequest} containing the user's message and chat ID.
     * @param chatId      The unique ID of the chat session.
     * @param user        The {@link User} initiating the chat.
     * @return A {@link Flux} of strings representing the chatbot's streaming response.
     * @throws SecurityException If the chat ID is invalid for the given user.
     */
    public Flux<String> chatFlux(ChatStreamRequest chatRequest, String chatId, User user) {
        var userId = user.getId();
        if (chatId == null) chatId = createConversation(userId);
        else if (!isValidConversation(userId, chatId)) throw new SecurityException("Invalid chat id for user ");

        Prompt userChatbotPrompt = createUserChatBotPrompt(user, chatRequest.message());

        return chatClient
                .prompt(userChatbotPrompt)
                .system(systemMessageResource)
                .advisors(advisorSpecification(userId, chatRequest.message(), chatId))
                .stream()
                .content()
                .onBackpressureBuffer();
    }

    /**
     * Configures the advisors for the chat client, including the {@link QuestionAnswerAdvisor} and {@link MessageChatMemoryAdvisor}.
     * This method sets up the necessary parameters for the advisors to provide personalized and context-aware responses.
     *
     * <p>
     * The {@link QuestionAnswerAdvisor} is configured with a {@link SearchRequest} that filters journals by the user's ID,
     * limits the results to the top K relevant journals, and uses the user's message as the query for semantic search.
     * </p>
     *
     * <p>
     * The {@link MessageChatMemoryAdvisor} is configured with the conversation ID and retrieve size to manage short-term
     * conversation memory for context-aware interactions.
     * </p>
     *
     * @param userId         The unique ID of the user, used to filter journals by the user's ID.
     * @param message        The user's message, used as the query for semantic search in the {@link QuestionAnswerAdvisor}.
     * @param conversationId The unique ID of the conversation, used to manage short-term memory in the {@link MessageChatMemoryAdvisor}.
     * @return A {@link Consumer} that configures the {@link ChatClient.AdvisorSpec} with the necessary advisors and parameters.
     */
    private Consumer<ChatClient.AdvisorSpec> advisorSpecification(String userId, String message, String conversationId) {
        return advisorSpec -> advisorSpec
                .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder()
                        .filterExpression("userId == '" + userId + "'")
                        .topK(3)
                        .query(message)
                        .build()))
                .param(MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId)
                .param(MessageChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10);

    }

    /**
     * Creates a {@link Prompt} for the chatbot based on the user's preferences and message.
     *
     * @param user               The {@link User} initiating the chat.
     * @param chatRequestMessage The user's message.
     * @return A {@link Prompt} containing the user's preferences and message.
     */
    private Prompt createUserChatBotPrompt(User user, String chatRequestMessage) {
        var userChatbotPromptTemplate = new PromptTemplate(userChatbotPromptTemplateResource);
        var userPreferences = user.getPreferences();
        Map<String, Object> userPreferencesMap = new LinkedHashMap<>();
        userPreferencesMap.put("supportStyle", userPreferences.getSupportStyle().toString());
        userPreferencesMap.put("supportStyleDescription", userPreferences.getSupportStyle().getDescription());
        userPreferencesMap.put("username", user.getUsername());
        userPreferencesMap.put("language", userPreferences.getLanguage());
        userPreferencesMap.put("userAge", userPreferences.getAge());
        userPreferencesMap.put("userGender", userPreferences.getGender());
        userPreferencesMap.put("timeZone", user.getTimezone());
        userPreferencesMap.put("userMessage", chatRequestMessage);
        return userChatbotPromptTemplate.create(userPreferencesMap);
    }

    /**
     * Generates a new conversation ID in the format "userId:conversationId".
     *
     * @param userId The unique ID of the user.
     * @return A new conversation ID.
     */
    private String createConversation(String userId) {
        return userId + ":" + UUID.randomUUID();
    }

    /**
     * Validates that the conversation ID belongs to the given user.
     *
     * @param userId         The unique ID of the user.
     * @param conversationId The conversation ID to validate.
     * @return {@code true} if the conversation ID is valid for the user, {@code false} otherwise.
     */
    private boolean isValidConversation(String userId, String conversationId) {
        return conversationId.startsWith(userId + ":");
    }
}
