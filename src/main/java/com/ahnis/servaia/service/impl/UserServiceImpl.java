package com.ahnis.servaia.service.impl;

import com.ahnis.servaia.service.UserService;
import com.ahnis.servaia.user.dto.request.PreferencesRequest;
import com.ahnis.servaia.user.dto.request.UserUpdateRequest;
import com.ahnis.servaia.user.dto.response.TherapistResponse;
import com.ahnis.servaia.user.dto.response.UserResponse;
import com.ahnis.servaia.user.entity.Preferences;
import com.ahnis.servaia.user.entity.User;

import java.time.Instant;

public class UserServiceImpl implements UserService {
    @Override
    public UserResponse getUserResponseByUsername(String username) {
        return null;
    }

    @Override
    public void updateUserByUsername(String username, UserUpdateRequest updateDTO) {

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

    @Override
    public TherapistResponse getSubscribedTherapist(String username) {
        return null;
    }
}
