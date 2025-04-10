package com.ahnis.servaia.user.config;


import com.ahnis.servaia.user.entity.Preferences;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "admin.user")
@Validated
public record AdminUserProperties(
        @NotNull(message = "Admin enabled flag must be specified")
        boolean enabled,

        @NotBlank(message = "Admin username must not be blank")
        @Size(min = 3, max = 20, message = "Admin username must be 3-20 characters")
        String username,

        @NotBlank(message = "Admin email must not be blank")
        @Email(message = "Admin email must be valid")
        String email,

        @NotBlank(message = "Admin password must not be blank")
        @Size(min = 3, message = "Admin password must be at least 8 characters")
        String password,

        @NotNull(message = "Preferences are required")
        Preferences preferences
) {
}
