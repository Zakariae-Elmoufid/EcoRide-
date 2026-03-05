package com.userservice.service;

import com.userservice.dto.VehiculeRequest;
import com.userservice.dto.VehiculeResponse;

import java.util.List;

public interface VehiculeService {
    VehiculeResponse addVehicule(VehiculeRequest request);

    List<VehiculeResponse> getVehiculesByDriver(Long driverId);
}
