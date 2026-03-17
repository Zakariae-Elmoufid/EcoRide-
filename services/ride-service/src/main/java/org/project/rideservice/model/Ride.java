package org.project.rideservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long driverId;


    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false, length = 100)
    private String departureCity;

    @Column(nullable = false, length = 100)
    private String arrivalCity;

    @Column(nullable = false)
    private Integer availableSeats;

    @Column(nullable = false)
    private Integer remainingSeats;

    @Column(nullable = false)
    private Double pricePerSeat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RideStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = RideStatus.OPEN;
        }
        if (remainingSeats == null) {
            remainingSeats = availableSeats;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}




