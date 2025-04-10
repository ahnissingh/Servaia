package com.ahnis.servaia.user.service.impl;

import com.ahnis.servaia.user.exception.EmailAlreadyExistsException;
import com.ahnis.servaia.user.mapper.UserMapper;
import com.ahnis.servaia.user.service.UserService;
import com.ahnis.servaia.user.dto.request.PreferencesRequest;
import com.ahnis.servaia.user.dto.request.UserUpdateRequest;
import com.ahnis.servaia.user.dto.response.TherapistResponse;
import com.ahnis.servaia.user.dto.response.UserResponse;
import com.ahnis.servaia.user.entity.Preferences;
import com.ahnis.servaia.user.entity.User;
import com.ahnis.servaia.user.repository.UserRepository;
import com.ahnis.servaia.user.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponse getUserResponseByUsername(String username) {
        return userMapper.toResponseDto(this.getUserByUsername(username));

    }

    @Override
    public void updateUserByUsername(String username, UserUpdateRequest updateDTO) {
        var currentUser = getUserByUsername(username);
        if (updateDTO.email() != null && !updateDTO.email().equals(currentUser.getEmail())) {
            validateEmail(updateDTO.email());
            userRepository.updateEmailByUsername(username, updateDTO.email());
        }

        // Update password if provided
        if (updateDTO.password() != null) {
            userRepository.updatePasswordByUsername(username, passwordEncoder.encode(updateDTO.password()));
        }
        // Update preferences if provided
        //todo patch update every property I think can give better perfomance
        var updatedPreferences = updateDTO.preferences();
        if (updatedPreferences != null) {
            if (updatedPreferences.reportFrequency() != null && !updatedPreferences.reportFrequency().equals(currentUser.getPreferences().getReportFrequency())) {
                var nextReportOn = UserUtils.calculateNextReportOn(Instant.now(), updatedPreferences.reportFrequency());
                userRepository.updateNextReportOnById(currentUser.getId(), nextReportOn);
            }
            userRepository.updatePreferencesByUsername(username, userMapper.toPreferencesEntity(updatedPreferences));
        }
    }

    @Override
    public void updateUserPreferences(String username, PreferencesRequest preferencesRequest) {

    }

    @Override
    public void deleteUserByUsername(String username) {

    }

    @Override
    public void updateUserReportDates(User user, Instant nextReportOn, Instant newNextReportOn) {

    }

    @Override
    public Preferences getUserPreferencesByUsername(String username) {
        return null;
    }

    //todo impl when therapists
    @Override
    public TherapistResponse getSubscribedTherapist(String username) {
        return null;
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email))
            throw new EmailAlreadyExistsException(email);
    }


}
