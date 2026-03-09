package com.userservice.mapper;

import org.mapstruct.Mapper;
import com.userservice.entity.*;
import com.userservice.dto.*;

@Mapper(componentModel = "spring", uses = { VehiculeMapper.class })
public interface UserMapper {

    UserResponse toResponse(User entity);

    DriverResponse toDriverResponse(Driver entity);

    Admin toAdmin(UserRequest request);

    Passenger toPassenger(UserRequest request);

    Driver toDriver(UserRequest request);
}
