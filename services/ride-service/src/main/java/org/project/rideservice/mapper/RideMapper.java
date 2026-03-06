package org.project.rideservice.mapper;

import org.project.rideservice.dto.RideRequestDto;
import org.project.rideservice.dto.RideResponseDto;
import org.project.rideservice.model.Ride;
import org.springframework.stereotype.Component;

@Component
public class RideMapper {

    public Ride toEntity(RideRequestDto dto) {
        Ride ride = new Ride();
        ride.setDriverId(dto.getDriverId());
        ride.setVehicleId(dto.getVehicleId());
        ride.setDepartureTime(dto.getDepartureTime());
        ride.setDepartureCity(dto.getDepartureCity());
        ride.setArrivalCity(dto.getArrivalCity());
        ride.setAvailableSeats(dto.getAvailableSeats());
        ride.setRemainingSeats(dto.getAvailableSeats()); // Initialement égal à availableSeats
        ride.setPricePerSeat(dto.getPricePerSeat());
        return ride;
    }

    public RideResponseDto toDto(Ride ride) {
        RideResponseDto dto = new RideResponseDto();
        dto.setId(ride.getId());
        dto.setDriverId(ride.getDriverId());
        dto.setVehicleId(ride.getVehicleId());
        dto.setDepartureTime(ride.getDepartureTime());
        dto.setDepartureCity(ride.getDepartureCity());
        dto.setArrivalCity(ride.getArrivalCity());
        dto.setAvailableSeats(ride.getAvailableSeats());
        dto.setRemainingSeats(ride.getRemainingSeats());
        dto.setPricePerSeat(ride.getPricePerSeat());
        dto.setStatus(ride.getStatus());
        dto.setCreatedAt(ride.getCreatedAt());
        dto.setUpdatedAt(ride.getUpdatedAt());
        return dto;
    }
}

