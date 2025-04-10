package com.ahnis.servaia.common.config.vectorstore.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for connecting to a Milvus cloud instance.
 * <p>
 * This class encapsulates the necessary properties to establish a connection to a Milvus cloud service,
 * including the URI, authentication token, username, and password.
 * </p>
 *
 * @author Ahnis Singh Aneja
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "milvus.cloud")
public class MilvusCloudProperties {

    /**
     * The URI of the Milvus cloud instance.
     */
    private String uri;

    /**
     * The authentication token for accessing the Milvus cloud instance.
     */
    private String token;

    /**
     * The username for authenticating with the Milvus cloud instance.
     */
    private String username;

    /**
     * The password for authenticating with the Milvus cloud instance.
     */
    private String password;
}
