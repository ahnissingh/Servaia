package com.ahnis.servaia.notification.config;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "email.sendgrid.config")
@Validated
public record SendGridProperties(
        @NotBlank(message = "SendGrid API key must not be blank")
        String apiKey,
        @Email(message = "From email must be valid")
        @NotBlank(message = "From email must not be blank")
        String fromEmail
) {
}



