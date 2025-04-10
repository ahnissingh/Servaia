package com.ahnis.servaia.user.controller;

import com.ahnis.servaia.common.dto.ApiResponse;
import com.ahnis.servaia.user.dto.request.UserRegistrationRequest;
import com.ahnis.servaia.user.dto.request.UserUpdateRequest;
import com.ahnis.servaia.user.dto.response.UserResponse;
import com.ahnis.servaia.user.service.AdminService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<UserResponse> users = adminService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @PostMapping("/users/enable/{userId}")
    public ResponseEntity<ApiResponse<Void>> enableUser(@PathVariable String userId) {
        adminService.enableUser(userId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "User enabled successfully", null));
    }

    @PostMapping("/users/disable/{userId}")
    public ResponseEntity<ApiResponse<Void>> disableUser(@PathVariable String userId) {
        adminService.disableUser(userId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "User disabled successfully", null));
    }

    @PostMapping("/users/lock/{userId}")
    public ResponseEntity<ApiResponse<Void>> lockUser(@PathVariable String userId) {
        adminService.lockUser(userId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "User locked successfully", null));
    }

    @PostMapping("/users/unlock/{userId}")
    public ResponseEntity<ApiResponse<Void>> unlockUser(@PathVariable String userId) {
        adminService.unlockUser(userId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "User unlocked successfully", null));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String userId) {
        adminService.deleteUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "User deleted successfully", null));
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable String userId,
            @RequestBody UserUpdateRequest updateRequest) {
        UserResponse updatedUser = adminService.updateUserById(userId, updateRequest);
        return ResponseEntity.ok(ApiResponse.success(updatedUser));
    }

    @PostMapping("/users/bulk")
    public ResponseEntity<ApiResponse<Void>> registerMultipleUsers(
            @RequestBody List<UserRegistrationRequest> registrationRequests) {
        adminService.registerMultipleUsers(registrationRequests);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.CREATED, "Users created successfully", null));
    }
}
