package com.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.userservice.entity.Vehicule;
import com.userservice.dto.VehiculeRequest;
import com.userservice.dto.VehiculeResponse;

@Mapper(componentModel = "spring")
public interface VehiculeMapper {
    @Mapping(target = "driver.id", source = "driverId")
    Vehicule toEntity(VehiculeRequest request);

    @Mapping(target = "driverId", source = "driver.id")
    VehiculeResponse toResponse(Vehicule entity);
}
