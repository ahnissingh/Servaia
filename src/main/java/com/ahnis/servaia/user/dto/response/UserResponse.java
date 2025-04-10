package com.ahnis.servaia.user.dto.response;



import com.ahnis.servaia.user.dto.request.PreferencesRequest;
import com.ahnis.servaia.user.enums.Role;

import java.time.Instant;
import java.util.Set;


public record UserResponse(
        String id,
        String username,
        String email,

        String firstName,
        String lastName,

        Set<Role> roles,
        PreferencesRequest preferences,
        Instant nextReportOn,
        Instant lastReportAt,
        Instant createdAt,
        Instant updatedAt,
        //todo in journal module
        int currentStreak,
        int longestStreak,
        Instant lastJournalEntryDate,//todo when we have journals
        Instant subscribedAt //todo use when adding therapists
) {
}
