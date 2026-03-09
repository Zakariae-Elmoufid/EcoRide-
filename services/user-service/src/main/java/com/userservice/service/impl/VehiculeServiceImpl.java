package com.userservice.service.impl;

import com.userservice.dto.VehiculeRequest;
import com.userservice.dto.VehiculeResponse;
import com.userservice.entity.Driver;
import com.userservice.entity.Vehicule;
import com.userservice.exception.UserNotFoundException;
import com.userservice.mapper.VehiculeMapper;
import com.userservice.repository.UserRepository;
import com.userservice.repository.VehiculeRepository;
import com.userservice.service.VehiculeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final UserRepository userRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    @Transactional
    public VehiculeResponse addVehicule(VehiculeRequest request) {
        Driver driver = (Driver) userRepository.findById(request.driverId())
                .orElseThrow(() -> new UserNotFoundException("Driver not found with id: " + request.driverId()));

        Vehicule vehicule = vehiculeMapper.toEntity(request);
        vehicule.setDriver(driver);
        vehicule = vehiculeRepository.save(vehicule);

        return vehiculeMapper.toResponse(vehicule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculeResponse> getVehiculesByDriver(Long driverId) {
        if (!userRepository.existsById(driverId)) {
            throw new UserNotFoundException("Driver not found with id: " + driverId);
        }
        return vehiculeRepository.findByDriverId(driverId).stream()
                .map(vehiculeMapper::toResponse)
                .collect(Collectors.toList());
    }
}
