package com.ahnis.servaia.chatbot.chatmemory.custom;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public final class UserAwareInMemoryChatMemory implements ChatMemory {

    // Structure: userId -> Map<conversationId, List<Message>>
    private final Map<String, Map<String, List<Message>>> userConversations = new ConcurrentHashMap<>();

    @Override
    public void add(String conversationId, List<Message> messages) {
        ParsedConversationId parsed = parseConversationId(conversationId);
        userConversations
                .computeIfAbsent(parsed.userId(), k -> new ConcurrentHashMap<>())
                .computeIfAbsent(parsed.conversationId(), k -> new ArrayList<>())
                .addAll(messages);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        ParsedConversationId parsed = parseConversationId(conversationId);
        Map<String, List<Message>> userConv = userConversations.get(parsed.userId());
        if (userConv == null) return List.of();

        List<Message> messages = userConv.get(parsed.conversationId());
        if (messages == null) return List.of();

        return messages.stream()
                .skip(Math.max(0, messages.size() - lastN))
                .toList();
    }

    @Override
    public void clear(String conversationId) {
        ParsedConversationId parsed = parseConversationId(conversationId);
        Map<String, List<Message>> userConv = userConversations.get(parsed.userId());
        if (userConv != null) {
            userConv.remove(parsed.conversationId());
        }
    }

    // Additional methods for conversation management
    public String createConversation(String userId) {
        String conversationId = UUID.randomUUID().toString();
        userConversations
                .computeIfAbsent(userId, k -> new ConcurrentHashMap<>())
                .put(conversationId, new ArrayList<>());
        return formatConversationId(userId, conversationId);
    }

    public boolean isValidConversation(String userId, String conversationId) {
        ParsedConversationId parsed = parseConversationId(conversationId);
        return parsed.userId().equals(userId) && userConversations
                .getOrDefault(userId, Collections.emptyMap())
                .containsKey(parsed.conversationId());
    }

    private static String formatConversationId(String userId, String conversationId) {
        return userId + ":" + conversationId;
    }

    private ParsedConversationId parseConversationId(String conversationId) {
        String[] parts = conversationId.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid conversationId format");
        }
        return new ParsedConversationId(parts[0], parts[1]);
    }

    private record ParsedConversationId(String userId, String conversationId) {
    }
}
