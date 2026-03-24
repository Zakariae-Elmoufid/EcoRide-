package org.example.bookingservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ride-service", fallback = RideServiceClientFallback.class)
public interface RideServiceClient {

    @GetMapping("/api/rides/{id}/exists")
    Boolean rideExists(@PathVariable("id") Long rideId);
}

