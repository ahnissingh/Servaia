package com.ahnis.servaia.chatbot.chatmemory.custom;

import com.google.common.base.Preconditions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public final class MongoDbChatMemory implements ChatMemory {

    public static final String CONVERSATION_TS = MongoDbChatMemory.class.getSimpleName() + "_message_timestamp";

    final MongoDbChatMemoryConfig conf;

    private final MongoCollection<Document> collection;

    public MongoDbChatMemory(MongoDbChatMemoryConfig config) {
        this.conf = config;
        this.conf.ensureSchemaExists();
        this.collection = this.conf.getCollection();
    }

    public static MongoDbChatMemory create(MongoDbChatMemoryConfig conf) {
        return new MongoDbChatMemory(conf);
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        final AtomicLong instantSeq = new AtomicLong(Instant.now().toEpochMilli());
        messages.forEach(msg -> {
            if (msg.getMetadata().containsKey(CONVERSATION_TS)) {
                msg.getMetadata().put(CONVERSATION_TS, Instant.ofEpochMilli(instantSeq.getAndIncrement()));
            }
            add(conversationId, msg);
        });
    }

    @Override
    public void add(String sessionId, Message msg) {

        Preconditions.checkArgument(
                !msg.getMetadata().containsKey(CONVERSATION_TS)
                        || msg.getMetadata().get(CONVERSATION_TS) instanceof Instant,
                "messages only accept metadata '%s' entries of type Instant", CONVERSATION_TS);

        msg.getMetadata().putIfAbsent(CONVERSATION_TS, Instant.now());

        Document document = new Document();
        List<Object> primaryKeys = this.conf.primaryKeyTranslator.apply(sessionId);

        for (int k = 0; k < primaryKeys.size(); ++k) {
            MongoDbChatMemoryConfig.SchemaColumn keyColumn = this.conf.getPrimaryKeyColumn(k);
            document.append(keyColumn.name(), primaryKeys.get(k));
        }

        Instant instant = (Instant) msg.getMetadata().get(CONVERSATION_TS);
        document.append(CONVERSATION_TS, instant)
                .append("message", msg.getText());

        if (msg instanceof UserMessage) {
            document.append("user", msg.getText());
        } else if (msg instanceof AssistantMessage) {
            document.append("assistant", msg.getText());
        }

        this.collection.insertOne(document);
    }

    @Override
    public void clear(String sessionId) {
        List<Object> primaryKeys = this.conf.primaryKeyTranslator.apply(sessionId);
        Bson filter = Filters.and(
                primaryKeys.stream()
                        .map(key -> Filters.eq(this.conf.getPrimaryKeyColumn(primaryKeys.indexOf(key)).name(), key))
                        .toArray(Bson[]::new));

        this.collection.deleteMany(filter);
    }

    @Override
    public List<Message> get(String sessionId, int lastN) {
        List<Object> primaryKeys = this.conf.primaryKeyTranslator.apply(sessionId);
        Bson filter = Filters.and(
                primaryKeys.stream()
                        .map(key -> Filters.eq(this.conf.getPrimaryKeyColumn(primaryKeys.indexOf(key)).name(), key))
                        .toArray(Bson[]::new));

        List<Message> messages = new ArrayList<>();
        for (Document doc : this.collection.find(filter).sort(new Document(CONVERSATION_TS, -1)).limit(lastN)) {
            String assistant = doc.getString("assistant");
            String user = doc.getString("user");
            if (assistant != null) {
                messages.add(new AssistantMessage(assistant));
            }
            if (user != null) {
                messages.add(new UserMessage(user));
            }
        }
        return messages;
    }
}
