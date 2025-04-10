package com.ahnis.servaia.chatbot.chatmemory.config;


import com.ahnis.servaia.chatbot.chatmemory.custom.MongoDbChatMemory;
import com.ahnis.servaia.chatbot.chatmemory.custom.MongoDbChatMemoryConfig;
import com.mongodb.client.MongoClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.cassandra.CassandraChatMemory;
import org.springframework.ai.chat.memory.cassandra.CassandraChatMemoryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Duration;

/**
 * Configuration class for setting up chat memory in the application.
 * <p>
 * This class defines a Spring bean for {@link ChatMemory}, which is used to store and retrieve
 * conversation history. The chat memory is backed by a MongoDB collection, configured using
 * {@link MongoDbChatMemory}.
 * </p>
 *
 * @author Ahnis Singh Aneja
 */
@Configuration
public class ChatMemoryConfig {

    /**
     * Creates and configures a {@link ChatMemory} bean backed by MongoDB.
     * <p>
     * The chat memory is configured with the following properties:
     * <ul>
     *   <li>MongoDB client: The provided {@link MongoClient} instance.</li>
     *   <li>Database name: "journal_ai".</li>
     *   <li>Collection name: "chat_memory".</li>
     *   <li>Time-to-live (TTL): 90 days.</li>
     * </ul>
     * </p>
     *
     * @param mongoClient The MongoDB client used to connect to the database.
     * @return A configured {@link ChatMemory} instance.
     */
//    @Bean
//
//    public ChatMemory chatMemory(MongoClient mongoClient) {
//        return MongoDbChatMemory.create(MongoDbChatMemoryConfig.builder()
//                .withMongoClient(mongoClient)
//                .withCollectionName("chat_memory")
//                .withDatabaseName("journal_ai")
//                .withTimeToLive(Duration.ofDays(90))
//                .build()
//        );
//    }


}
