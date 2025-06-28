package com.example.RealEstateBackend.service;

import com.example.RealEstateBackend.dto.user.UpdateUserRequest;
import com.example.RealEstateBackend.dto.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserResponse> getAllUsers(Pageable pageable);
    UserResponse getUserById(String id);
    UserResponse updateUser(String id, UpdateUserRequest request);
    void deleteUser(String id);
}
