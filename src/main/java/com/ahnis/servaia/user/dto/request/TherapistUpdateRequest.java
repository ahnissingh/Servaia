package com.ahnis.servaia.user.dto.request;

import com.ahnis.servaia.user.enums.Language;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.Set;

public record TherapistUpdateRequest(
        @Size(max = 500) String bio,
        @Size(min = 1) Set<String> specialties,
        @Size(min = 1) Set<Language> spokenLanguages,
        @Min(0) int yearsOfExperience,
        @URL String profilePictureUrl
) {
}
