package com.userservice.dto;

public record VehiculeRequest(
        String brand,
        String model,
        String licensePlate,
        Integer carRange,
        Long driverId) {
}
