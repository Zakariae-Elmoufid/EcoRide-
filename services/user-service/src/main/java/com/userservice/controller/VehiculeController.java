package com.userservice.controller;

import com.userservice.dto.VehiculeRequest;
import com.userservice.dto.VehiculeResponse;
import com.userservice.service.VehiculeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private final VehiculeService vehiculeService;

    @PostMapping
    public ResponseEntity<VehiculeResponse> addVehicule(@RequestBody VehiculeRequest request) {
        VehiculeResponse createdVehicule = vehiculeService.addVehicule(request);
        return new ResponseEntity<>(createdVehicule, HttpStatus.CREATED);
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<VehiculeResponse>> getVehiculesByDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(vehiculeService.getVehiculesByDriver(driverId));
    }
}
