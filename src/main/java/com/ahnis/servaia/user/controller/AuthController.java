package com.ahnis.servaia.user.controller;

import com.ahnis.servaia.user.dto.request.AuthRequest;
import com.ahnis.servaia.user.dto.request.UserRegistrationRequest;
import com.ahnis.servaia.user.dto.response.AuthResponse;
import com.ahnis.servaia.user.service.AuthService;
import com.ahnis.servaia.user.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final PasswordResetService passwordResetService;
    //todo will add a ps reset service later dummy code now
    //private final PasswordResetService

    /// Handles both users and therapists
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.loginUser(authRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register/user")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
        var registeredUserAuthResponse = authService.registerUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registeredUserAuthResponse);
    }

    //todo
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        passwordResetService.sendPasswordResetEmail(email);
        return "Password reset email sent.";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        passwordResetService.resetPassword(token, newPassword);
        return "Password reset successfully.";
    }
}
