package com.userservice.dto;

import com.userservice.entity.Role;

public record UserResponse(
                Long id,
                String email,
                String firstName,
                String lastName,
                String phone,
                Role role) {
}
