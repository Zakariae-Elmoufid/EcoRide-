package org.project.rideservice.client;

import org.project.rideservice.dto.DriverInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", fallback = UserServiceClientFallback.class)
public interface UserServiceClient {

    @GetMapping("/api/users/{userId}")
    DriverInfoDto getUserById(@PathVariable("userId") Long userId);
}

