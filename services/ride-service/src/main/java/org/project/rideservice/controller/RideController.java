package org.project.rideservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.rideservice.dto.RideRequestDto;
import org.project.rideservice.dto.RideResponseDto;
import org.project.rideservice.dto.UpdateSeatsDto;
import org.project.rideservice.service.RideService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
@Slf4j
public class RideController {

    private final RideService rideService;

    /**
     * POST /api/rides — Créer un nouveau trajet (DRIVER uniquement)
     */
    @PostMapping
    public ResponseEntity<RideResponseDto> createRide(@RequestBody RideRequestDto request) {
        log.info("POST /api/rides - Création d'un trajet");
        RideResponseDto response = rideService.createRide(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/rides?from=&to=&date= — Rechercher des trajets
     */
    @GetMapping
    public ResponseEntity<List<RideResponseDto>> searchRides(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        log.info("GET /api/rides - Recherche: from={}, to={}, date={}", from, to, date);
        List<RideResponseDto> rides = rideService.searchRides(from, to, date);
        return ResponseEntity.ok(rides);
    }

    /**
     * GET /api/rides/{id} — Obtenir les détails d'un trajet (avec info driver)
     */
    @GetMapping("/{id}")
    public ResponseEntity<RideResponseDto> getRideById(@PathVariable Long id) {
        log.info("GET /api/rides/{} - Détails du trajet", id);
        RideResponseDto response = rideService.getRideById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/rides/{id}/seats — Décrémenter les sièges disponibles
     */
    @PutMapping("/{id}/seats")
    public ResponseEntity<RideResponseDto> updateSeats(
            @PathVariable Long id,
            @RequestBody UpdateSeatsDto request) {

        log.info("PUT /api/rides/{}/seats - Décrémentation de {} siège(s)", id, request.getSeatsToDecrement());
        RideResponseDto response = rideService.decrementSeats(id, request.getSeatsToDecrement());
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/rides/{id}/complete — Terminer un trajet
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<RideResponseDto> completeRide(@PathVariable Long id) {
        log.info("PUT /api/rides/{}/complete - Complétion du trajet", id);
        RideResponseDto response = rideService.completeRide(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/rides/driver/{driverId} — Obtenir tous les trajets d'un conducteur
     */
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<RideResponseDto>> getRidesByDriver(@PathVariable Long driverId) {
        log.info("GET /api/rides/driver/{} - Trajets du conducteur", driverId);
        List<RideResponseDto> rides = rideService.getRidesByDriverId(driverId);
        return ResponseEntity.ok(rides);
    }
}



