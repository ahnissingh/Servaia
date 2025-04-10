package com.ahnis.servaia.user.service;


import com.ahnis.servaia.user.dto.request.AuthRequest;
import com.ahnis.servaia.user.dto.request.UserRegistrationRequest;
import com.ahnis.servaia.user.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse registerUser(UserRegistrationRequest registrationDTO);

    AuthResponse loginUser(AuthRequest authRequest);
//todo designed this uncomment when therapist
//    AuthResponse registerTherapist(TherapistRegistrationRequest registrationDTO);
}

