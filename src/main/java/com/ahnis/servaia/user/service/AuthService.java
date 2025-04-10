package com.ahnis.servaia.user.service;


import com.ahnis.servaia.user.dto.request.AuthRequest;
import com.ahnis.servaia.user.dto.request.TherapistRegistrationRequest;
import com.ahnis.servaia.user.dto.request.UserRegistrationRequest;
import com.ahnis.servaia.user.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse registerUser(UserRegistrationRequest registrationDTO);

    AuthResponse loginUser(AuthRequest authRequest);

    AuthResponse registerTherapist(TherapistRegistrationRequest registrationDTO);
}

