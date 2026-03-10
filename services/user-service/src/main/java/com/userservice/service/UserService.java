package com.userservice.service;

import com.userservice.dto.DriverResponse;
import com.userservice.dto.UserRequest;
import com.userservice.dto.UserResponse;

public interface UserService {
    UserResponse createUser(String keycloakId,UserRequest request);

    UserResponse getUserById(Long id);
    UserResponse getUserByKeycloakId(String keycloakId);

    UserResponse getUserByEmail(String email);

    DriverResponse getDriverProfile(Long id);

    void updateReliabilityScore(Long driverId, Double newScore);
}
