package com.ahnis.servaia.chatbot.dto;

/**
 * A record representing a streaming chat request from the user.
 * <p>
 * This record is used to encapsulate the user's message for asynchronous (streaming) chat interactions.
 * Unlike {@link ChatRequest}, it does not include a conversation ID, as the ID is typically provided
 * via the API path.
 * </p>
 *
 * @param message The user's message to be processed by the chatbot.
 *
 * @author Ahnis Singh Aneja
 */
public record ChatStreamRequest(String message) {
}
