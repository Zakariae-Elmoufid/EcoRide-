package org.project.rideservice.client;

import lombok.extern.slf4j.Slf4j;
import org.project.rideservice.dto.DriverInfoDto;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserServiceClientFallback implements UserServiceClient {

    @Override
    public DriverInfoDto getUserById(Long userId) {
        log.error("Fallback: Impossible de récupérer l'utilisateur avec l'ID: {}", userId);

        DriverInfoDto fallbackDriver = new DriverInfoDto();
        fallbackDriver.setId(userId);
        fallbackDriver.setEmail("N/A");
        fallbackDriver.setFirstName("N/A");
        fallbackDriver.setLastName("N/A");
        fallbackDriver.setPhone("N/A");
        fallbackDriver.setRole("DRIVER");

        return fallbackDriver;
    }
}

