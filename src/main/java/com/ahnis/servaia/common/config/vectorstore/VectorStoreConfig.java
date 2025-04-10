package com.ahnis.servaia.common.config.vectorstore;

import com.ahnis.servaia.common.config.vectorstore.properties.VectorStoreProperties;
import io.milvus.client.MilvusServiceClient;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up a vector store.
 * <p>
 * This class defines a Spring bean for {@link VectorStore}, which is used to store and retrieve embeddings.
 * The vector store is configured using properties from {@link VectorStoreProperties} and a Milvus client.
 * </p>
 *
 * @author Ahnis Singh Aneja
 */
@Configuration
public class VectorStoreConfig {


    /**
     * Creates and configures a {@link VectorStore} bean.
     *
     * @param milvusServiceClient   The {@link MilvusServiceClient} used to connect to the Milvus instance.
     * @param embeddingModel        The {@link EmbeddingModel} used to generate embeddings.
     * @param vectorStoreProperties The {@link VectorStoreProperties} containing the vector store configuration.
     * @return A configured {@link VectorStore} instance.
     */
    @Bean
    public VectorStore vectorStore(MilvusServiceClient milvusServiceClient,
                                   EmbeddingModel embeddingModel,
                                   VectorStoreProperties vectorStoreProperties) {
        return MilvusVectorStore
                .builder(milvusServiceClient, embeddingModel)
                .databaseName(vectorStoreProperties.getDatabaseName())
                .collectionName(vectorStoreProperties.getCollectionName())
                .metricType(MetricType.valueOf(vectorStoreProperties.getMetricType()))
                .indexType(IndexType.valueOf(vectorStoreProperties.getIndexType()))
                .embeddingDimension(vectorStoreProperties.getEmbeddingDimension())
                .initializeSchema(vectorStoreProperties.isInitializeSchema())
                .build();
    }
}
