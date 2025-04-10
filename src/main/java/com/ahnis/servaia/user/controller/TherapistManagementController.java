package com.ahnis.servaia.user.controller;

import com.ahnis.servaia.common.dto.ApiResponse;
import com.ahnis.servaia.user.dto.request.TherapistUpdateRequest;
import com.ahnis.servaia.user.dto.response.TherapistClientResponse;
import com.ahnis.servaia.user.dto.response.TherapistPersonalResponse;
import com.ahnis.servaia.user.entity.Therapist;
import com.ahnis.servaia.user.service.TherapistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/therapists/me")
@PreAuthorize("hasRole('ROLE_THERAPIST')")
@RequiredArgsConstructor

//todo add delete api
public class TherapistManagementController {
    private final TherapistService therapistService;

    @GetMapping
    public ApiResponse<TherapistPersonalResponse> getTherapistProfile(
            @AuthenticationPrincipal Therapist therapist
    ) {
        return ApiResponse.success(
                therapistService.getProfile(therapist.getId())
        );
    }

    @PatchMapping
    public ApiResponse<Void> updateProfile(
            @Valid @RequestBody TherapistUpdateRequest request,
            @AuthenticationPrincipal Therapist therapist
    ) {
        therapistService.updateProfile(therapist.getId(), request);
        return ApiResponse.success(HttpStatus.NO_CONTENT, "Profile updated", null);
    }

    @GetMapping("/clients")
    public ApiResponse<List<TherapistClientResponse>> getMyClients(
            @AuthenticationPrincipal Therapist therapist
    ) {
        return ApiResponse.success(
                therapistService.getClients(therapist.getId())
        );
    }
}
