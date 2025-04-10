package com.ahnis.servaia.common.config.vectorstore;

import com.knuddels.jtokkit.api.EncodingType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.BatchingStrategy;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class BatchingStrategyConfig {
    /**
     * Creates a custom batching strategy for OpenAI embeddings.
     *
     * @return A {@link BatchingStrategy} instance configured for OpenAI embeddings.
     */
    @Bean
    @ConditionalOnProperty(name = "vector.batching.enabled", havingValue = "true", matchIfMissing = false)
    public BatchingStrategy CustomBatchingStrategyForOpenAi() {
        log.warn("Batching strategy is enabled");
        return new TokenCountBatchingStrategy(
                EncodingType.CL100K_BASE,
                8191,
                0.1
        );
    }
}
