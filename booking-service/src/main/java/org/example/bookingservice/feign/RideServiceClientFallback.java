package org.example.bookingservice.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RideServiceClientFallback implements RideServiceClient {

    private static final Logger log = LoggerFactory.getLogger(RideServiceClientFallback.class);

    @Override
    public Boolean rideExists(Long rideId) {
        log.warn("Fallback: ride-service is unavailable. Returning true for rideId={}", rideId);
        return true; // optimistic fallback — let the booking proceed
    }
}

