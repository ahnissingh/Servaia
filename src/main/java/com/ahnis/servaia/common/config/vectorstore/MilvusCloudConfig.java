package com.ahnis.servaia.common.config.vectorstore;

import com.ahnis.servaia.common.config.vectorstore.properties.MilvusCloudProperties;
import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up a Milvus cloud client.
 * <p>
 * This class defines a Spring bean for {@link MilvusServiceClient}, which is used to connect to a Milvus cloud instance.
 * The connection parameters are derived from the {@link MilvusCloudProperties} class.
 * </p>
 *
 * @author Ahnis Singh Aneja
 */
@Configuration
public class MilvusCloudConfig {

    /**
     * Creates and configures a {@link MilvusServiceClient} bean.
     *
     * @param cloudProperties The {@link MilvusCloudProperties} containing the connection details.
     * @return A configured {@link MilvusServiceClient} instance.
     */
    @Bean
    MilvusServiceClient milvusClient(MilvusCloudProperties cloudProperties) {
        return new MilvusServiceClient(
                ConnectParam.newBuilder()
                        .withUri(cloudProperties.getUri())
                        .withToken(cloudProperties.getToken())
                        .withAuthorization(cloudProperties.getUsername(), cloudProperties.getPassword())
                        .build()
        );
    }
}
