package com.userservice.dto;

import com.userservice.entity.Role;

public record UserRequest(
                String email,
                String firstName,
                String lastName,
                String phone,
                Role role) {
}
