package com.ahnis.servaia.chatbot.controller;


import com.ahnis.servaia.chatbot.dto.ChatRequest;
import com.ahnis.servaia.chatbot.dto.ChatResponse;
import com.ahnis.servaia.chatbot.dto.ChatStreamRequest;
import com.ahnis.servaia.chatbot.service.ChatService;
import com.ahnis.servaia.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * The REST controller for handling chat interactions with the chatbot.
 * <p>
 * This controller provides two endpoints:
 * <ul>
 *   <li>{@code /api/v1/chat}: For synchronous chat interactions.</li>
 *   <li>{@code /api/v1/chat/c/{chatId}}: For asynchronous (streaming) chat interactions.</li>
 * </ul>
 * Both endpoints require the user to be authenticated, and the user's details are automatically
 * injected using Spring Security's {@link AuthenticationPrincipal}.
 * </p>
 *
 * @author Ahnis Singh Aneja
 */
@RestController
@RequestMapping("/api/v1/chat")
@Slf4j
@RequiredArgsConstructor
public class ChatBotController {
    private final ChatService chatService;

    /**
     * Handles synchronous chat interactions with the user.
     * <p>
     * This endpoint processes the user's message and returns a single response.
     * If the request does not include a conversation ID, a new conversation is created.
     * </p>
     *
     * @param chatRequest The {@link ChatRequest} containing the user's message and optional conversation ID.
     * @param user        The authenticated user, automatically injected by Spring Security.
     * @return A {@link ChatResponse} containing the chatbot's response and the conversation ID.
     */
    @PostMapping()
    public ChatResponse chat(
            @RequestBody ChatRequest chatRequest,
            @AuthenticationPrincipal User user
    ) {
        return chatService.chatSync(user, chatRequest);
    }

    /**
     * Handles asynchronous (streaming) chat interactions with the user.
     * <p>
     * This endpoint processes the user's message and returns a stream of responses.
     * The conversation ID is provided as a path variable.
     * </p>
     *
     * @param chatId      The unique ID of the chat session, provided as a path variable.
     * @param chatRequest The {@link ChatStreamRequest} containing the user's message.
     * @param user        The authenticated user, automatically injected by Spring Security.
     * @return A {@link Flux} of strings representing the chatbot's streaming response.
     */
    @PostMapping(value = "/c/{chatId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public Flux<String> chatStream(@PathVariable(required = false) String chatId,
                                   @RequestBody ChatStreamRequest chatRequest,
                                   @AuthenticationPrincipal User user) {
        return chatService.chatFlux(chatRequest, chatId, user);
    }
}

