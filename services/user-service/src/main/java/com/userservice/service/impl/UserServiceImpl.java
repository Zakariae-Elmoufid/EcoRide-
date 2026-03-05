package com.userservice.service.impl;

import com.userservice.dto.DriverResponse;
import com.userservice.dto.UserRequest;
import com.userservice.dto.UserResponse;
import com.userservice.entity.Driver;
import com.userservice.entity.User;
import com.userservice.exception.EmailAlreadyExistsException;
import com.userservice.exception.UserNotFoundException;
import com.userservice.mapper.UserMapper;
import com.userservice.repository.UserRepository;
import com.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already exists: " + request.email());
        }

        User user;
        switch (request.role()) {
            case ADMIN -> user = userMapper.toAdmin(request);
            case DRIVER -> {
                Driver driver = userMapper.toDriver(request);
                driver.setReliabilityScore(5.0); // Default score for new drivers
                user = driver;
            }
            case PASSENGER -> user = userMapper.toPassenger(request);
            default -> throw new IllegalArgumentException("Unknown role: " + request.role());
        }

        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public DriverResponse getDriverProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (!(user instanceof Driver driver)) {
            throw new IllegalArgumentException("User with id " + id + " is not a driver.");
        }

        return userMapper.toDriverResponse(driver);
    }

    @Override
    @Transactional
    public void updateReliabilityScore(Long driverId, Double newScore) {
        User user = userRepository.findById(driverId)
                .orElseThrow(() -> new UserNotFoundException("Driver not found with id: " + driverId));

        if (!(user instanceof Driver driver)) {
            throw new IllegalArgumentException("User with id " + driverId + " is not a driver.");
        }

        driver.setReliabilityScore(newScore);
        userRepository.save(driver);
    }
}
