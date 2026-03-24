package org.example.bookingservice.dto;

import lombok.Builder;
import lombok.Data;
import org.example.bookingservice.entity.BookingStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private Long rideId;
    private String passengerId;
    private String passengerEmail;
    private BookingStatus status;
    private Integer seatsBooked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

