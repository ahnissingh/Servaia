package com.ahnis.servaia.chatbot.dto;

/**
 * A record representing the chatbot's response to a user's chat request.
 * <p>
 * This record encapsulates the conversation ID and the chatbot's response.
 * It is used to return the result of a synchronous chat interaction.
 * </p>
 *
 * @param conversationId The unique ID of the conversation associated with this response.
 * @param response       The chatbot's response to the user's message.
 *
 * @author Ahnis Singh Aneja
 */
public record ChatResponse(
        String conversationId, // The conversation ID for this chat session
        String response        // The chatbot's response
) {
}
