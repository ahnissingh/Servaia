package com.ahnis.servaia.user.service.impl;

import com.ahnis.servaia.user.dto.request.UserRegistrationRequest;
import com.ahnis.servaia.user.dto.request.UserUpdateRequest;
import com.ahnis.servaia.user.dto.response.UserResponse;
import com.ahnis.servaia.user.entity.User;
import com.ahnis.servaia.user.exception.UserNotFoundException;
import com.ahnis.servaia.user.mapper.UserMapper;
import com.ahnis.servaia.user.repository.UserRepository;
import com.ahnis.servaia.user.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponseDto);
    }


    @Override
    public void enableUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void disableUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public void lockUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        user.setAccountNonLocked(false);
        userRepository.save(user);
    }

    @Override
    public void unlockUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        user.setAccountNonLocked(true);
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public UserResponse updateUserById(String userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        if (userUpdateRequest.email() != null && !userUpdateRequest.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userUpdateRequest.email())) {
                throw new IllegalArgumentException("Email already in use");
            }
            user.setEmail(userUpdateRequest.email());
        }

        if (userUpdateRequest.password() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateRequest.password()));
        }

        if (userUpdateRequest.preferences() != null) {
            user.setPreferences(userMapper.toPreferencesEntity(userUpdateRequest.preferences()));
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public void registerMultipleUsers(List<UserRegistrationRequest> userRegistrationRequestList) {
        List<User> userWithEncodedPassword = userRegistrationRequestList.stream()
                .map(request -> {
                    var user = userMapper.toEntity(request);
                    user.setPassword(passwordEncoder.encode(request.password()));
                    return user;
                })
                .toList();

        userRepository.saveAll(userWithEncodedPassword);
    }
}
