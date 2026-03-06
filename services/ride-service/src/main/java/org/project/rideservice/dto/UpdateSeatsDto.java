package org.project.rideservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSeatsDto {

    @NotNull(message = "Le nombre de sièges à décrémenter est obligatoire")
    @Min(value = 1, message = "Le nombre de sièges doit être au moins 1")
    @Max(value = 8, message = "Le nombre de sièges ne peut pas dépasser 8")
    private Integer seatsToDecrement;
}

