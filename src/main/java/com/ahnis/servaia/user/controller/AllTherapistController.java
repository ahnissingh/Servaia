package com.ahnis.servaia.user.controller;

import com.ahnis.servaia.user.dto.response.TherapistResponse;
import com.ahnis.servaia.user.entity.User;
import com.ahnis.servaia.user.service.TherapistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// For users to view/search/interact with therapists
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
            @RequestParam(required = false) String username
    ) {
        return therapistService.search(specialty, username);
    }

    @PostMapping("/{therapistId}/subscribe")
    public void subscribe(
            @PathVariable String therapistId,
            @AuthenticationPrincipal User user
    ) {
        therapistService.subscribe(user.getId(), therapistId);
    }


}
