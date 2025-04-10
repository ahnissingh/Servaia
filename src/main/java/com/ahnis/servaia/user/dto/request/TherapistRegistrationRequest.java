package com.ahnis.servaia.user.dto.request;

import com.ahnis.servaia.user.enums.Language;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.util.Set;

public record TherapistRegistrationRequest(
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, max = 20, message = "Username must be 3-20 characters")
        String username,
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "First name cannot be blank")

        String firstName,

        @NotBlank(message = "Last Name cannot be blank")
        String lastName,
        @Min(0)
        int yearsOfExperience,
        @Size(max = 500)
        String bio,
        @NotEmpty Set<Language> spokenLanguages,
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        @NotBlank(message = "License number cannot be blank")
        String licenseNumber,

        @NotEmpty(message = "Specialties cannot be empty")
        Set<String> specialties,
        @URL
        String profilePictureUrl
) {
}
