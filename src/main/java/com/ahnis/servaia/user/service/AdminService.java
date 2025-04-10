package com.ahnis.servaia.user.service;


import com.ahnis.servaia.user.dto.request.UserRegistrationRequest;
import com.ahnis.servaia.user.dto.request.UserUpdateRequest;
import com.ahnis.servaia.user.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {

    //new methods
    Page<UserResponse> getAllUsers(Pageable pageable);

    void enableUser(String userId);

    void disableUser(String userId);

    void lockUser(String userId);

    void unlockUser(String userId);

    void deleteUserById(String userId);

    UserResponse updateUserById(String userId, UserUpdateRequest userUpdateRequest);

    void registerMultipleUsers(List<UserRegistrationRequest> userRegistrationRequestList);

}

