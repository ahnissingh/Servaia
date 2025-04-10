package com.ahnis.servaia.user.dto.response;

import com.ahnis.servaia.user.entity.Therapist;
import com.ahnis.servaia.user.enums.Language;

import java.time.Instant;
import java.util.Set;

public record TherapistPersonalResponse(
        String id,
        String username,
        String email,
        String firstname,
        String lastName,
        String licenseNumber,
        Set<String> specialties,
        Set<Language> spokenLanguages,
        int yearsOfExperience,
        String bio,
        String profilePictureUrl,
        int clientCount,
        Instant createdAt
) {
    public static TherapistPersonalResponse fromEntity(Therapist therapist) {
        return new TherapistPersonalResponse(
                therapist.getId(),
                therapist.getUsername(),
                therapist.getEmail(),
                therapist.getFirstName(),
                therapist.getLastName(),
                therapist.getLicenseNumber(),
                therapist.getSpecialties(),
                therapist.getLanguages(),
                therapist.getYearsOfExperience(),
                therapist.getBio(),
                therapist.getProfilePictureUrl(),
                therapist.getClientUserId().size(),
                therapist.getCreatedAt()
        );
    }
}
