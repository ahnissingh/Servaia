package com.ahnis.servaia.user.service.impl;

import com.ahnis.servaia.common.security.JwtUtil;
import com.ahnis.servaia.user.dto.request.AuthRequest;
import com.ahnis.servaia.user.dto.request.UserRegistrationRequest;
import com.ahnis.servaia.user.dto.response.AuthResponse;
import com.ahnis.servaia.user.enums.Role;
import com.ahnis.servaia.user.exception.UsernameOrEmailAlreadyExistsException;
import com.ahnis.servaia.user.mapper.UserMapper;
import com.ahnis.servaia.user.repository.UserRepository;
import com.ahnis.servaia.user.service.AuthService;
import com.ahnis.servaia.user.util.UserUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
//todo add therapist registery
//Main entry point for therapists and users
//for now only users
public class AuthServiceImpl implements AuthService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public AuthResponse registerUser(UserRegistrationRequest registrationDTO) {
        //Step1 Validate the request for email or username
        validateRegistration(registrationDTO);
        //Step2 Map request to entity
        var newUser = userMapper.toEntity(registrationDTO);

        //Step3 hash the password and set to entity and set roles
        newUser.setPassword(passwordEncoder.encode(registrationDTO.password()));
        newUser.setRoles(Set.of(Role.USER)); //Default role for users lmao won't give admin

        //Step4: Calculate nextReportOn based on reportFrequency in preferences

        Instant nextReportOn = UserUtils.calculateNextReportOn(Instant.now(), newUser.getPreferences().getReportFrequency());
        Instant nextReportOnFixedAtStartOfDay = nextReportOn.truncatedTo(ChronoUnit.DAYS);
        newUser.setNextReportOn(nextReportOnFixedAtStartOfDay);
        newUser.setLastReportAt(null);//Newly registered  user has no reports obviously

        //Step5 Convert entity  to object having jwt token
        var savedUser = userRepository.save(newUser);
        log.info("Saving user with username {} \n email {} \n preferences {} \n nextReportOn {}",
                registrationDTO.username(), registrationDTO.email(), registrationDTO.preferences(), newUser.getNextReportOn());
        return buildAuthResponse(savedUser);
    }

    @Override
    public AuthResponse loginUser(AuthRequest authRequest) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.usernameOrEmail(),
                        authRequest.password()));
        var user = (UserDetails) authentication.getPrincipal();
        log.info("User logged in {}", authRequest.usernameOrEmail());
        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(UserDetails user) {
        return new AuthResponse(jwtUtil.generateToken(user));
    }

    private void validateRegistration(UserRegistrationRequest dto) {
        if (userRepository.existsByUsernameOrEmail(dto.username(), dto.email()))
            throw new UsernameOrEmailAlreadyExistsException("Username or email already exists {%s , %s }  ".formatted(dto.username(), dto.email()));
    }
}
