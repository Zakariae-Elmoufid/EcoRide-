package org.project.rideservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.project.rideservice.model.RideStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideResponseDto {

    private Long id;
    private Long driverId;
    private Long vehicleId;
    private LocalDateTime departureTime;
    private String departureCity;
    private String arrivalCity;
    private Integer availableSeats;
    private Integer remainingSeats;
    private Double pricePerSeat;
    private RideStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Info supplémentaire pour le driver (sera récupérée via Feign)
    private DriverInfoDto driverInfo;
}

