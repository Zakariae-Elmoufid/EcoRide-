package com.userservice.dto;

public record VehiculeResponse(
        Long id,
        String brand,
        String model,
        String licensePlate,
        Integer carRange,
        Long driverId) {
}
