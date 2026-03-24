package org.example.bookingservice.repository;

import org.example.bookingservice.entity.Booking;
import org.example.bookingservice.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByPassengerId(String passengerId);

    List<Booking> findByRideId(Long rideId);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByPassengerIdAndStatus(String passengerId, BookingStatus status);
}

