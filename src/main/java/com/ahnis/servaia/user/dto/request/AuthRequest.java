package com.ahnis.servaia.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        @NotBlank(message = "Username should not be empty") String usernameOrEmail,
        @Size(min = 3, message = "Password must be at least 3 characters")
        @NotBlank(message = "Password should not be empty") String password
) {}

