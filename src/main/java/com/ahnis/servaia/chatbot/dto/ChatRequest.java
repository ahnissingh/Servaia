package com.ahnis.servaia.chatbot.dto;

/**
 * A record representing a chat request from the user.
 * <p>
 * This record is used to encapsulate the user's message and an optional conversation ID.
 * If the conversation ID is not provided, a new conversation is created.
 * </p>
 *
 * @param conversationId The unique ID of the conversation. This field is optional.
 *                       If not provided, a new conversation ID will be generated.
 * @param message        The user's message to be processed by the chatbot.
 *
 * @author Ahnis Singh Aneja
 */
public record ChatRequest(
        String conversationId, // Optional: If not provided, a new conversation is created
        String message         // The user's message
) {
}
