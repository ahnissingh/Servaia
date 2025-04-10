package com.ahnis.servaia.common.security.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Configuration properties for security settings.
 * <p>
 * This class encapsulates the properties required to configure security-related settings, such as allowed origins for CORS.
 * The properties are loaded from the application configuration file using the prefix `security`.
 * </p>
 *
 * @author Ahnis Singh Aneja
 */
@Component
@ConfigurationProperties(prefix = "security")
@Getter
@Setter
public class SecurityProperties {

    /**
     * A list of allowed origins for CORS (Cross-Origin Resource Sharing).
     * <p>
     * This list must not be empty and specifies the domains that are allowed to access the application's resources.
     * </p>
     */
    @NotBlank(message = "Allowed origins must not be empty")
    private List<String> allowedOrigins;
}
