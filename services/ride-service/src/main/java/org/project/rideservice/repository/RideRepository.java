package org.project.rideservice.repository;

import org.project.rideservice.model.Ride;
import org.project.rideservice.model.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    // Recherche de trajets par ville de départ, ville d'arrivée et date
    @Query("SELECT r FROM Ride r WHERE " +
           "LOWER(r.departureCity) LIKE LOWER(CONCAT('%', :from, '%')) " +
           "AND LOWER(r.arrivalCity) LIKE LOWER(CONCAT('%', :to, '%')) " +
           "AND DATE(r.departureTime) = DATE(:date) " +
           "AND r.status = :status " +
           "AND r.remainingSeats > 0 " +
           "ORDER BY r.departureTime ASC")
    List<Ride> searchRides(
        @Param("from") String from,
        @Param("to") String to,
        @Param("date") LocalDateTime date,
        @Param("status") RideStatus status
    );

    // Recherche de trajets par ville de départ uniquement
    @Query("SELECT r FROM Ride r WHERE " +
           "LOWER(r.departureCity) LIKE LOWER(CONCAT('%', :from, '%')) " +
           "AND r.status = :status " +
           "AND r.remainingSeats > 0 " +
           "ORDER BY r.departureTime ASC")
    List<Ride> searchRidesByDeparture(
        @Param("from") String from,
        @Param("status") RideStatus status
    );

    // Recherche de trajets par ville d'arrivée uniquement
    @Query("SELECT r FROM Ride r WHERE " +
           "LOWER(r.arrivalCity) LIKE LOWER(CONCAT('%', :to, '%')) " +
           "AND r.status = :status " +
           "AND r.remainingSeats > 0 " +
           "ORDER BY r.departureTime ASC")
    List<Ride> searchRidesByArrival(
        @Param("to") String to,
        @Param("status") RideStatus status
    );

    // Tous les trajets ouverts
    List<Ride> findByStatusAndRemainingSeatsGreaterThan(RideStatus status, Integer seats);

    // Trajets d'un conducteur spécifique
    List<Ride> findByDriverIdOrderByDepartureTimeDesc(Long driverId);

    // Trajets par statut
    List<Ride> findByStatus(RideStatus status);
}

