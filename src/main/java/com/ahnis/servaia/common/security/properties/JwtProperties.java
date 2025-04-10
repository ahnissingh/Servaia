package com.ahnis.servaia.common.security.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for JWT (JSON Web Token) settings.
 * <p>
 * This class encapsulates the properties required to configure JWT, including the secret key and expiration time.
 * The properties are loaded from the application configuration file using the prefix `jwt`.
 * </p>
 *
 * @author Ahnis Singh Aneja
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {

    /**
     * The secret key used to sign the JWT.
     * <p>
     * This value must not be blank and is typically a base64-encoded string.
     * </p>
     */
    @NotBlank(message = "Jwt Secret must not be blank")
    private String secret;

    /**
     * The expiration time of the JWT in milliseconds.
     * <p>
     * This value must be a positive number and determines how long the JWT remains valid.
     * </p>
     */
    @Positive(message = "Expiration of jwt must be positive")
    private Long expiration;
}
