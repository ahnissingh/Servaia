package com.ahnis.servaia.user.dto.response;

import com.ahnis.servaia.user.enums.Language;

import java.util.Set;

public record TherapistResponse(
        String id,
        String username,
        String firstName,
        String lastName,
        Set<String> specialties,
        Set<Language> spokenLanguages,
        int yearsOfExperience,
        String bio,
        String profilePictureUrl
) {
}
