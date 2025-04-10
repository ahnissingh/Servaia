package com.ahnis.servaia.common.config.vectorstore.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for the vector store.
 * <p>
 * This class encapsulates the properties required to configure a vector store, including the database name,
 * collection name, metric type, index type, embedding dimension, and whether to initialize the schema.
 * </p>
 *
 * @author Ahnis Singh Aneja
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "vector.store")
public class VectorStoreProperties {

    /**
     * The name of the database used for the vector store.
     */
    private String databaseName;

    /**
     * The name of the collection used for the vector store.
     */
    private String collectionName;

    /**
     * The metric type used for similarity search in the vector store.
     */
    private String metricType;

    /**
     * The index type used for indexing vectors in the vector store.
     */
    private String indexType;

    /**
     * The dimension of the embeddings stored in the vector store.
     */
    private int embeddingDimension;

    /**
     * Whether to initialize the schema for the vector store.
     */
    private boolean initializeSchema;
}
