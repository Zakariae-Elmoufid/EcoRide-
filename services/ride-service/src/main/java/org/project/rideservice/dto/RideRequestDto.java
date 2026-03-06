package org.project.rideservice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDto {

    @NotNull(message = "Driver ID est obligatoire")
    private Long driverId;

    @NotNull(message = "Vehicle ID est obligatoire")
    private Long vehicleId;

    @NotNull(message = "L'heure de départ est obligatoire")
    @Future(message = "L'heure de départ doit être dans le futur")
    private LocalDateTime departureTime;

    @NotBlank(message = "La ville de départ est obligatoire")
    @Size(min = 2, max = 100, message = "La ville de départ doit contenir entre 2 et 100 caractères")
    private String departureCity;

    @NotBlank(message = "La ville d'arrivée est obligatoire")
    @Size(min = 2, max = 100, message = "La ville d'arrivée doit contenir entre 2 et 100 caractères")
    private String arrivalCity;

    @NotNull(message = "Le nombre de sièges disponibles est obligatoire")
    @Min(value = 1, message = "Le nombre de sièges doit être au moins 1")
    @Max(value = 8, message = "Le nombre de sièges ne peut pas dépasser 8")
    private Integer availableSeats;

    @NotNull(message = "Le prix par siège est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
    private Double pricePerSeat;
}

