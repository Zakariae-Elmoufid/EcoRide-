package com.userservice.dto;

import com.userservice.entity.Role;
import java.util.List;

public record DriverResponse(
                Long id,
                String email,
                String firstName,
                String lastName,
                String phone,
                Role role,
                Double reliabilityScore,
                List<VehiculeResponse> vehicules) {
}
