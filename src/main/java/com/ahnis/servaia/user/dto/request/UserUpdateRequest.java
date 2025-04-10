package com.ahnis.servaia.user.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

//todo migrate validation properties to config
public record UserUpdateRequest(
        @Email(message = "Invalid email format")
        String email,

        @Size(min = 8, message = "Password must be at least 8 characters")
        String password,

        @Valid
        PreferencesRequest preferences
) {}

