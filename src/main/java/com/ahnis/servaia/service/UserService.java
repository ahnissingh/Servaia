package com.ahnis.servaia.service;

import com.ahnis.servaia.user.dto.request.PreferencesRequest;
import com.ahnis.servaia.user.dto.request.UserUpdateRequest;
import com.ahnis.servaia.user.dto.response.TherapistResponse;
import com.ahnis.servaia.user.dto.response.UserResponse;
import com.ahnis.servaia.user.entity.Preferences;
import com.ahnis.servaia.user.entity.User;

import java.time.Instant;

public interface UserService {
    UserResponse getUserResponseByUsername(String username);

    void updateUserByUsername(String username, UserUpdateRequest updateDTO);

    void updateUserPreferences(String username, PreferencesRequest preferencesRequest);

    void deleteUserByUsername(String username);

    void updateUserReportDates(User user, Instant nextReportOn, Instant newNextReportOn);

    Preferences getUserPreferencesByUsername(String username);
    TherapistResponse getSubscribedTherapist(String username);

}
