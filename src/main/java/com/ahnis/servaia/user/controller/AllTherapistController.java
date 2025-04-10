package com.ahnis.servaia.user.controller;

import com.ahnis.servaia.user.dto.response.TherapistResponse;
import com.ahnis.servaia.user.entity.User;
import com.ahnis.servaia.user.service.TherapistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// For users to view/search/interact with therapists
@Slf4j
@RestController
@RequestMapping("/api/v1/therapists")
@RequiredArgsConstructor
public class AllTherapistController {
    private final TherapistService therapistService;

    @GetMapping
    public Page<TherapistResponse> getAllTherapists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return therapistService.getAllTherapists(page, size);
    }

    @GetMapping("/search")
    public List<TherapistResponse> searchTherapists(
            @RequestParam(required = false) String specialty,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName

    ) {
        return therapistService.search(specialty, firstName, lastName);
    }

    @PostMapping("/{therapistId}/subscribe")
    public void subscribe(
            @PathVariable String therapistId,
            @AuthenticationPrincipal User user
    ) {
        log.info("Therapist ko subscribe karne laga {} ", therapistId);
        therapistService.subscribe(user, therapistId);
    }


}
