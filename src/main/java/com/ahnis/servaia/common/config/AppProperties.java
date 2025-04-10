package com.ahnis.servaia.common.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for the application.
 * <p>
 * This class encapsulates general application properties, such as the base URL and password reset URL.
 * </p>
 *
 * @author Ahnis Singh Aneja
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    /**
     * The base URL of the application.
     */
    private String baseUrl;

    /**
     * The URL for the password reset functionality.
     */
    private String passwordResetUrl;
}

