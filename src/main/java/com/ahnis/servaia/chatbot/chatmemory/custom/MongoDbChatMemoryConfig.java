package com.ahnis.servaia.chatbot.chatmemory.custom;


import com.google.common.base.Preconditions;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * Configuration for the MongoDB Chat Memory store.
 *
 * @author Ahnis Singh Aneja
 * @since 1.0.0
 */
public final class MongoDbChatMemoryConfig {

    public static final String DEFAULT_DATABASE_NAME = "seravia_db";

    public static final String DEFAULT_COLLECTION_NAME = "seravia_chat_memory";

    public static final String DEFAULT_SESSION_ID_NAME = "session_id";

    public static final String DEFAULT_EXCHANGE_ID_NAME = "message_timestamp";

    public static final String DEFAULT_ASSISTANT_COLUMN_NAME = "assistant";

    public static final String DEFAULT_USER_COLUMN_NAME = "user";

    private static final Logger logger = LoggerFactory.getLogger(MongoDbChatMemoryConfig.class);

    final MongoClient mongoClient;

    final Schema schema;

    final String assistantColumn;

    final String userColumn;

    final SessionIdToPrimaryKeysTranslator primaryKeyTranslator;

    private final Integer timeToLiveSeconds;

    private final boolean disallowSchemaChanges;

    private MongoDbChatMemoryConfig(Builder builder) {
        this.mongoClient = builder.mongoClient;
        this.schema = new Schema(builder.database, builder.collection, builder.partitionKeys, builder.clusteringKeys);
        this.assistantColumn = builder.assistantColumn;
        this.userColumn = builder.userColumn;
        this.timeToLiveSeconds = builder.timeToLiveSeconds;
        this.disallowSchemaChanges = builder.disallowSchemaChanges;
        this.primaryKeyTranslator = builder.primaryKeyTranslator;
    }

    public static Builder builder() {
        return new Builder();
    }

    SchemaColumn getPrimaryKeyColumn(int index) {
        return index < this.schema.partitionKeys().size() ? this.schema.partitionKeys().get(index)
                : this.schema.clusteringKeys().get(index - this.schema.partitionKeys().size());
    }

    MongoCollection<Document> getCollection() {
        return this.mongoClient.getDatabase(this.schema.database).getCollection(this.schema.collection);
    }

    void ensureSchemaExists() {
        if (!this.disallowSchemaChanges) {
            MongoDatabase database = this.mongoClient.getDatabase(this.schema.database);
            if (!database.listCollectionNames().into(new ArrayList<>()).contains(this.schema.collection)) {
                database.createCollection(this.schema.collection);
            }
        }
    }

    /**
     * Given a string sessionId, return the value for each primary key column.
     */
    public interface SessionIdToPrimaryKeysTranslator extends Function<String, List<Object>> {
    }

    record Schema(String database, String collection, List<SchemaColumn> partitionKeys,
                  List<SchemaColumn> clusteringKeys) {
    }

    public record SchemaColumn(String name, String type) {
    }

    public static final class Builder {

        private MongoClient mongoClient = null;

        private String database = DEFAULT_DATABASE_NAME;

        private String collection = DEFAULT_COLLECTION_NAME;

        private List<SchemaColumn> partitionKeys = List.of(new SchemaColumn(DEFAULT_SESSION_ID_NAME, "string"));

        private List<SchemaColumn> clusteringKeys = List.of(new SchemaColumn(DEFAULT_EXCHANGE_ID_NAME, "timestamp"));

        private String assistantColumn = DEFAULT_ASSISTANT_COLUMN_NAME;

        private String userColumn = DEFAULT_USER_COLUMN_NAME;

        private Integer timeToLiveSeconds = null;

        private boolean disallowSchemaChanges = false;

        private SessionIdToPrimaryKeysTranslator primaryKeyTranslator = List::of;

        private Builder() {
        }

        public Builder withMongoClient(MongoClient mongoClient) {
            this.mongoClient = mongoClient;
            return this;
        }

        public Builder withDatabaseName(String database) {
            this.database = database;
            return this;
        }

        public Builder withCollectionName(String collection) {
            this.collection = collection;
            return this;
        }

        public Builder withPartitionKeys(List<SchemaColumn> partitionKeys) {
            Preconditions.checkArgument(!partitionKeys.isEmpty());
            this.partitionKeys = partitionKeys;
            return this;
        }

        public Builder withClusteringKeys(List<SchemaColumn> clusteringKeys) {
            Preconditions.checkArgument(!clusteringKeys.isEmpty());
            this.clusteringKeys = clusteringKeys;
            return this;
        }

        public Builder withAssistantColumnName(String name) {
            this.assistantColumn = name;
            return this;
        }

        public Builder withUserColumnName(String name) {
            this.userColumn = name;
            return this;
        }

        /**
         * How long are messages kept for
         */
        public Builder withTimeToLive(Duration timeToLive) {
            Preconditions.checkArgument(0 < timeToLive.getSeconds());
            this.timeToLiveSeconds = (int) timeToLive.toSeconds();
            return this;
        }

        public Builder disallowSchemaChanges() {
            this.disallowSchemaChanges = true;
            return this;
        }

        public Builder withChatExchangeToPrimaryKeyTranslator(SessionIdToPrimaryKeysTranslator primaryKeyTranslator) {
            this.primaryKeyTranslator = primaryKeyTranslator;
            return this;
        }

        public MongoDbChatMemoryConfig build() {
            Preconditions.checkNotNull(this.mongoClient, "MongoClient must be provided");

            int primaryKeyColumns = this.partitionKeys.size() + this.clusteringKeys.size();
            int primaryKeysToBind = this.primaryKeyTranslator.apply(UUID.randomUUID().toString()).size();

            Preconditions.checkArgument(primaryKeyColumns == primaryKeysToBind + 1,
                    "The primaryKeyTranslator must always return one less element than the number of primary keys in total. The last clustering key remains undefined, expecting to be the timestamp for messages within sessionId. The sessionId can map to any primary key column (though it should map to a partition key column).");

            Preconditions.checkArgument(
                    this.clusteringKeys.getLast().name().equals(DEFAULT_EXCHANGE_ID_NAME),
                    "last clustering key must be the exchangeIdColumn");

            return new MongoDbChatMemoryConfig(this);
        }
    }
}
