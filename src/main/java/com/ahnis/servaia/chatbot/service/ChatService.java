package com.ahnis.servaia.chatbot.service;

import com.ahnis.servaia.chatbot.dto.ChatRequest;
import com.ahnis.servaia.chatbot.dto.ChatResponse;
import com.ahnis.servaia.chatbot.dto.ChatStreamRequest;
import reactor.core.publisher.Flux;

import com.ahnis.servaia.user.entity.User;
/**
 * The service interface for handling chat interactions with the chatbot.
 * <p>
 * This interface defines the contract for both synchronous and asynchronous (streaming) chat interactions.
 * Implementations of this interface are responsible for processing user messages, managing conversation state,
 * and generating responses using predefined templates and advisors.
 * </p>
 * <p>
 * The interface provides two main methods:
 * <ul>
 *   <li>{@link #chatSync(User, ChatRequest)}: For synchronous chat interactions.</li>
 *   <li>{@link #chatFlux(ChatStreamRequest, String chatId , User user)}: For asynchronous (streaming) chat interactions.</li>
 * </ul>
 * </p>
 *
 * @author Ahnis Singh Aneja
 */
public interface ChatService {

    /**
     * Handles synchronous chat interactions with the user.
     * <p>
     * This method processes the user's message and returns a single response. It is typically used for
     * non-streaming interactions where the chatbot's response is returned in its entirety.
     * </p>
     *
     * @param user    The {@link User} initiating the chat. Contains user-specific preferences and metadata.
     * @param request The {@link ChatRequest} containing the user's message and optional conversation ID.
     * @return A {@link ChatResponse} containing the chatbot's response and the conversation ID.
     */
    ChatResponse chatSync(User user, ChatRequest request);

    /**
     * Handles asynchronous (streaming) chat interactions with the user.
     * <p>
     * This method processes the user's message and returns a stream of responses. It is typically used for
     * real-time interactions where the chatbot's response is streamed to the client as it is generated.
     * </p>
     *
     * @param chatRequest The {@link ChatStreamRequest} containing the user's message.
     * @param chatId      The unique ID of the chat session. If not provided, a new conversation is created.
     * @param user        The {@link User} initiating the chat. Contains user-specific preferences and metadata.
     * @return A {@link Flux} of strings representing the chatbot's streaming response.
     */
    Flux<String> chatFlux(ChatStreamRequest chatRequest, String chatId, User user);
}
